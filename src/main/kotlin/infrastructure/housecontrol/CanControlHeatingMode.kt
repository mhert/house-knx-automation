package infrastructure.housecontrol

interface CanControlHeatingMode {
    fun switchToComfortMode()

    fun switchToStandbyMode()

    fun switchToNightMode()
}
