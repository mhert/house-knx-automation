package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.ReachedEveningEvent
import houseknxautomation.controllable.CanControlHeatingMode
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InTheEveningTurnOnHeatingNightMode(private val heatingModeController: CanControlHeatingMode) {
    @EventListener
    fun invoke(event: ReachedEveningEvent) {
        heatingModeController.switchToNightMode()
    }
}
