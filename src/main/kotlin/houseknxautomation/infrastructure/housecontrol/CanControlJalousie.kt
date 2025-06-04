package houseknxautomation.infrastructure.housecontrol

interface CanControlJalousie {
    fun allJalousieUp()

    fun allJalousieDown()

    fun allJalousieExceptBedroomsUp()

    fun allJalousieExceptBedroomsDown()
}
