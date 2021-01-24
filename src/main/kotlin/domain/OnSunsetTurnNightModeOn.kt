package domain

import domain.clock.SunsetEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.DayNightModeController

class OnSunsetTurnNightModeOn(
    private val dayNightModeController: DayNightModeController
) : ObjectBasedListener<SunsetEvent> {

    override fun invoke(event: SunsetEvent) {
        dayNightModeController.switchToNightMode()
    }
}