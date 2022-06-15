package domain

import domain.clock.SunsetEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.CanControlDayNightMode

class OnSunsetTurnNightModeOn(
    private val dayNightModeController: CanControlDayNightMode
) : ObjectBasedListener<SunsetEvent> {

    override fun invoke(event: SunsetEvent) {
        dayNightModeController.switchToNightMode()

        // Wait some time and send the command again. Sometimes the command does not reach all devices
        object : Thread() {
            override fun run() {
                super.run()

                sleep(60 * 1000)

                dayNightModeController.switchToNightMode()
            }
        }.start()
    }
}