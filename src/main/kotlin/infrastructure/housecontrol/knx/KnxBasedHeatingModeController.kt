package infrastructure.housecontrol.knx

import infrastructure.housecontrol.CanControlHeatingMode
import infrastructure.knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicator

class KnxBasedHeatingModeController(
    private val processCommunicator: ProcessCommunicator,
    private val dayNightModeControlGroupAddress: GroupAddress
): CanControlHeatingMode {
    private enum class HvacMode(val value: Byte) {
        COMFORT(0x01),
        STANDBY(0x02),
        NIGHT(0x03),
        FROST_HEAT_PROTECTION(0x04),
    }

    override fun switchToComfortMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            HvacMode.COMFORT.value.toInt(),
            ProcessCommunicator.UNSCALED
        )
    }

    override fun switchToStandbyMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            HvacMode.STANDBY.value.toInt(),
            ProcessCommunicator.UNSCALED
        )
    }

    override fun switchToNightMode() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(dayNightModeControlGroupAddress.toInt()),
            HvacMode.NIGHT.value.toInt(),
            ProcessCommunicator.UNSCALED
        )
    }
}