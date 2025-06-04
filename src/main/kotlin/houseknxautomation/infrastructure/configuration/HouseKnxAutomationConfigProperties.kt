package houseknxautomation.infrastructure.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "house-knx-automation")
class HouseKnxAutomationConfigProperties(
    var printEvents: String,
    var printControls: String,
    var knxGatewayAddress: String,
    var knxGatewayPort: String,
    var timeZone: String,
    var locationLat: String,
    var locationLon: String,
    var morningTime: String,
    var eveningTime: String,
    var allJalousieControlGroupAddress: String,
    var allJalousieExceptBedroomsControlGroupAddress: String,
    var dayNightModeControlGroupAddress: String,
    var heatingModeControlGroupAddress: String,
    var sunriseEarliest: String?,
    var sunriseLatest: String?,
    var sunsetEarliest: String?,
    var sunsetLatest: String?,
    var offsetBeforeSunriseInSeconds: String,
    var offsetAfterSunriseInSeconds: String,
    var offsetBeforeSunsetInSeconds: String,
    var offsetAfterSunsetInSeconds: String,
)
