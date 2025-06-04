package houseknxautomation.domain.clock

import houseknxautomation.infrastructure.eventbus.EventBus
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class SunriseSunsetEventEmitterTest {
    @Test
    fun `test sunriseEvent will be emitted on sunrise`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = null
        val sunsetEarliest = null
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T07:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(1)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will be emitted on before sunrise`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = null
        val sunsetEarliest = null
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T04:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(1)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will be emitted on sunset`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = null
        val sunsetEarliest = null
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T22:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(1)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunriseEvent will not be emitted on sunrise before sunriseTimeEarliest`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = LocalTime.parse("08:00:00")
        val sunriseTimeLatest = null
        val sunsetEarliest = null
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T07:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunriseEvent will be emitted on sunrise after sunriseTimeEarliest`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = LocalTime.parse("08:00:00")
        val sunriseTimeLatest = null
        val sunsetEarliest = null
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T09:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(1)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunriseEvent will be emitted after sunriseTimeLatest`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = LocalTime.parse("04:30:00")
        val sunsetEarliest = null
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T05:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(1)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will not be emitted on sunset before sunsetEarliest`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = null
        val sunsetEarliest = LocalTime.parse("23:00:00")
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T22:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will be emitted on sunset after sunsetEarliest`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = null
        val sunsetEarliest = LocalTime.parse("23:00:00")
        val sunsetLatest = null
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T23:30:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(1)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will be emitted after sunsetLatest`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = null
        val sunriseTimeLatest = null
        val sunsetEarliest = null
        val sunsetLatest = LocalTime.parse("20:00:00")
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T20:15:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(1)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunriseEvent will not be triggerd when all parameters are set`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = LocalTime.parse("07:00:00")
        val sunriseTimeLatest = LocalTime.parse("08:00:00")
        val sunsetEarliest = LocalTime.parse("17:00:00")
        val sunsetLatest = LocalTime.parse("21:00:00")
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T06:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunriseEvent will be triggerd when all parameters are set`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = LocalTime.parse("07:00:00")
        val sunriseTimeLatest = LocalTime.parse("08:00:00")
        val sunsetEarliest = LocalTime.parse("17:00:00")
        val sunsetLatest = LocalTime.parse("21:00:00")
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T07:30:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(1)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will not be triggerd when all parameters are set`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = LocalTime.parse("07:00:00")
        val sunriseTimeLatest = LocalTime.parse("08:00:00")
        val sunsetEarliest = LocalTime.parse("17:00:00")
        val sunsetLatest = LocalTime.parse("21:00:00")
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T16:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(1)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(0)).dispatch(refEq(SunsetEvent()))
    }

    @Test
    fun `test sunsetEvent will be triggerd when all parameters are set`() {
        val timeZone = ZoneId.of("Europe/Berlin")
        val locationLat = 49.008180334335925
        val locationLon = 10.848553940809218
        val sunriseTimeEarliest = LocalTime.parse("07:00:00")
        val sunriseTimeLatest = LocalTime.parse("08:00:00")
        val sunsetEarliest = LocalTime.parse("17:00:00")
        val sunsetLatest = LocalTime.parse("21:00:00")
        val offsetSunrise = 0
        val offsetSunset = 0

        val now =
            LocalDateTime.parse("2022-06-01T21:30:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .atZone(timeZone)
                .toInstant()

        val eventBus = mock<EventBus>()
        val clock = mock<Clock> { on { instant() } doReturn (now) }

        val sunriseSunsetEventEmitter =
            SunriseSunsetEventEmitter(
                eventBus,
                clock,
                timeZone,
                locationLat,
                locationLon,
                sunriseTimeEarliest,
                sunriseTimeLatest,
                sunsetEarliest,
                sunsetLatest,
                offsetSunrise.toLong(),
                offsetSunset.toLong(),
            )

        sunriseSunsetEventEmitter.tick()

        verify(eventBus, times(0)).dispatch(refEq(SunriseEvent()))
        verify(eventBus, times(1)).dispatch(refEq(SunsetEvent()))
    }
}
