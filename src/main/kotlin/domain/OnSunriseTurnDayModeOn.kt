package domain

import domain.clock.SunriseEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.DayNightModeController

class OnSunriseTurnDayModeOn(
    private val dayNightModeController: DayNightModeController
) : ObjectBasedListener<SunriseEvent> {

    override fun invoke(event: SunriseEvent) {
        dayNightModeController.switchToDayMode()
    }
}