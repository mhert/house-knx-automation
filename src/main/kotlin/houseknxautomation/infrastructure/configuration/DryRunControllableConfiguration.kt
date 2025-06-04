package houseknxautomation.infrastructure.configuration

import houseknxautomation.controllable.CanControlDayNightMode
import houseknxautomation.controllable.CanControlHeatingMode
import houseknxautomation.controllable.CanControlJalousie
import houseknxautomation.controllable.dryrun.DryRunBasedDayNightModeController
import houseknxautomation.controllable.dryrun.DryRunBasedHeatingModeController
import houseknxautomation.controllable.dryrun.DryRunBasedJalousieController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dry-run")
class DryRunControllableConfiguration {
    @Bean
    fun canControlDayNightMode(): CanControlDayNightMode {
        return DryRunBasedDayNightModeController()
    }

    @Bean
    fun canControlHeatingMode(): CanControlHeatingMode {
        return DryRunBasedHeatingModeController()
    }

    @Bean
    fun canControlJalousie(): CanControlJalousie {
        return DryRunBasedJalousieController()
    }
}
