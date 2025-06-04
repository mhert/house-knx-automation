package houseknxautomation.infrastructure.housecontrol.logging

import houseknxautomation.infrastructure.housecontrol.CanControlJalousie
import org.slf4j.Logger

class JalousieControllerLoggingFacade(
    private val logger: Logger,
    private val jalousieController: CanControlJalousie

    ): CanControlJalousie {
    override fun allJalousieUp() {
        logger.info("" + jalousieController::class.simpleName + " allJalousieUp")

        jalousieController.allJalousieUp()
    }

    override fun allJalousieDown() {
        logger.info("" + jalousieController::class.simpleName + " allJalousieDown")

        jalousieController.allJalousieDown()
    }

    override fun allJalousieExceptBedroomsUp() {
        logger.info("" + jalousieController::class.simpleName + " allJalousieExceptBedroomsUp")

        jalousieController.allJalousieExceptBedroomsUp()
    }

    override fun allJalousieExceptBedroomsDown() {
        logger.info("" + jalousieController::class.simpleName + " allJalousieExceptBedroomsDown")

        jalousieController.allJalousieExceptBedroomsDown()
    }
}