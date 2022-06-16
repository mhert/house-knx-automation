# house-knx-automation

This small kotlin app controls multiple features in our house via KNX. For now, it:
 - Moves the jalousie up and down on sunrise/sunset
 - Switches day-night-mode on sunrise/sunset
 - Switches between heating-modes in the sunrise/sunset

## Build

### Docker image
```shell
docker buildx build --no-cache --progress plain --tag `basename $PWD` --file Dockerfile .
```

### Executable jar
```shell
./gradlew -Djdk.lang.Process.launchMechanism=vfork clean build -i --stacktrace
```

## Usage

### Docker
```shell
docker run ghcr.io/mhert/house-knx-automation:latest
```

### Executable jar
```shell
java -jar build/libs/house_knx_automation-1.0-SNAPSHOT.jar
```

## Configuration

You can control the app via environment variables or arguments.

### Required config
| env var                                            | argument                                       | used for                                              |
|----------------------------------------------------|------------------------------------------------|-------------------------------------------------------|
| KNX_GATEWAY_ADDRESS                                | --knxGatewayAddress                            | Connection to KNX                                     |
| KNX_GATEWAY_PORT                                   | --knxGatewayPort                               | Connection to KNX                                     |
| TIME_ZONE                                          | --timeZone                                     | Calculation of sunset/sunrise time / Set heating mode |
| LOCATION_LAT                                       | --locationLat                                  | Calculation of sunset/sunrise time                    |
| LOCATION_LON                                       | --locationLon                                  | Calculation of sunset/sunrise time                    |
| MORNING_TIME                                       | --morningTime                                  | Set heating mode                                      |
| EVENING_TIME                                       | --eveningTime                                  | Set heating mode                                      |
| ALL_JALOUSIE_CONTROL_GROUP_ADDRESS                 | --allJalousieControlGroupAddress               | Move up/down jalousie automatically                   |
| ALL_JALOUSIE_EXCEPT_BEDROOMS_CONTROL_GROUP_ADDRESS | --allJalousieExceptBedroomsControlGroupAddress | Move up/down jalousie automatically                   |
| DAY_NIGHT_MODE_CONTROL_GROUP_ADDRESS               | --dayNightModeControlGroupAddress              | Switch day-night-mode automatically                   |
| HEATING_MODE_CONTROL_GROUP_ADDRESS                 | --heatingModeControlGroupAddress               | Switch heating comfort-night-mode automatically       |

### Optional config
| env var                          | argument                       | used for                                             |
|----------------------------------|--------------------------------|------------------------------------------------------|
| DRY_RUN                          | --dryRun                       | If set, no events will be emitted                    |
| OFFSET_BEFORE_SUNRISE_IN_SECONDS | --offsetBeforeSunriseInSeconds | Will be subtracted from sunrise time                 |
| OFFSET_AFTER_SUNRISE_IN_SECONDS  | --offsetAfterSunriseInSeconds  | Will be added to sunrise time                        |
| OFFSET_BEFORE_SUNSET_IN_SECONDS  | --offsetBeforeSunsetInSeconds  | Will be subtracted from sunset time                  |
| OFFSET_AFTER_SUNSET_IN_SECONDS   | --offsetAfterSunsetInSeconds   | Will be subtracted from sunset time                  |
| SUNRISE_EARLIEST                 | --sunriseEarliest              | Set earliest time, sunrise actions will be triggered |
| SUNRISE_LATEST                   | --sunriseLatest                | Set latest time, sunrise actions will be triggered   |
| SUNSET_EARLIEST                  | --sunsetEarliest               | Set earliest time, sunset action will be triggered   |
| SUNSET_LATEST                    | --sunsetLatest                 | Set latest time, sunset action will be triggered     |
