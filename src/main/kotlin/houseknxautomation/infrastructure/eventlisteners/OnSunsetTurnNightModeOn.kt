package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.SunsetEvent
import houseknxautomation.controllable.CanControlDayNightMode
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OnSunsetTurnNightModeOn(private val dayNightModeController: CanControlDayNightMode) {
    @EventListener
    fun invoke(event: SunsetEvent) {
        dayNightModeController.switchToNightMode()
    }
}
