# Catena-x Digital Twin demo

## Run control plane
```
./gradlew clean build && docker build launchers/control-plane -t control-plane-demo && docker run --rm control-plane-demo
```