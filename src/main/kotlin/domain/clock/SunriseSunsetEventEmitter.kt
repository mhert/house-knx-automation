package domain.clock

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator
import com.luckycatlabs.sunrisesunset.dto.Location
import infrastructure.eventbus.EventBus
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*

class SunriseSunsetEventEmitter(
    private val eventBus: EventBus,
    private val clock: Clock,
    private val timeZone: ZoneId,
    private val locationLat: Double,
    private val locationLon: Double,
    private val offsetSunrise: Long,
    private val offsetSunset: Long,
) {
    private enum class State {
        UNKNOWN,
        DAY,
        NIGHT,
    }

    private var state = State.UNKNOWN

    fun tick() {
        val now = clock.instant()
        val sunriseToday = calculateSunrise(locationLat, locationLon, now).plusSeconds(offsetSunrise)
        val sunsetToday = calculateSunset(locationLat, locationLon, now).plusSeconds(offsetSunset)

        if (state != State.DAY && (now.isAfter(sunriseToday) && now.isBefore(sunsetToday))) {
            eventBus.dispatch(SunriseEvent())
            state = State.DAY
        }

        if (state != State.NIGHT && (now.isBefore(sunriseToday) || now.isAfter(sunsetToday))) {
            eventBus.dispatch(SunsetEvent())
            state = State.NIGHT
        }
    }

    private fun calculateSunrise(
        locationLat: Double,
        locationLon: Double,
        now: Instant
    ): Instant {
        val calculator = SunriseSunsetCalculator(
            Location(locationLat, locationLon),
            timeZone.toString()
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
        now: Instant
    ): Instant {
        val calculator = SunriseSunsetCalculator(
            Location(locationLat, locationLon),
            timeZone.toString()
        )
        val sunrise = calculator.getOfficialSunsetCalendarForDate(
            GregorianCalendar.from(
                now.atZone(ZoneId.systemDefault())
            )
        )
        return sunrise.toInstant()
    }
}