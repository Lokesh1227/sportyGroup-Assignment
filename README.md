# Betting Feed Manager

A Spring Boot microservice that accepts feeds from two providers (Alpha and Beta), normalizes them to a shared internal format, and dispatches the standardized messages to a mocked in-memory queue (with logs).

## Features
- Two HTTP POST endpoints to receive provider payloads
  - /v1/provider-alpha/feed (msg_type: odds_update | settlement)
  - /v1/provider-beta/feed (type: ODDS | SETTLEMENT)
- Parsing and validation of provider-specific JSON
- Normalization to internal betting event messages with metadata
- Mock queue publisher that logs messages and stores them in-memory
- Swagger UI for interactive testing
- Dockerfile and Docker Compose for containerized runs

## Endpoints
Provider Alpha ODDS_CHANGE request:
```
POST http://localhost:8080/provider-alpha/feed
Content-Type: application/json
{
  "msg_type": "odds_update",
  "event_id": "ev123",
  "values": {"1": 2.0, "X": 3.1, "2": 3.8}
}
```

Provider Alpha BET_SETTLEMENT request:
```
POST http://localhost:8080/provider-alpha/feed
Content-Type: application/json
{
  "msg_type": "settlement",
  "event_id": "ev123",
  "outcome": "1"
}
```

Provider Beta ODDS_CHANGE request:
```
POST http://localhost:8080/provider-beta/feed
Content-Type: application/json
{
  "type": "ODDS",
  "event_id": "ev456",
  "odds": {"home": 1.95, "draw": 3.2, "away": 4.0}
}
```

Provider Beta BET_SETTLEMENT request:
```
POST http://localhost:8080/provider-beta/feed
Content-Type: application/json
{
  "type": "SETTLEMENT",
  "event_id": "ev456",
  "result": "away"
}
```

## Observability
Standardized messages are logged with prefix [QUEUE] and also stored in an in-memory queue (`MessageQueue` bean). This is a mock of a real message queue.

## Standardized Internal Model
Internal standardized messages live under com.spg.betting_feed_manager.model.BettingEventMessages and include:
- BettingEventMessage (sealed super type): eventId, messageProvider(ALPHA|BETA), eventType(ODDS_CHANGE|BET_SETTLEMENT), receivedAt
- BettingEventOddsChangeMessage: outcomeOdds is Map<BetOutcome, Double> where BetOutcome ∈ {HOME, DRAW, AWAY}
- BettingEventSettlementMessage: winningOutcome is BetOutcome

## Run locally
Prerequisites:
- Java 17+
- Maven 3.9+

Run:
```
mvn spring-boot:run
```
Swagger UI:
- http://localhost:8080/swagger-ui.html (or /swagger-ui/index.html)
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Docker
Build and run with Docker:
- docker build -t betting-feed-manager:latest .
- docker run --rm -p 8080:8080 --name bfm betting-feed-manager:latest

Or with Docker Compose:
- docker compose up --build

The compose service is named BettingFeedManager and exposes port 8080.

Env vars:
- SPRING_PROFILES_ACTIVE (default: default)
- JAVA_OPTS (e.g., -Xms256m -Xmx512m)

## Testing
Run tests:
- mvn test

## Notes
- Messages are logged with prefix [QUEUE] by the MessageQueue component.
- Validation errors return HTTP 400 with a JSON body: {"error": "..."}.
