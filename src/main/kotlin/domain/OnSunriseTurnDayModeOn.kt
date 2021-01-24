package domain

import domain.clock.SunriseEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.DayNightModeController

class OnSunriseTurnDayModeOn(
    private val dayNightModeController: DayNightModeController
) : ObjectBasedListener<SunriseEvent> {

    override fun invoke(event: SunriseEvent) {
        dayNightModeController.switchToDayMode()

        // Wait some time and send the command again. Sometimes the command does not reach all devices
        object : Thread() {
            override fun run() {
                super.run()

                sleep(60 * 1000)

                dayNightModeController.switchToDayMode()
            }
        }.start()
    }
}