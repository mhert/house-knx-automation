package infrastructure.housecontrol

import infrastructure.knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicationBase
import tuwien.auto.calimero.process.ProcessCommunicator

class HeatingModeController(
    private val processCommunicator: ProcessCommunicator,
    private val dayNightModeControlGroupAddress: GroupAddress
) {
    private enum class HvacMode(public val value: Byte) {
        COMFORT(0x01),
        STANDBY(0x02),
        NIGHT(0x03),
        FROST_HEAT_PROTECTION(0x04),
    }

    fun switchToComfortMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            HvacMode.COMFORT.value.toInt(),
            ProcessCommunicationBase.UNSCALED
        )
    }

    fun switchToStandbyMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            HvacMode.STANDBY.value.toInt(),
            ProcessCommunicationBase.UNSCALED
        )
    }

    fun switchToNightMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            HvacMode.NIGHT.value.toInt(),
            ProcessCommunicationBase.UNSCALED
        )
    }
}