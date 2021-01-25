package domain

import domain.clock.ReachedEveningEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.HeatingModeController

class InTheEveningTurnOnHeatingNightMode(
    private val heatingModeController: HeatingModeController
) : ObjectBasedListener<ReachedEveningEvent> {
    override fun invoke(event: ReachedEveningEvent) {
        heatingModeController.switchToNightMode()
    }
}
