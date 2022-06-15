package domain

import domain.clock.SunsetEvent
import infrastructure.eventbus.ObjectBasedListener
import infrastructure.housecontrol.CanControlJalousie

class OnSunsetMoveJalousieDown(
    private val jalousieController: CanControlJalousie
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