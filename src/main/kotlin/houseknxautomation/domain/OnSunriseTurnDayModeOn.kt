package houseknxautomation.domain

import houseknxautomation.domain.clock.SunriseEvent
import houseknxautomation.infrastructure.eventbus.ObjectBasedListener
import houseknxautomation.infrastructure.housecontrol.CanControlDayNightMode

class OnSunriseTurnDayModeOn(private val dayNightModeController: CanControlDayNightMode) :
    ObjectBasedListener<SunriseEvent> {

    override fun invoke(event: SunriseEvent) {
        dayNightModeController.switchToDayMode()

        // Wait some time and send the command again. Sometimes the command does not reach all
        // devices
        object : Thread() {
                override fun run() {
                    super.run()

                    sleep(60 * 1000)

                    dayNightModeController.switchToDayMode()
                }
            }
            .start()
    }
}
