package domain

import domain.clock.ReachedMorningEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.HeatingModeController

class InTheMorningTurnOnHeatingComfortMode(
    private val heatingModeController: HeatingModeController
) : ObjectBasedListener<ReachedMorningEvent> {
    override fun invoke(event: ReachedMorningEvent) {
        heatingModeController.switchToComfortMode()
    }
}
