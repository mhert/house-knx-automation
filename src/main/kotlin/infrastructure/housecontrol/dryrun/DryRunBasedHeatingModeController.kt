package infrastructure.housecontrol.dryrun

import infrastructure.housecontrol.CanControlHeatingMode

class DryRunBasedHeatingModeController(
): CanControlHeatingMode {
    override fun switchToComfortMode() {
        println("HeatingMode:switchToComfortMode")
    }

    override fun switchToStandbyMode() {
        println("HeatingMode:switchToStandbyMode")
    }

    override fun switchToNightMode() {
        println("HeatingMode:switchToNightMode")
    }
}