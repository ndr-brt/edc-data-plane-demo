# Catena-x Digital Twin demo

## Run
```
./gradlew clean build
docker-compose up --build
```

## Use case

Create asset on provider:
```
curl -i -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/asset.json" http://127.0.0.1:8181/api/assets
```

Create contract definition on provider:
```
curl -i -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/contractdefinition.json" http://127.0.0.1:8181/api/contractdefinitions
```

Initiate negotiation on consumer:
```
curl -i -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/contractoffer.json" http://127.0.0.1:8191/api/control/negotiation
```

Lookup contract agreement id with the negotiation id (UUID) received from the last call:
```
curl -X GET -H 'X-Api-Key: 123456' "http://localhost:8191/api/control/negotiation/{UUID}/state"
```