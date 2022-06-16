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

    val dryRunConfig = argEnvParser.optionalBoolean("dryRun", "DRY_RUN", false)

    val knxGatewayAddressConfig = argEnvParser.requiredString("knxGatewayAddress", "KNX_GATEWAY_ADDRESS")
    val knxGatewayPortConfig = argEnvParser.requiredInt("knxGatewayPort", "KNX_GATEWAY_PORT")

    val timeZoneConfig = argEnvParser.requiredString("timeZone", "TIME_ZONE")
    val locationLatConfig = argEnvParser.requiredDouble("locationLat", "LOCATION_LAT")
    val locationLonConfig = argEnvParser.requiredDouble("locationLon", "LOCATION_LON")
    val morningTimeConfig = argEnvParser.requiredString("morningTime", "MORNING_TIME")
    val eveningTimeConfig = argEnvParser.requiredString("eveningTime", "EVENING_TIME")

    val allJalousieControlGroupAddressConfig = argEnvParser.requiredString("allJalousieControlGroupAddress", "ALL_JALOUSIE_CONTROL_GROUP_ADDRESS")
    val allJalousieExceptBedroomsControlGroupAddressConfig = argEnvParser.requiredString("allJalousieExceptBedroomsControlGroupAddress", "ALL_JALOUSIE_EXCEPT_BEDROOMS_CONTROL_GROUP_ADDRESS")
    val dayNightModeControlGroupAddressConfig = argEnvParser.requiredString("dayNightModeControlGroupAddress", "DAY_NIGHT_MODE_CONTROL_GROUP_ADDRESS")
    val heatingModeControlGroupAddressConfig = argEnvParser.requiredString("heatingModeControlGroupAddress", "HEATING_MODE_CONTROL_GROUP_ADDRESS")

    val sunriseEarliestConfig = argEnvParser.optionalString("sunriseEarliest", "SUNRISE_EARLIEST", null)
    val sunriseLatestConfig = argEnvParser.optionalString("sunriseLatest", "SUNRISE_LATEST", null)
    val sunsetEarliestConfig = argEnvParser.optionalString("sunsetEarliest", "SUNSET_EARLIEST", null)
    val sunsetLatestConfig = argEnvParser.optionalString("sunsetLatest", "SUNSET_LATEST", null)

    val offsetBeforeSunriseInSecondsConfig = argEnvParser.optionalLong("offsetBeforeSunriseInSeconds", "OFFSET_BEFORE_SUNRISE_IN_SECONDS", 0)
    val offsetAfterSunriseInSecondsConfig = argEnvParser.optionalLong("offsetAfterSunriseInSeconds", "OFFSET_AFTER_SUNRISE_IN_SECONDS", 0)
    val offsetBeforeSunsetInSecondsConfig = argEnvParser.optionalLong("offsetBeforeSunsetInSeconds", "OFFSET_BEFORE_SUNSET_IN_SECONDS", 0)
    val offsetAfterSunsetInSecondsConfig = argEnvParser.optionalLong("offsetAfterSunsetInSeconds", "OFFSET_AFTER_SUNSET_IN_SECONDS", 0)

    argEnvParser.parse()

    val sunriseEarliest = sunriseEarliestConfig.toOptionalString()?.let { LocalTime.parse(it) }
    val sunriseLatest = sunriseLatestConfig.toOptionalString()?.let { LocalTime.parse(it) }
    val sunsetEarliest = sunsetEarliestConfig.toOptionalString()?.let { LocalTime.parse(it) }
    val sunsetLatest = sunsetLatestConfig.toOptionalString()?.let { LocalTime.parse(it) }

    val offsetSunrise = offsetBeforeSunriseInSecondsConfig.toLong() + offsetAfterSunriseInSecondsConfig.toLong()
    val offsetSunset = offsetBeforeSunsetInSecondsConfig.toLong() + offsetAfterSunsetInSecondsConfig.toLong()

    val localAddress = InetSocketAddress(0)
    val gatewayAddress = InetSocketAddress(knxGatewayAddressConfig.toString(), knxGatewayPortConfig.toInt())

    SynchronousEventBus().let { eventBus ->
        val sunriseSunsetEventEmitter = SunriseSunsetEventEmitter(
            eventBus,
            Clock.systemDefaultZone(),
            ZoneId.of(timeZoneConfig.toString()),
            locationLatConfig.toDouble(),
            locationLonConfig.toDouble(),
            sunriseEarliest,
            sunriseLatest,
            sunsetEarliest,
            sunsetLatest,
            offsetSunrise,
            offsetSunset
        )

        val morningEveningEventEmitter = MorningEveningEventEmitter(
            eventBus,
            Clock.systemDefaultZone(),
            ZoneId.of(timeZoneConfig.toString()),
            LocalTime.parse(morningTimeConfig.toString()),
            LocalTime.parse(eveningTimeConfig.toString()),
        )

        if (!dryRunConfig.toBoolean()) {
            KNXNetworkLinkIP.newTunnelingLink(
                localAddress,
                gatewayAddress,
                true,
                TPSettings.TP1
            ).use { knxLink ->
                ProcessCommunicatorImpl(knxLink).use { processCommunicator ->
                    val dayNightModeController = KnxBasedDayNightModeController(
                        processCommunicator,
                        GroupAddress.fromString(dayNightModeControlGroupAddressConfig.toString())
                    )

                    val heatingModeController =
                        KnxBasedHeatingModeController(
                            processCommunicator,
                            GroupAddress.fromString(heatingModeControlGroupAddressConfig.toString())
                        )

                    val jalousieController =
                        KnxBasedJalousieController(
                            processCommunicator,
                            GroupAddress.fromString(allJalousieControlGroupAddressConfig.toString()),
                            GroupAddress.fromString(allJalousieExceptBedroomsControlGroupAddressConfig.toString())
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
