package houseknxautomation.infrastructure.aspects

import houseknxautomation.infrastructure.configuration.HouseKnxAutomationConfigProperties
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class LogAllControls(private val config: HouseKnxAutomationConfigProperties) {
    @Before("execution(* houseknxautomation.controllable..*.*(..))")
    fun logControl(joinPoint: JoinPoint) {
        if (config.printControls == "0") {
            return
        }

        System.getLogger(this::class.java.getName())
            .log(System.Logger.Level.INFO, joinPoint.signature)
    }
}
