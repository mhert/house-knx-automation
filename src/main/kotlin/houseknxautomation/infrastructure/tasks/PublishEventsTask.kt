package houseknxautomation.infrastructure.tasks

import houseknxautomation.clock.MorningEveningEventEmitter
import houseknxautomation.clock.SunriseSunsetEventEmitter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PublishEventsTask(
    private var sunriseSunsetEventEmitter: SunriseSunsetEventEmitter,
    private var morningEveningEventEmitter: MorningEveningEventEmitter,
) {
    @Scheduled(initialDelay = 1000, fixedRate = 1000)
    fun collect() {
        sunriseSunsetEventEmitter.tick()
        morningEveningEventEmitter.tick()
    }
}
