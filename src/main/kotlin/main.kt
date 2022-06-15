import domain.*
import domain.clock.*
import infrastructure.ArgOrEnvParser
import infrastructure.eventbus.EventBus
import infrastructure.eventbus.SynchronousEventBus
import infrastructure.housecontrol.CanControlDayNightMode
import infrastructure.housecontrol.CanControlHeatingMode
import infrastructure.housecontrol.CanControlJalousie
import infrastructure.housecontrol.dryrun.DryRunBasedDayNightModeController
import infrastructure.housecontrol.dryrun.DryRunBasedHeatingModeController
import infrastructure.housecontrol.dryrun.DryRunBasedJalousieController
import infrastructure.housecontrol.knx.KnxBasedDayNightModeController
import infrastructure.housecontrol.knx.KnxBasedHeatingModeController
import infrastructure.housecontrol.knx.KnxBasedJalousieController
import infrastructure.knx.GroupAddress
import tuwien.auto.calimero.link.KNXNetworkLinkIP
import tuwien.auto.calimero.link.medium.TPSettings
import tuwien.auto.calimero.process.ProcessCommunicatorImpl
import java.net.InetSocketAddress
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

fun main(args: Array<String>) {
    val argEnvParser = ArgOrEnvParser("house-knx-automation", args, System.getenv())

    val dryRun = argEnvParser.optionalBoolean("dryRun", "DRY_RUN", false)

    val knxGatewayAddress = argEnvParser.requiredString("knxGatewayAddress", "KNX_GATEWAY_ADDRESS")
    val knxGatewayPort = argEnvParser.requiredInt("knxGatewayPort", "KNX_GATEWAY_PORT")

    val timeZone = argEnvParser.requiredString("timeZone", "TIME_ZONE")
    val locationLat = argEnvParser.requiredDouble("locationLat", "LOCATION_LAT")
    val locationLon = argEnvParser.requiredDouble("locationLon", "LOCATION_LON")
    val morningTime = argEnvParser.requiredString("morningTime", "MORNING_TIME")
    val eveningTime = argEnvParser.requiredString("eveningTime", "EVENING_TIME")

    val allJalousieControlGroupAddress = argEnvParser.requiredString("allJalousieControlGroupAddress", "ALL_JALOUSIE_CONTROL_GROUP_ADDRESS")
    val allJalousieExceptBedroomsControlGroupAddress = argEnvParser.requiredString("allJalousieExceptBedroomsControlGroupAddress", "ALL_JALOUSIE_EXCEPT_BEDROOMS_CONTROL_GROUP_ADDRESS")
    val dayNightModeControlGroupAddress = argEnvParser.requiredString("dayNightModeControlGroupAddress", "DAY_NIGHT_MODE_CONTROL_GROUP_ADDRESS")
    val heatingModeControlGroupAddress = argEnvParser.requiredString("heatingModeControlGroupAddress", "HEATING_MODE_CONTROL_GROUP_ADDRESS")

    val offsetBeforeSunriseInSeconds = argEnvParser.optionalLong("offsetBeforeSunriseInSeconds", "OFFSET_BEFORE_SUNRISE_IN_SECONDS", 0)
    val offsetAfterSunriseInSeconds = argEnvParser.optionalLong("offsetAfterSunriseInSeconds", "OFFSET_AFTER_SUNRISE_IN_SECONDS", 0)
    val offsetBeforeSunsetInSeconds = argEnvParser.optionalLong("offsetBeforeSunsetInSeconds", "OFFSET_BEFORE_SUNSET_IN_SECONDS", 0)
    val offsetAfterSunsetInSeconds = argEnvParser.optionalLong("offsetAfterSunsetInSeconds", "OFFSET_AFTER_SUNSET_IN_SECONDS", 0)

    val offsetSunrise = offsetBeforeSunriseInSeconds.toLong() + offsetAfterSunriseInSeconds.toLong()
    val offsetSunset = offsetBeforeSunsetInSeconds.toLong() + offsetAfterSunsetInSeconds.toLong()

    argEnvParser.parse()

    val localAddress = InetSocketAddress(0)
    val gatewayAddress = InetSocketAddress(knxGatewayAddress.toString(), knxGatewayPort.toInt())

    SynchronousEventBus().let { eventBus ->
        val sunriseSunsetEventEmitter = SunriseSunsetEventEmitter(
            eventBus,
            Clock.systemDefaultZone(),
            ZoneId.of(timeZone.toString()),
            locationLat.toDouble(),
            locationLon.toDouble(),
            offsetSunrise,
            offsetSunset
        )

        val morningEveningEventEmitter = MorningEveningEventEmitter(
            eventBus,
            Clock.systemDefaultZone(),
            ZoneId.of(timeZone.toString()),
            LocalTime.parse(morningTime.toString()),
            LocalTime.parse(eveningTime.toString()),
        )

        if (!dryRun.toBoolean()) {
            KNXNetworkLinkIP.newTunnelingLink(
                localAddress,
                gatewayAddress,
                true,
                TPSettings.TP1
            ).use { knxLink ->
                ProcessCommunicatorImpl(knxLink).use { processCommunicator ->
                    val dayNightModeController = KnxBasedDayNightModeController(
                        processCommunicator,
                        GroupAddress.fromString(dayNightModeControlGroupAddress.toString())
                    )

                    val heatingModeController =
                        KnxBasedHeatingModeController(
                            processCommunicator,
                            GroupAddress.fromString(heatingModeControlGroupAddress.toString())
                        )

                    val jalousieController =
                        KnxBasedJalousieController(
                            processCommunicator,
                            GroupAddress.fromString(allJalousieControlGroupAddress.toString()),
                            GroupAddress.fromString(allJalousieExceptBedroomsControlGroupAddress.toString())
                        )

                    configureEventBus(eventBus, dayNightModeController, heatingModeController, jalousieController)

                    while (knxLink.isOpen) eventBusTick(sunriseSunsetEventEmitter, morningEveningEventEmitter)
                }
            }
        }
        else {
            val dayNightModeController = DryRunBasedDayNightModeController()
            val heatingModeController = DryRunBasedHeatingModeController()
            val jalousieController = DryRunBasedJalousieController()

            configureEventBus(eventBus, dayNightModeController, heatingModeController, jalousieController)

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
    eventBus.listen(SunriseEvent::class, OnSunriseTurnDayModeOn(dayNightModeController))
    eventBus.listen(SunriseEvent::class, OnSunriseMoveJalousieUp(jalousieController))
    eventBus.listen(SunsetEvent::class, OnSunsetTurnNightModeOn(dayNightModeController))
    eventBus.listen(SunsetEvent::class, OnSunsetMoveJalousieDown(jalousieController))
    eventBus.listen(ReachedMorningEvent::class, InTheMorningTurnOnHeatingComfortMode(heatingModeController))
    eventBus.listen(ReachedEveningEvent::class, InTheEveningTurnOnHeatingNightMode(heatingModeController))
}
fun eventBusTick(
    sunriseSunsetEventEmitter: SunriseSunsetEventEmitter,
    morningEveningEventEmitter: MorningEveningEventEmitter,
) {
    sunriseSunsetEventEmitter.tick()
    morningEveningEventEmitter.tick()
    Thread.sleep(1000)
}
