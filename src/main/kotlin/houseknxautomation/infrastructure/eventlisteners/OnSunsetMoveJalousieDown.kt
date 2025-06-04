package houseknxautomation.infrastructure.eventlisteners

import houseknxautomation.clock.SunsetEvent
import houseknxautomation.controllable.CanControlJalousie
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class OnSunsetMoveJalousieDown(private val jalousieController: CanControlJalousie) {
    @EventListener
    fun invoke(event: SunsetEvent) {
        jalousieController.allJalousieDown()

        // Wait some time and send the command again. Sometimes the command does not reach all
        // devices
        object : Thread() {
                override fun run() {
                    super.run()

                    sleep(60 * 1000)

                    jalousieController.allJalousieDown()
                }
            }
            .start()
    }
}
