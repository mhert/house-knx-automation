package houseknxautomation.infrastructure.aspects

import houseknxautomation.infrastructure.configuration.HouseKnxAutomationConfigProperties
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class LogAllEvents(private val config: HouseKnxAutomationConfigProperties) {
    @Before("execution(@org.springframework.context.event.EventListener * *(..))")
    fun logEvent(joinPoint: JoinPoint) {
        if (config.printEvents == "0") {
            return
        }

        System.getLogger(this::class.java.getName())
            .log(System.Logger.Level.INFO, joinPoint.args[0])
    }
}
