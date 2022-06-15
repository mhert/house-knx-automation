package infrastructure.housecontrol.knx

import infrastructure.housecontrol.CanControlDayNightMode
import infrastructure.knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicator

class KnxBasedDayNightModeController(
    private val processCommunicator: ProcessCommunicator,
    private val dayNightModeControlGroupAddress: GroupAddress
): CanControlDayNightMode {
    private enum class DayNight(val value: Boolean) {
        DAY(true),
        NIGHT(false),
    }

    override fun switchToDayMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            DayNight.DAY.value
        )
    }

    override fun switchToNightMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            DayNight.NIGHT.value
        )
    }
}