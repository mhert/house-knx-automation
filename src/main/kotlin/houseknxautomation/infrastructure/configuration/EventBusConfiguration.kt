package houseknxautomation.infrastructure.configuration

import houseknxautomation.clock.MorningEveningEventEmitter
import houseknxautomation.clock.SunriseSunsetEventEmitter
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventBusConfiguration {
    @Bean
    fun sunriseSunsetEventEmitter(
        config: HouseKnxAutomationConfigProperties,
        eventPublisher: ApplicationEventPublisher,
    ): SunriseSunsetEventEmitter {
        return SunriseSunsetEventEmitter(
            eventPublisher,
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
            config.offsetBeforeSunsetInSeconds.toLong() + config.offsetAfterSunsetInSeconds.toLong(),
        )
    }

    @Bean
    fun morningEveningEventEmitter(
        config: HouseKnxAutomationConfigProperties,
        eventPublisher: ApplicationEventPublisher,
    ): MorningEveningEventEmitter {
        return MorningEveningEventEmitter(
            eventPublisher,
            Clock.systemDefaultZone(),
            ZoneId.of(config.timeZone),
            LocalTime.parse(config.morningTime),
            LocalTime.parse(config.eveningTime),
        )
    }
}
