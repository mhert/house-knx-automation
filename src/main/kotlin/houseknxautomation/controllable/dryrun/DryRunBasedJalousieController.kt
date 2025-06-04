package houseknxautomation.controllable.dryrun

import houseknxautomation.controllable.CanControlJalousie

open class DryRunBasedJalousieController() : CanControlJalousie {
    override fun allJalousieUp() {}

    override fun allJalousieDown() {}

    override fun allJalousieExceptBedroomsUp() {}

    override fun allJalousieExceptBedroomsDown() {}
}
