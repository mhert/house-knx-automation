package houseknxautomation.domain.clock

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator
import com.luckycatlabs.sunrisesunset.dto.Location
import houseknxautomation.infrastructure.eventbus.EventBus
import java.time.*
import java.util.*

class SunriseSunsetEventEmitter(
    private val eventBus: EventBus,
    private val clock: Clock,
    private val timeZone: ZoneId,
    private val locationLat: Double,
    private val locationLon: Double,
    private val sunriseTimeEarliest: LocalTime?,
    private val sunriseTimeLatest: LocalTime?,
    private val sunsetEarliest: LocalTime?,
    private val sunsetLatest: LocalTime?,
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
        val nowWithDate = LocalDateTime.from(clock.instant().atZone(timeZone))
        val now = nowWithDate.toLocalTime()

        val sunriseTimeForToday =
            calculateSunrise(locationLat, locationLon, nowWithDate.atZone(timeZone))
                .plusSeconds(offsetSunrise)
        val sunsetTimeForToday =
            calculateSunset(locationLat, locationLon, nowWithDate.atZone(timeZone))
                .plusSeconds(offsetSunset)

        val nowIsBeforeEarliestSunriseTime =
            sunriseTimeEarliest?.let { now.isBefore(sunriseTimeEarliest) } ?: false
        val nowIsAfterLatestSunriseTime =
            sunriseTimeLatest?.let { now.isAfter(sunriseTimeLatest) } ?: false

        val nowIsBeforeEarliestSunsetTime =
            sunsetEarliest?.let { now.isBefore(sunsetEarliest) } ?: false
        val nowIsAfterLatestSunsetTime = sunsetLatest?.let { now.isAfter(sunsetLatest) } ?: false
        val nowIsBeforeLatestSunsetTime = sunsetLatest?.let { now.isBefore(sunsetLatest) } ?: true

        val nowIsAfterSunriseTime = now.isAfter(sunriseTimeForToday)
        val nowIsBeforeSunriseTime = !nowIsAfterSunriseTime

        val nowIsBeforeSunsetTime = now.isBefore(sunsetTimeForToday)
        val nowIsAfterSunsetTime = !nowIsBeforeSunsetTime

        if (state !== State.DAY) {
            if (nowIsBeforeSunsetTime && nowIsBeforeLatestSunsetTime) {
                if (!nowIsBeforeEarliestSunriseTime && nowIsAfterSunriseTime) {
                    eventBus.dispatch(SunriseEvent())
                    state = State.DAY

                    return
                }

                if (nowIsAfterLatestSunriseTime) {
                    eventBus.dispatch(SunriseEvent())
                    state = State.DAY

                    return
                }
            }
        }

        if (state !== State.NIGHT) {
            if (nowIsBeforeSunriseTime) {
                eventBus.dispatch(SunsetEvent())
                state = State.NIGHT

                return
            }
            if (nowIsAfterSunriseTime) {
                if (!nowIsBeforeEarliestSunsetTime && nowIsAfterSunsetTime) {
                    eventBus.dispatch(SunsetEvent())
                    state = State.NIGHT

                    return
                }

                if (nowIsAfterLatestSunsetTime) {
                    eventBus.dispatch(SunsetEvent())
                    state = State.NIGHT

                    return
                }
            }
        }
    }

    private fun calculateSunrise(
        locationLat: Double,
        locationLon: Double,
        now: ZonedDateTime,
    ): LocalTime {
        val calculator =
            SunriseSunsetCalculator(
                Location(locationLat, locationLon),
                TimeZone.getTimeZone(now.zone),
            )
        val sunrise = calculator.getOfficialSunriseCalendarForDate(GregorianCalendar.from(now))
        return LocalDateTime.ofInstant(sunrise.toInstant(), now.zone).toLocalTime()
    }

    private fun calculateSunset(
        locationLat: Double,
        locationLon: Double,
        now: ZonedDateTime,
    ): LocalTime {
        val calculator =
            SunriseSunsetCalculator(
                Location(locationLat, locationLon),
                TimeZone.getTimeZone(now.zone),
            )
        val sunrise = calculator.getOfficialSunsetCalendarForDate(GregorianCalendar.from(now))
        return LocalDateTime.ofInstant(sunrise.toInstant(), now.zone).toLocalTime()
    }
}
