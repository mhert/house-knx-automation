package houseknxautomation.controllable.knx

import houseknxautomation.controllable.CanControlJalousie
import houseknxautomation.knx.GroupAddress
import io.calimero.process.ProcessCommunicator

open class KnxBasedJalousieController(
    private val processCommunicator: ProcessCommunicator,
    private val allJalousieControlGroupAddress: GroupAddress,
    private val allJalousieExceptBedroomsControlGroupAddress: GroupAddress,
) : CanControlJalousie {
    private enum class JalousieControl(val value: Boolean) {
        UP(false),
        DOWN(true),
    }

    override fun allJalousieUp() {
        processCommunicator.write(
            io.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
            JalousieControl.UP.value,
        )
    }

    override fun allJalousieDown() {
        processCommunicator.write(
            io.calimero.GroupAddress(allJalousieControlGroupAddress.toInt()),
            JalousieControl.DOWN.value,
        )
    }

    override fun allJalousieExceptBedroomsUp() {
        processCommunicator.write(
            io.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
            JalousieControl.UP.value,
        )
    }

    override fun allJalousieExceptBedroomsDown() {
        processCommunicator.write(
            io.calimero.GroupAddress(allJalousieExceptBedroomsControlGroupAddress.toInt()),
            JalousieControl.DOWN.value,
        )
    }
}
