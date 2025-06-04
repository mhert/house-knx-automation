package houseknxautomation.infrastructure.housecontrol.dryrun

import houseknxautomation.infrastructure.housecontrol.CanControlHeatingMode

class DryRunBasedHeatingModeController() : CanControlHeatingMode {
    override fun switchToComfortMode() {}

    override fun switchToStandbyMode() {}

    override fun switchToNightMode() {}
}
