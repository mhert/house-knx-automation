package houseknxautomation.controllable.knx

import houseknxautomation.controllable.CanControlHeatingMode
import houseknxautomation.knx.GroupAddress
import io.calimero.process.ProcessCommunicator

open class KnxBasedHeatingModeController(
    private val processCommunicator: ProcessCommunicator,
    private val heatingModeControlGroupAddress: GroupAddress,
) : CanControlHeatingMode {
    private enum class HvacMode(val value: Byte) {
        COMFORT(0x01),
        STANDBY(0x02),
        NIGHT(0x03),
        FROST_HEAT_PROTECTION(0x04),
    }

    override fun switchToComfortMode() {
        processCommunicator.write(
            io.calimero.GroupAddress(heatingModeControlGroupAddress.toInt()),
            HvacMode.COMFORT.value.toInt(),
            ProcessCommunicator.UNSCALED,
        )
    }

    override fun switchToStandbyMode() {
        processCommunicator.write(
            io.calimero.GroupAddress(heatingModeControlGroupAddress.toInt()),
            HvacMode.STANDBY.value.toInt(),
            ProcessCommunicator.UNSCALED,
        )
    }

    override fun switchToNightMode() {
        processCommunicator.write(
            io.calimero.GroupAddress(heatingModeControlGroupAddress.toInt()),
            HvacMode.NIGHT.value.toInt(),
            ProcessCommunicator.UNSCALED,
        )
    }
}
