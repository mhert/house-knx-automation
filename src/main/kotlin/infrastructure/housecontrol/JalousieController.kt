package infrastructure.housecontrol

import knx.GroupAddress
import tuwien.auto.calimero.process.ProcessCommunicator

class JalousieController(
    private val processCommunicator: ProcessCommunicator,
    private val allJalousieControlGroupAddress: GroupAddress,
    private val allJalousieExceptBedroomsControlGroupAddress: GroupAddress
) {
    enum class JalousieControl(public val value: Boolean) {
        UP(false),
        DOWN(true),
    }

    fun allJalousieUp() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
            JalousieControl.UP.value
        )
    }

    fun allJalousieDown() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
            JalousieControl.DOWN.value
        )
    }

    fun allJalousieExceptBedroomsUp() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
            JalousieControl.UP.value
        )
    }

    fun allJalousieExceptBedroomsDown() {
        processCommunicator.write(
            tuwien.auto.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
            JalousieControl.DOWN.value
        )
    }
}