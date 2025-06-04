package houseknxautomation.controllable.dryrun

import houseknxautomation.controllable.CanControlHeatingMode

open class DryRunBasedHeatingModeController() : CanControlHeatingMode {
    override fun switchToComfortMode() {}

    override fun switchToStandbyMode() {}

    override fun switchToNightMode() {}
}
