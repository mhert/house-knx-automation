package houseknxautomation.infrastructure.housecontrol.logging

import houseknxautomation.infrastructure.housecontrol.CanControlHeatingMode
import org.slf4j.Logger

class HeatingModeControllerLoggingFacade(
    private val logger: Logger,
    private val heatingModeController: CanControlHeatingMode,
) : CanControlHeatingMode {
    override fun switchToComfortMode() {
        logger.info("" + heatingModeController::class.simpleName + " switchToComfortMode")

        heatingModeController.switchToComfortMode()
    }

    override fun switchToStandbyMode() {
        logger.info("" + heatingModeController::class.simpleName + " switchToStandbyMode")

        heatingModeController.switchToStandbyMode()
    }

    override fun switchToNightMode() {
        logger.info("" + heatingModeController::class.simpleName + " switchToNightMode")

        heatingModeController.switchToNightMode()
    }
}
