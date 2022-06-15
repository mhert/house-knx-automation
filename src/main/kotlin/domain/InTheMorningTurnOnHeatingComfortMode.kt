package domain

import domain.clock.ReachedMorningEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.CanControlHeatingMode

class InTheMorningTurnOnHeatingComfortMode(
    private val heatingModeController: CanControlHeatingMode
) : ObjectBasedListener<ReachedMorningEvent> {
    override fun invoke(event: ReachedMorningEvent) {
        heatingModeController.switchToComfortMode()
    }
}
