package infrastructure.housecontrol.knx

import infrastructure.housecontrol.CanControlJalousie
import infrastructure.knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicator

class KnxBasedJalousieController(
    private val processCommunicator: ProcessCommunicator,
    private val allJalousieControlGroupAddress: GroupAddress,
    private val allJalousieExceptBedroomsControlGroupAddress: GroupAddress
): CanControlJalousie {
    private enum class JalousieControl(val value: Boolean) {
        UP(false),
        DOWN(true),
    }

    override fun allJalousieUp() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
            JalousieControl.UP.value
        )
    }

    override fun allJalousieDown() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
            JalousieControl.DOWN.value
        )
    }

    override fun allJalousieExceptBedroomsUp() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
            JalousieControl.UP.value
        )
    }

    override fun allJalousieExceptBedroomsDown() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
            JalousieControl.DOWN.value
        )
    }
}