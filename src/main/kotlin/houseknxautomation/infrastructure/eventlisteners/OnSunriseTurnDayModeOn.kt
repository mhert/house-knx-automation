package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.SunriseEvent
import houseknxautomation.controllable.CanControlDayNightMode
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OnSunriseTurnDayModeOn(private val dayNightModeController: CanControlDayNightMode) {
    @EventListener
    fun invoke(event: SunriseEvent) {
        dayNightModeController.switchToDayMode()
    }
}
