package infrastructure.housecontrol.dryrun

import infrastructure.housecontrol.CanControlJalousie

class DryRunBasedJalousieController(
): CanControlJalousie {
    override fun allJalousieUp() {
    }

    override fun allJalousieDown() {
    }

    override fun allJalousieExceptBedroomsUp() {
    }

    override fun allJalousieExceptBedroomsDown() {
    }
}
