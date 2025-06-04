package houseknxautomation.domain

import houseknxautomation.domain.clock.SunsetEvent
import houseknxautomation.infrastructure.eventbus.ObjectBasedListener
import houseknxautomation.infrastructure.housecontrol.CanControlDayNightMode

class OnSunsetTurnNightModeOn(private val dayNightModeController: CanControlDayNightMode) :
    ObjectBasedListener<SunsetEvent> {

    override fun invoke(event: SunsetEvent) {
        dayNightModeController.switchToNightMode()

        // Wait some time and send the command again. Sometimes the command does not reach all
        // devices
        object : Thread() {
                override fun run() {
                    super.run()

                    sleep(60 * 1000)

                    dayNightModeController.switchToNightMode()
                }
            }
            .start()
    }
}
