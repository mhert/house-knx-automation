package infrastructure.housecontrol.dryrun

import infrastructure.housecontrol.CanControlJalousie

class DryRunBasedJalousieController(
): CanControlJalousie {
    override fun allJalousieUp() {
        println("Jalousie:allJalousieUp")
    }

    override fun allJalousieDown() {
        println("Jalousie:allJalousieDown")
    }

    override fun allJalousieExceptBedroomsUp() {
        println("Jalousie:allJalousieExceptBedroomsUp")
    }

    override fun allJalousieExceptBedroomsDown() {
        println("Jalousie:allJalousieExceptBedroomsDown")
    }
}
