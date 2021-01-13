import infrastructure.ArgOrEnvParser
import knx.GroupAddress
import tuwien.auto.calimero.link.KNXNetworkLinkIP
import tuwien.auto.calimero.link.medium.TPSettings
import tuwien.auto.calimero.process.ProcessCommunicatorImpl
import java.net.InetSocketAddress
import java.time.Clock
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val argEnvParser = ArgOrEnvParser("house-knx-automation", args, System.getenv())

    val knxGatewayAddress = argEnvParser.requiredString("knxGatewayAddress", "KNX_GATEWAY_ADDRESS")
    val knxGatewayPort = argEnvParser.requiredInt("knxGatewayPort", "KNX_GATEWAY_PORT")

    val timeZone = argEnvParser.requiredString("timeZone", "TIME_ZONE")
    val locationLat = argEnvParser.requiredDouble("locationLat", "LOCATION_LAT")
    val locationLon = argEnvParser.requiredDouble("locationLon", "LOCATION_LON")

    val allJalousieControlGroupAddress = argEnvParser.requiredString("allJalousieControlGroupAddress", "ALL_JALOUSIE_CONTROL_GROUP_ADDRESS")
    val allJalousieExceptBedroomsControlGroupAddress = argEnvParser.requiredString("allJalousieExceptBedroomsControlGroupAddress", "ALL_JALOUSIE_EXCEPT_BEDROOMS_CONTROL_GROUP_ADDRESS")
    val dayNightModeControlGroupAddress = argEnvParser.requiredString("dayNightModeControlGroupAddress", "DAY_NIGHT_MODE_CONTROL_GROUP_ADDRESS")

    val offsetBeforeSunriseInSeconds = argEnvParser.optionalLong("offsetBeforeSunriseInSeconds", "OFFSET_BEFORE_SUNRISE_IN_SECONDS", 0)
    val offsetAfterSunriseInSeconds = argEnvParser.optionalLong("offsetAfterSunriseInSeconds", "OFFSET_AFTER_SUNRISE_IN_SECONDS", 0)
    val offsetBeforeSunsetInSeconds = argEnvParser.optionalLong("offsetBeforeSunsetInSeconds", "OFFSET_BEFORE_SUNSET_IN_SECONDS", 0)
    val offsetAfterSunsetInSeconds = argEnvParser.optionalLong("offsetAfterSunsetInSeconds", "OFFSET_AFTER_SUNSET_IN_SECONDS", 0)

    val offsetSunrise = offsetBeforeSunriseInSeconds.toLong() + offsetAfterSunriseInSeconds.toLong()
    val offsetSunset = offsetBeforeSunsetInSeconds.toLong() + offsetAfterSunsetInSeconds.toLong()

    argEnvParser.parse()

    try {
        val localAddress = InetSocketAddress(0)
        val gatewayAddress = InetSocketAddress(knxGatewayAddress.toString(), knxGatewayPort.toInt())

        KNXNetworkLinkIP.newTunnelingLink(
            localAddress,
            gatewayAddress,
            true,
            TPSettings.TP1
        ).use { knxLink ->
            ProcessCommunicatorImpl(knxLink).use { processCommunicator ->
                TimeBasedFunctionalityController(
                    processCommunicator,
                    GroupAddress.fromString(allJalousieControlGroupAddress.toString()),
                    GroupAddress.fromString(allJalousieExceptBedroomsControlGroupAddress.toString()),
                    GroupAddress.fromString(dayNightModeControlGroupAddress.toString()),
                ).let { timeBasedJalousieController ->
                    while (knxLink.isOpen) {
                        timeBasedJalousieController.check(
                            Clock.systemDefaultZone(),
                            timeZone.toString(),
                            locationLat.toDouble(),
                            locationLon.toDouble(),
                            offsetSunrise,
                            offsetSunset
                        )
                        Thread.sleep(1000)
                    }
                }
            }
        }
    } catch (e: Exception) {
        System.err.println(e)
        exitProcess(1)
    }
}
