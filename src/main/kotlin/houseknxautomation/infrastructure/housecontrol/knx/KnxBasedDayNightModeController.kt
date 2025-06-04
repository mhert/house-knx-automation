package houseknxautomation.infrastructure.housecontrol.knx

import houseknxautomation.infrastructure.housecontrol.CanControlDayNightMode
import houseknxautomation.infrastructure.knx.GroupAddress
import io.calimero.process.ProcessCommunicator

class KnxBasedDayNightModeController(
    private val processCommunicator: ProcessCommunicator,
    private val dayNightModeControlGroupAddress: GroupAddress,
) : CanControlDayNightMode {
    private enum class DayNight(val value: Boolean) {
        DAY(true),
        NIGHT(false),
    }

    override fun switchToDayMode() {
        processCommunicator.write(
            io.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            DayNight.DAY.value,
        )
    }

    override fun switchToNightMode() {
        processCommunicator.write(
            io.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            DayNight.NIGHT.value,
        )
    }
}
