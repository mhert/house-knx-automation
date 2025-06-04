package houseknxautomation.domain

import houseknxautomation.domain.clock.ReachedMorningEvent
import houseknxautomation.infrastructure.eventbus.ObjectBasedListener
import houseknxautomation.infrastructure.housecontrol.CanControlHeatingMode

class InTheMorningTurnOnHeatingComfortMode(
    private val heatingModeController: CanControlHeatingMode
) : ObjectBasedListener<ReachedMorningEvent> {
    override fun invoke(event: ReachedMorningEvent) {
        heatingModeController.switchToComfortMode()
    }
}
