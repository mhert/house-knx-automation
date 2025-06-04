package houseknxautomation.clock

import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId
import org.springframework.context.ApplicationEventPublisher

class MorningEveningEventEmitter(
    private val eventPublisher: ApplicationEventPublisher,
    private val clock: Clock,
    private val timeZone: ZoneId,
    private val morningTime: LocalTime,
    private val eveningTime: LocalTime,
) {
    private enum class State {
        UNKNOWN,
        MORNING,
        EVENING,
    }

    private var state = State.UNKNOWN

    fun tick() {
        val now = LocalTime.from(clock.instant().atZone(timeZone))

        if (state != State.MORNING && (now.isAfter(morningTime) && now.isBefore(eveningTime))) {
            eventPublisher.publishEvent(ReachedMorningEvent())
            state = State.MORNING
        }

        if (state != State.EVENING && (now.isBefore(morningTime) || now.isAfter(eveningTime))) {
            eventPublisher.publishEvent(ReachedEveningEvent())
            state = State.EVENING
        }
    }
}
