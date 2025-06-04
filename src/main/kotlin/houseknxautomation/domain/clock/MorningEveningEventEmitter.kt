package houseknxautomation.domain.clock

import houseknxautomation.infrastructure.eventbus.EventBus
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

class MorningEveningEventEmitter(
    private val eventBus: EventBus,
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
            eventBus.dispatch(ReachedMorningEvent())
            state = State.MORNING
        }

        if (state != State.EVENING && (now.isBefore(morningTime) || now.isAfter(eveningTime))) {
            eventBus.dispatch(ReachedEveningEvent())
            state = State.EVENING
        }
    }
}
