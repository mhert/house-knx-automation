package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.ReachedMorningEvent
import houseknxautomation.controllable.CanControlHeatingMode
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class InTheMorningTurnOnHeatingComfortMode(
    private val heatingModeController: CanControlHeatingMode
) {
    @EventListener
    fun invoke(event: ReachedMorningEvent) {
        heatingModeController.switchToComfortMode()
    }
}
