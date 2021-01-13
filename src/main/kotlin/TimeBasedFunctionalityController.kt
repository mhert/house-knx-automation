import knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicator
import java.time.Clock
import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator
import com.luckycatlabs.sunrisesunset.dto.Location
import java.time.Instant
import java.time.ZoneId
import java.util.*

class TimeBasedFunctionalityController(
    private val processCommunicator: ProcessCommunicator,
    private val allJalousieControlGroupAddress: GroupAddress,
    private val allJalousieExceptBedroomsControlGroupAddress: GroupAddress,
    private val dayNightModeControlGroupAddress: GroupAddress,
) {
    enum class State {
        UNKNOWN,
        DAY,
        NIGHT,
    }

    enum class JalousieControl(public val value: Boolean) {
        UP(false),
        DOWN(true),
    }

    enum class DayNight(public val value: Boolean) {
        DAY(true),
        NIGHT(false),
    }

    private var state = State.UNKNOWN

    fun check(
        clock: Clock,
        timeZone: String,
        locationLat: Double,
        locationLon: Double,
        offsetSunrise: Long,
        offsetSunset: Long
    ) {
        val now = clock.instant()
        val sunriseToday = calculateSunrise(locationLat, locationLon, timeZone, now).plusSeconds(offsetSunrise)
        val sunsetToday = calculateSunset(locationLat, locationLon, timeZone, now).plusSeconds(offsetSunset)

        if (now.isAfter(sunriseToday) && now.isBefore(sunsetToday)) {
            if (state == State.DAY) {
                return
            }

            processCommunicator.write(
                tuwien.auto.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
                JalousieControl.UP.value
            )
            processCommunicator.write(
                tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
                DayNight.DAY.value
            )
            state = State.DAY
        }

        if (now.isBefore(sunriseToday) || now.isAfter(sunsetToday)) {
            if (state == State.NIGHT) {
                return
            }

            processCommunicator.write(
                tuwien.auto.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
                JalousieControl.DOWN.value
            )
            processCommunicator.write(
                tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
                DayNight.NIGHT.value
            )
            state = State.NIGHT
        }
    }

    private fun calculateSunrise(
        locationLat: Double,
        locationLon: Double,
        timeZone: String,
        now: Instant
    ): Instant {
        val calculator = SunriseSunsetCalculator(
            Location(locationLat, locationLon),
            timeZone
        )
        val sunrise = calculator.getOfficialSunriseCalendarForDate(
            GregorianCalendar.from(
                now.atZone(ZoneId.systemDefault())
            )
        )
        return sunrise.toInstant()
    }

    private fun calculateSunset(
        locationLat: Double,
        locationLon: Double,
        timeZone: String,
        now: Instant
    ): Instant {
        val calculator = SunriseSunsetCalculator(
            Location(locationLat, locationLon),
            timeZone
        )
        val sunrise = calculator.getOfficialSunsetCalendarForDate(
            GregorianCalendar.from(
                now.atZone(ZoneId.systemDefault())
            )
        )
        return sunrise.toInstant()
    }
}
