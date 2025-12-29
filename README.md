# PassFlowChe Backend

Backend part of PassFlowChe project.

## Modules
- auth — authentication & JWT
- gateway — API Gateway
- path-flow — passenger detection & analytics

## Tech stack
- Java 17
- Spring Boot
- Spring Cloud Gateway
- PostgreSQL + PostGIS
- JWT

## Methods

### POST /auth/register

Registering a new user.

Request body:
```json
{
"email": "user@example.com",
"password": "strongPassword"
}
```

Response:
```json
{
  "message": "User registered successfully"
}
```

### POST /auth/login

Validates user credentials and returns a JWT token for further requests.

Request body:
```json
{
"email": "user@example.com",
"password": "strongPassword"
}
```

Response:
```json
{
"message": "JWT_TOKEN"
}
```

### GET /path-flow/allBusStops

Returns a list of all bus stops with coordinates. Access is allowed only to authorized users.

Authorization: Bearer <JWT_TOKEN>

Response: List of BusStopDto

### GET /path-flow/allPathParts

Returns all path parts. Access is allowed only to authorized users.

Authorization: Bearer <JWT_TOKEN>

Response: List of PathPartDto

### POST /path-flow/passengers/addPassenger

Accepts the passenger's coordinates, date and time, determines its state
(at a stop, on the road, passenger in a vehicle, etc.) and stores the result.

Authorization: Bearer <JWT_TOKEN>

Request body:
```json
{
"coordinates_passenger": "POINT(31.2888 51.4999)",
"date_passenger": "2025-01-01",
"time_passenger": "12:00:00"
}
```

### GET /path-flow/passengers/bus-stop/{busStopId}/count

Returns the number of unique passengers who were at a specific bus stop in the last 30 seconds.

Authorization: Bearer <JWT_TOKEN>

Response:
```json
5
```

### GET /path-flow/passengers/path-parts/count

Returns the number of unique passengers on each road section
in the last 30 seconds.

Response:
```json
{
"1": 3,
"2": 7,
"5": 2
}
```

## Notes

All requests are routed through API Gateway, which validates JWT tokens and forwards user context to services.
