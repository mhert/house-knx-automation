package houseknxautomation.domain

import houseknxautomation.domain.clock.ReachedEveningEvent
import houseknxautomation.infrastructure.eventbus.ObjectBasedListener
import houseknxautomation.infrastructure.housecontrol.CanControlHeatingMode

class InTheEveningTurnOnHeatingNightMode(private val heatingModeController: CanControlHeatingMode) :
    ObjectBasedListener<ReachedEveningEvent> {
    override fun invoke(event: ReachedEveningEvent) {
        heatingModeController.switchToNightMode()
    }
}
