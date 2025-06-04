package houseknxautomation.infrastructure

import houseknxautomation.domain.InTheEveningTurnOnHeatingNightMode
import houseknxautomation.domain.InTheMorningTurnOnHeatingComfortMode
import houseknxautomation.domain.OnSunriseMoveJalousieUp
import houseknxautomation.domain.OnSunriseTurnDayModeOn
import houseknxautomation.domain.OnSunsetMoveJalousieDown
import houseknxautomation.domain.OnSunsetTurnNightModeOn
import houseknxautomation.domain.clock.MorningEveningEventEmitter
import houseknxautomation.domain.clock.ReachedEveningEvent
import houseknxautomation.domain.clock.ReachedMorningEvent
import houseknxautomation.domain.clock.SunriseEvent
import houseknxautomation.domain.clock.SunriseSunsetEventEmitter
import houseknxautomation.domain.clock.SunsetEvent
import houseknxautomation.infrastructure.configuration.HouseKnxAutomationConfigProperties
import houseknxautomation.infrastructure.eventbus.EventBus
import houseknxautomation.infrastructure.eventbus.SynchronousEventBus
import houseknxautomation.infrastructure.eventbus.logging.ObjectBasedListenerLoggingFacade
import houseknxautomation.infrastructure.housecontrol.CanControlDayNightMode
import houseknxautomation.infrastructure.housecontrol.CanControlHeatingMode
import houseknxautomation.infrastructure.housecontrol.CanControlJalousie
import houseknxautomation.infrastructure.housecontrol.dryrun.DryRunBasedDayNightModeController
import houseknxautomation.infrastructure.housecontrol.dryrun.DryRunBasedHeatingModeController
import houseknxautomation.infrastructure.housecontrol.dryrun.DryRunBasedJalousieController
import houseknxautomation.infrastructure.housecontrol.knx.KnxBasedDayNightModeController
import houseknxautomation.infrastructure.housecontrol.knx.KnxBasedHeatingModeController
import houseknxautomation.infrastructure.housecontrol.knx.KnxBasedJalousieController
import houseknxautomation.infrastructure.housecontrol.logging.DayNightModeControllerLoggingFacade
import houseknxautomation.infrastructure.housecontrol.logging.HeatingModeControllerLoggingFacade
import houseknxautomation.infrastructure.housecontrol.logging.JalousieControllerLoggingFacade
import houseknxautomation.infrastructure.knx.GroupAddress
import io.calimero.link.KNXNetworkLinkIP
import io.calimero.link.medium.TPSettings
import io.calimero.process.ProcessCommunicatorImpl
import java.net.InetSocketAddress
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId
import kotlin.use
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
@ConfigurationPropertiesScan
class HouseKnxAutomationApplication(private var config: HouseKnxAutomationConfigProperties) :
    CommandLineRunner {

    override fun run(vararg args: String?) {
        SynchronousEventBus().let { eventBus ->
            val sunriseSunsetEventEmitter =
                SunriseSunsetEventEmitter(
                    eventBus,
                    Clock.systemDefaultZone(),
                    ZoneId.of(config.timeZone),
                    config.locationLat.toDouble(),
                    config.locationLon.toDouble(),
                    config.sunriseEarliest?.let { LocalTime.parse(it) },
                    config.sunriseLatest?.let { LocalTime.parse(it) },
                    config.sunsetEarliest?.let { LocalTime.parse(it) },
                    config.sunsetLatest?.let { LocalTime.parse(it) },
                    config.offsetBeforeSunriseInSeconds.toLong() +
                        config.offsetAfterSunriseInSeconds.toLong(),
                    config.offsetBeforeSunsetInSeconds.toLong() +
                        config.offsetAfterSunsetInSeconds.toLong(),
                )

            val morningEveningEventEmitter =
                MorningEveningEventEmitter(
                    eventBus,
                    Clock.systemDefaultZone(),
                    ZoneId.of(config.timeZone),
                    LocalTime.parse(config.morningTime),
                    LocalTime.parse(config.eveningTime),
                )

            val logger = LoggerFactory.getLogger("Controller")

            if (!config.dryRun.toBoolean()) {
                KNXNetworkLinkIP.newTunnelingLink(
                        InetSocketAddress(0),
                        InetSocketAddress(config.knxGatewayAddress, config.knxGatewayPort.toInt()),
                        true,
                        TPSettings(),
                    )
                    .use { knxLink ->
                        ProcessCommunicatorImpl(knxLink).use { processCommunicator ->
                            val dayNightModeController =
                                DayNightModeControllerLoggingFacade(
                                    logger,
                                    KnxBasedDayNightModeController(
                                        processCommunicator,
                                        GroupAddress.fromString(
                                            config.dayNightModeControlGroupAddress
                                        ),
                                    ),
                                )

                            val heatingModeController =
                                HeatingModeControllerLoggingFacade(
                                    logger,
                                    KnxBasedHeatingModeController(
                                        processCommunicator,
                                        GroupAddress.fromString(
                                            config.heatingModeControlGroupAddress
                                        ),
                                    ),
                                )

                            val jalousieController =
                                JalousieControllerLoggingFacade(
                                    logger,
                                    KnxBasedJalousieController(
                                        processCommunicator,
                                        GroupAddress.fromString(
                                            config.allJalousieControlGroupAddress
                                        ),
                                        GroupAddress.fromString(
                                            config.allJalousieExceptBedroomsControlGroupAddress
                                        ),
                                    ),
                                )

                            configureEventBus(
                                eventBus,
                                dayNightModeController,
                                heatingModeController,
                                jalousieController,
                            )

                            while (knxLink.isOpen) eventBusTick(
                                sunriseSunsetEventEmitter,
                                morningEveningEventEmitter,
                            )
                        }
                    }
            } else {
                val dayNightModeController =
                    DayNightModeControllerLoggingFacade(logger, DryRunBasedDayNightModeController())
                val heatingModeController =
                    HeatingModeControllerLoggingFacade(logger, DryRunBasedHeatingModeController())
                val jalousieController =
                    JalousieControllerLoggingFacade(logger, DryRunBasedJalousieController())

                configureEventBus(
                    eventBus,
                    dayNightModeController,
                    heatingModeController,
                    jalousieController,
                )

                while (true) eventBusTick(sunriseSunsetEventEmitter, morningEveningEventEmitter)
            }
        }
    }

    fun configureEventBus(
        eventBus: EventBus,
        dayNightModeController: CanControlDayNightMode,
        heatingModeController: CanControlHeatingMode,
        jalousieController: CanControlJalousie,
    ) {
        val logger = LoggerFactory.getLogger("EventBus")

        eventBus.listen(
            SunriseEvent::class,
            ObjectBasedListenerLoggingFacade(logger, OnSunriseTurnDayModeOn(dayNightModeController)),
        )
        eventBus.listen(
            SunriseEvent::class,
            ObjectBasedListenerLoggingFacade(logger, OnSunriseMoveJalousieUp(jalousieController)),
        )
        eventBus.listen(
            SunsetEvent::class,
            ObjectBasedListenerLoggingFacade(
                logger,
                OnSunsetTurnNightModeOn(dayNightModeController),
            ),
        )
        eventBus.listen(
            SunsetEvent::class,
            ObjectBasedListenerLoggingFacade(logger, OnSunsetMoveJalousieDown(jalousieController)),
        )
        eventBus.listen(
            SunsetEvent::class,
            ObjectBasedListenerLoggingFacade(logger, OnSunsetMoveJalousieDown(jalousieController)),
        )
        eventBus.listen(
            ReachedMorningEvent::class,
            ObjectBasedListenerLoggingFacade(
                logger,
                InTheMorningTurnOnHeatingComfortMode(heatingModeController),
            ),
        )
        eventBus.listen(
            ReachedEveningEvent::class,
            ObjectBasedListenerLoggingFacade(
                logger,
                InTheEveningTurnOnHeatingNightMode(heatingModeController),
            ),
        )
    }

    fun eventBusTick(
        sunriseSunsetEventEmitter: SunriseSunsetEventEmitter,
        morningEveningEventEmitter: MorningEveningEventEmitter,
    ) {
        sunriseSunsetEventEmitter.tick()
        morningEveningEventEmitter.tick()
        Thread.sleep(1000)
    }
}

@ExperimentalUnsignedTypes
fun main(args: Array<String>) {
    runApplication<HouseKnxAutomationApplication>(*args)
}
