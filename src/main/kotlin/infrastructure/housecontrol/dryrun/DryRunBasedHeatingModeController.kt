package infrastructure.housecontrol.dryrun

import infrastructure.housecontrol.CanControlHeatingMode

class DryRunBasedHeatingModeController(
): CanControlHeatingMode {
    override fun switchToComfortMode() {
    }

    override fun switchToStandbyMode() {
    }

    override fun switchToNightMode() {
    }
}