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
docker run mhert/house-knx-automation:latest
```

### Executable jar
```shell
java -jar build/libs/house_knx_automation-1.0-SNAPSHOT.jar
```

## Configuration

You can configure it via env vars.

### Required config
| env var                                            | used for                                              |
|----------------------------------------------------|-------------------------------------------------------|
| KNX_GATEWAY_ADDRESS                                | Connection to KNX                                     |
| KNX_GATEWAY_PORT                                   | Connection to KNX                                     |
| TIME_ZONE                                          | Calculation of sunset/sunrise time / Set heating mode |
| LOCATION_LAT                                       | Calculation of sunset/sunrise time                    |
| LOCATION_LON                                       | Calculation of sunset/sunrise time                    |
| MORNING_TIME                                       | Set heating mode                                      |
| EVENING_TIME                                       | Set heating mode                                      |
| ALL_JALOUSIE_CONTROL_GROUP_ADDRESS                 | Move up/down jalousie automatically                   |
| ALL_JALOUSIE_EXCEPT_BEDROOMS_CONTROL_GROUP_ADDRESS | Move up/down jalousie automatically                   |
| DAY_NIGHT_MODE_CONTROL_GROUP_ADDRESS               | Switch day-night-mode automatically                   |
| HEATING_MODE_CONTROL_GROUP_ADDRESS                 | Switch heating comfort-night-mode automatically       |

### Optional config
| env var                          | used for                                             |
|----------------------------------|------------------------------------------------------|
| DRY_RUN                          | If set, no events will be emitted                    |
| OFFSET_BEFORE_SUNRISE_IN_SECONDS | Will be subtracted from sunrise time                 |
| OFFSET_AFTER_SUNRISE_IN_SECONDS  | Will be added to sunrise time                        |
| OFFSET_BEFORE_SUNSET_IN_SECONDS  | Will be subtracted from sunset time                  |
| OFFSET_AFTER_SUNSET_IN_SECONDS   | Will be subtracted from sunset time                  |
| SUNRISE_EARLIEST                 | Set earliest time, sunrise actions will be triggered |
| SUNRISE_LATEST                   | Set latest time, sunrise actions will be triggered   |
| SUNSET_EARLIEST                  | Set earliest time, sunset action will be triggered   |
| SUNSET_LATEST                    | Set latest time, sunset action will be triggered     |
