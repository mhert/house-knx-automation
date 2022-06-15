package domain

import domain.clock.ReachedEveningEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.CanControlHeatingMode

class InTheEveningTurnOnHeatingNightMode(
    private val heatingModeController: CanControlHeatingMode
) : ObjectBasedListener<ReachedEveningEvent> {
    override fun invoke(event: ReachedEveningEvent) {
        heatingModeController.switchToNightMode()
    }
}
