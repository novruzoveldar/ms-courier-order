# ms-courier-order

---

## Table of Contents

* [About](#about)
* [Features](#features)
* [Tech stack](#tech-stack)
* [Requirements](#requirements)
* [Quick start (local)](#quick-start-local)
* [Configuration](#configuration)
* [API endpoints (summary & examples)](#api-endpoints-summary--examples)
* [Project structure (important packages)](#project-structure-important-packages)
* [Development notes](#development-notes)
* [Troubleshooting](#troubleshooting)
* [Contributing](#contributing)
* [License & Author](#license--author)
* [Caveats & notes](#caveats--notes)

---

## About

This service is responsible for courier order lifecycle management and courier filtering. Typical responsibilities include:

* Creating and updating courier orders
* Cancelling orders and changing delivery state
* Returning order history (paged and unpaged)
* Returning courier lists filtered by availability and other criteria
* Securing endpoints with JWT and propagating Authorization headers to downstream calls when needed

The application is configured to register with **Eureka** and fetch configuration from **Spring Cloud Config** by default.

---

## Features

* REST API for courier order operations (create, change, cancel, detail, history, assign)
* Courier filtering endpoint
* JWT-based authentication + `TokenProvider` utilities
* Redis (Redisson) used for token storage
* Persistence with Spring Data JPA (PostgreSQL driver included)
* Swagger (Springfox) documentation configured
* OpenFeign support + interceptor to forward `Authorization` header
* MapStruct used for mapping DTOs <-> entities

---

## Tech stack

* Java 11
* Spring Boot 2.6.x
* Spring Security (JWT)
* Spring Data JPA + PostgreSQL driver
* Spring Cloud (Eureka client, Config)
* OpenFeign (feign-annotation-error-decoder)
* Redisson (Redis client)
* MapStruct, Lombok
* Swagger (springfox)
* Gradle (Gradle wrapper included)

---

## Requirements

* JDK 11+
* Redis (if you enable Redis-backed token storage)
* (Optional) PostgreSQL — if you enable DB persistence
* (Optional) Spring Cloud Config server (defaults to `http://localhost:8888`)
* (Optional) Eureka server for service discovery (the app runs standalone if you provide downstream service URLs)

---

## Quick start (local)

Build the project:

```bash
# from project root (Gradle wrapper included)
./gradlew clean build
```

Run with Gradle:

```bash
./gradlew bootRun
```

Or run the produced jar (example with property overrides):

```bash
./gradlew bootJar
java -jar build/libs/ms-courier-order-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=local \
  --application.security.authentication.jwt.secret='change-me' \
  --application.security.authentication.jwt.token-validity-in-seconds=86400 \
  --redis.url=redis://127.0.0.1:6379 \
  --server.port=8080
```

Notes:

* Default application name: `ms-courier-order`.
* `spring.cloud.config.uri` defaults to `http://localhost:8888` (see `src/main/resources/application.yml`). If you don't run a Config Server, pass configuration properties through CLI or environment variables.

---

## Configuration

Key properties used by the application (found in code/config):

* `application.security.authentication.jwt.secret` — JWT signing secret (required)
* `application.security.authentication.jwt.token-validity-in-seconds` — token TTL in seconds
* `redis.url` — Redisson/Redis URL (for token storage)
* `spring.cloud.config.uri` — Spring Cloud Config server URI (default `http://localhost:8888`)

You can provide these via:

* Spring Cloud Config
* `application-*.yml` file
* Environment variables
* Command-line args (`--property=value`)

---

## API endpoints (summary & examples)

Base paths (from controllers):

* `/courier` — courier-related endpoints
* `/courier/order` — courier order endpoints

### Courier filtering

`POST /courier/filter`
Request body: `CourierFilterRequest` (JSON)
Example fields (partial):

```json
{
  "availabilityState": ["READY"]
}
```

Response: list of `CourierFilterDto` (name, surname, email, mobile, availabilityState, type, ...)

### Create courier order

`POST /courier/order/create`
Request body: `CourierOrderRequest`
Example:

```json
{
  "id": 123,
  "deliverAddress": "123 Main St, City",
  "amount": "25.00",
  "measurement": {"unit":"kg","value":2.5},
  "deliveryDate": "2025-10-10T10:00:00Z"
}
```

Response: `CourierOrderDto` (parcelId, deliveryDate, state, ...)

### Change courier order

`POST /courier/order/change`
Request body: `CourierOrderChangeRequest`
Example (partial):

```json
{
  "parcelId": 123,
  "destination": "New Address"
}
```

Response: updated `CourierOrderDto`

### Cancel order

`PUT /courier/order/cancel/{id}`
Path param: `id` — parcel id (Long)
Response: HTTP 200 OK (no body)

### Get order detail

`GET /courier/order/detail?parcelId=<id>`
Response: `CourierOrderHistoryDto` (parcelId, courierOrderId, deliveryDate, routeBeginDate, state, measurement, deliverAddress, amount...)

### Get paged order history

`POST /courier/order/history`
Request body: `CourierOrderFilter`
Example:

```json
{
  "parcelId": null,
  "states": ["PROCESS", "PENDING"],
  "from": "2025-01-01T00:00:00Z",
  "to": "2025-12-31T23:59:59Z",
  "page": 0,
  "limit": 20,
  "courierId": null
}
```

Response: List of `CourierOrderHistoryDto`

### Get all order history (unpaged)

`POST /courier/order/history/all`
Request body: `CourierOrderFilter` (same as above)
Response: List of `AllOrderHistoryDto` — contains combined order + courier meta information

### Change delivery state

`POST /courier/order/change/state`
Request body: `OrderStateChangeRequest`
Example:

```json
{
  "deliveryState": "DELIVERED",
  "parcelId": 123
}
```

Response: HTTP 200 OK

### Assign courier to order

`POST /courier/order/assign`
Request body: `OrderAssignRequest`
Example:

```json
{
  "parcelId": 123,
  "accountId": 456
}
```

Response: `AllOrderHistoryDto` for the assigned order

---

## Project structure (important packages)

* `com.guavapay` — main application
* `com.guavapay.config` — Feign interceptor, Swagger, Redis, security configs
* `com.guavapay.controller` — REST controllers (`CourierController`, `CourierOrderController`)
* `com.guavapay.model` — DTOs, request objects, entities, mappers, converters
* `com.guavapay.repository` — Spring Data JPA repositories
* `com.guavapay.security` — `TokenProvider`, `Principal`
* `com.guavapay.service` — service interface & implementation (`CourierService`, `CourierServiceImpl`)
* `com.guavapay.cache` — token storage (Redisson)
* `com.guavapay.util` — serializers/deserializers and helpers

---

## Development notes

* Swagger UI endpoints (Springfox 2.x): `/swagger-ui.html`, `/v2/api-docs`.
* Feign interceptor forwards incoming `Authorization` header to outgoing Feign requests.
* MapStruct is used for DTO<->Entity mapping.
* Entities (e.g., `Parcel`, `Courier`, `CourierOrder`, `Account`) are JPA-annotated and configured to work with PostgreSQL/HikariCP.

---

## Troubleshooting

* **Config server unreachable**: `spring.cloud.config.uri` defaults to `http://localhost:8888`. If you don't use a Config Server, supply properties via CLI or env vars.
* **JWT errors**: Ensure `application.security.authentication.jwt.secret` is set and matches token issuer.
* **Redis issues**: Check `redis.url` and that Redis is reachable when using Redis-backed token storage.
* **Database issues**: Ensure PostgreSQL is running and connection properties are set if enabling persistence.
* **Feign/downstream**: If Feign clients call other microservices, ensure those services are available or override URLs and test locally.

---

## Contributing

* Fork the repo, create a feature branch, add tests, run `./gradlew build`, and open a pull request.
* Keep API contracts stable and update Swagger docs when adding/changing endpoints.

---

## License & Author

This repository includes an `LICENSE` file — the project is licensed under the terms provided there.
Author / contact: Eldar R. Novruzov

---
