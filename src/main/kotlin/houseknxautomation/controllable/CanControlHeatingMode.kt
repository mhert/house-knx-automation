package houseknxautomation.controllable

interface CanControlHeatingMode {
    fun switchToComfortMode()

    fun switchToStandbyMode()

    fun switchToNightMode()
}
