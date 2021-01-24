package domain

import domain.clock.SunriseEvent
import domain.clock.SunsetEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.DayNightModeController
import infrastructure.housecontrol.JalousieController

class OnSunsetMoveJalousieDown(
    private val jalousieController: JalousieController
) : ObjectBasedListener<SunsetEvent> {

    override fun invoke(event: SunsetEvent) {
        jalousieController.allJalousieDown()
    }
}