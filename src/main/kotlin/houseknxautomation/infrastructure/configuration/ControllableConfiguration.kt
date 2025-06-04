package houseknxautomation.infrastructure.configuration

import houseknxautomation.controllable.CanControlDayNightMode
import houseknxautomation.controllable.CanControlHeatingMode
import houseknxautomation.controllable.CanControlJalousie
import houseknxautomation.controllable.knx.KnxBasedDayNightModeController
import houseknxautomation.controllable.knx.KnxBasedHeatingModeController
import houseknxautomation.controllable.knx.KnxBasedJalousieController
import houseknxautomation.knx.GroupAddress
import io.calimero.process.ProcessCommunicatorImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("default")
class ControllableConfiguration {
    @Bean
    fun canControlDayNightMode(
        config: HouseKnxAutomationConfigProperties,
        processCommunicator: ProcessCommunicatorImpl,
    ): CanControlDayNightMode {
        return KnxBasedDayNightModeController(
            processCommunicator,
            GroupAddress.fromString(config.dayNightModeControlGroupAddress),
        )
    }

    @Bean
    fun canControlHeatingMode(
        config: HouseKnxAutomationConfigProperties,
        processCommunicator: ProcessCommunicatorImpl,
    ): CanControlHeatingMode {
        return KnxBasedHeatingModeController(
            processCommunicator,
            GroupAddress.fromString(config.heatingModeControlGroupAddress),
        )
    }

    @Bean
    fun canControlJalousie(
        config: HouseKnxAutomationConfigProperties,
        processCommunicator: ProcessCommunicatorImpl,
    ): CanControlJalousie {
        return KnxBasedJalousieController(
            processCommunicator,
            GroupAddress.fromString(config.allJalousieControlGroupAddress),
            GroupAddress.fromString(config.allJalousieExceptBedroomsControlGroupAddress),
        )
    }
}
