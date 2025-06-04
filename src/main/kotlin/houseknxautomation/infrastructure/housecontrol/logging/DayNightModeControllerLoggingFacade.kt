package houseknxautomation.infrastructure.housecontrol.logging

import houseknxautomation.infrastructure.housecontrol.CanControlDayNightMode
import org.slf4j.Logger

class DayNightModeControllerLoggingFacade(
    private val logger: Logger,
    private val dayNightModeController: CanControlDayNightMode

    ): CanControlDayNightMode {
    override fun switchToDayMode() {
        logger.info("" + dayNightModeController::class.simpleName + " switchToDayMode")

        dayNightModeController.switchToDayMode()
    }

    override fun switchToNightMode() {
        logger.info("" + dayNightModeController::class.simpleName + " switchToNightMode")

        dayNightModeController.switchToNightMode()
    }
}