package domain

import domain.clock.SunriseEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.DayNightModeController
import infrastructure.housecontrol.JalousieController

class OnSunriseMoveJalousieUp(
    private val jalousieController: JalousieController
) : ObjectBasedListener<SunriseEvent> {

    override fun invoke(event: SunriseEvent) {
        jalousieController.allJalousieExceptBedroomsUp()
    }
}