# gRPC SPRING BOOT
Contains 2 apps, client & server.

## Dependency
### Client
- Spring Boot 2
- Sleuth
- Springfox Swagger 3.0.0-SNAPSHOT
- Protobuf Java
- gRPC Spring Boot Starter
- Reactor gRPC Stub (by Salesforce)

### Server
- Spring Boot 2
- Reactive Mongo Repository
- Sleuth
- Protobuf Java
- gRPC Spring Boot Starter
- Reactor gRPC Stub (by Salesforce)

## Component
### Client
- Proto file
- gRPC Client Interceptor
- Controller (for swagger)

### Server
- Proto file
- gRPC Server Interceptor