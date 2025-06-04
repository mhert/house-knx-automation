package houseknxautomation.domain

import houseknxautomation.domain.clock.SunriseEvent
import houseknxautomation.infrastructure.eventbus.ObjectBasedListener
import houseknxautomation.infrastructure.housecontrol.CanControlJalousie

class OnSunriseMoveJalousieUp(
    private val jalousieController: CanControlJalousie
) : ObjectBasedListener<SunriseEvent> {

    override fun invoke(event: SunriseEvent) {
        jalousieController.allJalousieExceptBedroomsUp()

        // Wait some time and send the command again. Sometimes the command does not reach all devices
        object : Thread() {
            override fun run() {
                super.run()

                sleep(60 * 1000)

                jalousieController.allJalousieExceptBedroomsUp()
            }
        }.start()
    }
}