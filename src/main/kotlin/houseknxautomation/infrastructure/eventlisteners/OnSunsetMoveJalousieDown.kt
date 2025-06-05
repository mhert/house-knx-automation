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
    }
}
