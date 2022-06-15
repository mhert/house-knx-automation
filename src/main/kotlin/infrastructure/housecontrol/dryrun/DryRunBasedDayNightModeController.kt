package infrastructure.housecontrol.dryrun

import infrastructure.housecontrol.CanControlDayNightMode

class DryRunBasedDayNightModeController(
): CanControlDayNightMode {

    override fun switchToDayMode() {
        println("DayNightMode:switchToDayMode")
    }

    override fun switchToNightMode() {
        println("DayNightMode:switchToNightMode")
    }
}
