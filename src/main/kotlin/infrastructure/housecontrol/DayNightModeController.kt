package infrastructure.housecontrol

import infrastructure.knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicator

class DayNightModeController(
    private val processCommunicator: ProcessCommunicator,
    private val dayNightModeControlGroupAddress: GroupAddress
) {
    private enum class DayNight(public val value: Boolean) {
        DAY(true),
        NIGHT(false),
    }

    fun switchToDayMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            DayNight.DAY.value
        )
    }

    fun switchToNightMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            DayNight.NIGHT.value
        )
    }
}