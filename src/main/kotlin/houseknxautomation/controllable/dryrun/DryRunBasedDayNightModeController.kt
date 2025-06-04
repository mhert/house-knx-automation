package houseknxautomation.controllable.dryrun

import houseknxautomation.controllable.CanControlDayNightMode

open class DryRunBasedDayNightModeController() : CanControlDayNightMode {
    override fun switchToDayMode() {}

    override fun switchToNightMode() {}
}
