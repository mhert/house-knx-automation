package houseknxautomation.infrastructure.housecontrol.dryrun

import houseknxautomation.infrastructure.housecontrol.CanControlJalousie

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
