package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.SunsetEvent
import houseknxautomation.controllable.CanControlDayNightMode
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OnSunsetTurnNightModeOn(private val dayNightModeController: CanControlDayNightMode) {
    @EventListener
    fun invoke(event: SunsetEvent) {
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
