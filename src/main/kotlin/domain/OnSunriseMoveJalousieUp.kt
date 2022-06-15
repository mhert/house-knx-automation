package domain

import domain.clock.SunriseEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.CanControlJalousie

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