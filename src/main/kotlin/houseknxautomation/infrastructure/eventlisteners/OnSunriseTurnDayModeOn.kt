package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.SunriseEvent
import houseknxautomation.controllable.CanControlDayNightMode
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OnSunriseTurnDayModeOn(private val dayNightModeController: CanControlDayNightMode) {
    @EventListener
    fun invoke(event: SunriseEvent) {
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
