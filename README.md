# EDC data plane demo

## Prerequisites
- EDC artifacts published on maven local at commmit `39387f62b8489a8f566ae20727c07f0fb8cf429c`
- Jdk 11
- Docker and Docker compose
- One certificate to sign tokens

### Generate certificate

```
mkdir certs
cd certs
openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem
# Give 123456 as passwork
openssl pkcs12 -inkey key.pem -in cert.pem -export -out cert.pfx
cd ..
```
Then put the cert.pem content into the vault property files

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

Insert the contract agreement id as `contractId` in the [datarequest.json](resources/datarequest.json) file then start transfer:
```
curl -i -X POST -H 'X-Api-Key: 123456' -H 'Content-Type: application/json' --data "@resources/datarequest.json" http://127.0.0.1:8191/api/control/transfer
```

With the ID received the transfer process can be fetched:
```
curl -X GET -H 'X-Api-Key: 123456' "http://localhost:8191/api/transfers/{UUID}"   
```