package houseknxautomation.infrastructure.housecontrol.dryrun

import houseknxautomation.infrastructure.housecontrol.CanControlDayNightMode

class DryRunBasedDayNightModeController() : CanControlDayNightMode {

    override fun switchToDayMode() {}

    override fun switchToNightMode() {}
}
