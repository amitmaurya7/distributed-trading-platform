# Distributed Trading Platform

## Overview

Distributed Trading Platform is a microservices-based trading application designed to simulate real-world order processing, trade execution, portfolio management, and market data handling using modern distributed system principles.

## Prerequisites

* Docker Desktop
* Git
* Java 21 (for future development)
* IntelliJ IDEA

## Local Infrastructure Setup

The project uses Docker Compose to provision local infrastructure services.

### Services

| Service    | Port |
| ---------- | ---- |
| PostgreSQL | 5432 |
| Zookeeper  | 2181 |
| Kafka      | 9092 |
| Redis      | 6379 |

### Start Infrastructure

```bash
docker compose up -d
```

### Verify Running Containers

```bash
docker ps
```

### Stop Infrastructure

```bash
docker compose stop
```

### Remove Infrastructure

```bash
docker compose down
```

## Service Verification

### PostgreSQL

```bash
docker exec dtp-postgres psql -U admin -d trading_db -c "SELECT current_database();"
```

### Redis

```bash
docker exec dtp-redis redis-cli ping
```

Expected Output:

```text
PONG
```

### Kafka

List Topics:

```bash
docker exec -it dtp-kafka kafka-topics --list --bootstrap-server localhost:9092
```

## Project Structure

```text
distributed-trading-platform/
├── docs/
├── docker-compose.yml
├── README.md
└── .gitignore
```

