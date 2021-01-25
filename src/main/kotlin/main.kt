import domain.*
import domain.clock.*
import infrastructure.housecontrol.DayNightModeController
import infrastructure.ArgOrEnvParser
import infrastructure.eventbus.SynchronousEventBus
import infrastructure.housecontrol.HeatingModeController
import infrastructure.housecontrol.JalousieController
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

        KNXNetworkLinkIP.newTunnelingLink(
            localAddress,
            gatewayAddress,
            true,
            TPSettings.TP1
        ).use { knxLink ->
            ProcessCommunicatorImpl(knxLink).use { processCommunicator ->
                val dayNightModeController = DayNightModeController(
                    processCommunicator,
                    GroupAddress.fromString(dayNightModeControlGroupAddress.toString())
                )
                val jalousieController = JalousieController(
                    processCommunicator,
                    GroupAddress.fromString(allJalousieControlGroupAddress.toString()),
                    GroupAddress.fromString(allJalousieExceptBedroomsControlGroupAddress.toString())
                )
                val heatingModeController = HeatingModeController(
                    processCommunicator,
                    GroupAddress.fromString(heatingModeControlGroupAddress.toString())
                )

                eventBus.listen(SunriseEvent::class, OnSunriseTurnDayModeOn(dayNightModeController))
                eventBus.listen(SunriseEvent::class, OnSunriseMoveJalousieUp(jalousieController))
                eventBus.listen(SunsetEvent::class, OnSunsetTurnNightModeOn(dayNightModeController))
                eventBus.listen(SunsetEvent::class, OnSunsetMoveJalousieDown(jalousieController))
                eventBus.listen(ReachedMorningEvent::class, InTheMorningTurnOnHeatingComfortMode(heatingModeController))
                eventBus.listen(ReachedEveningEvent::class, InTheEveningTurnOnHeatingNightMode(heatingModeController))

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

                while (knxLink.isOpen) {
                    sunriseSunsetEventEmitter.tick()
                    morningEveningEventEmitter.tick()
                    Thread.sleep(1000)
                }
            }
        }
    }
}
