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

        // Wait some time and send the command again. Sometimes the command does not reach all devices
        object : Thread() {
            override fun run() {
                super.run()

                sleep(60 * 1000)

                jalousieController.allJalousieDown()
            }
        }.start()
    }
}