# Job Scheduler

A production-style distributed job scheduling system built with Spring Boot, PostgreSQL, and Redis.

The scheduler supports cron-based execution, worker-based processing, retries, dead-letter queues, execution history, recovery mechanisms, and Dockerized deployment.

---

## Features

### Job Management

* Create, update, delete jobs
* Pause and resume scheduled jobs
* Cron expression based scheduling
* Execution history tracking

### Queue Processing

* Redis-backed job queue
* Multiple worker threads
* Concurrent job execution
* Queue statistics API

### Reliability

* Retry mechanism for failed jobs
* Dead Letter Queue (DLQ)
* Replay failed jobs
* Recovery of interrupted jobs after restart
* Optimistic locking to prevent concurrent update issues
* Graceful worker shutdown

### Deployment

* Dockerized application
* Docker Compose support
* PostgreSQL persistence
* Redis integration

---

## Architecture

```text
                +----------------+
                | Spring Boot API|
                +--------+-------+
                         |
                         |
                +--------v-------+
                | Job Scheduler  |
                +--------+-------+
                         |
                Enqueue Runnable Jobs
                         |
                         v
                +----------------+
                | Redis Queue    |
                +--------+-------+
                         |
                         |
         +---------------+---------------+
         |               |               |
         v               v               v

   Worker-1        Worker-2        Worker-N
         |               |               |
         +---------------+---------------+
                         |
                         v
                +----------------+
                | Command Runner |
                +--------+-------+
                         |
                         v
                +----------------+
                | PostgreSQL     |
                | Jobs           |
                | Executions     |
                +----------------+
```

---

## Tech Stack

### Backend

* Java 21
* Spring Boot 3
* Spring Data JPA
* Spring Scheduling

### Database

* PostgreSQL
* Flyway Migrations

### Queue

* Redis

### Infrastructure

* Docker
* Docker Compose

---

## Job Lifecycle

```text
ACTIVE
   |
   v
QUEUED
   |
   v
RUNNING
   |
   +------ SUCCESS ------> ACTIVE
   |
   +------ FAILURE ------> RETRY
                                |
                                v
                           ACTIVE

Retries Exhausted
        |
        v
     FAILED
        |
        v
 Dead Letter Queue
```

---

## Database Schema

### jobs

Stores job definitions.

Key fields:

* id
* name
* command
* cron_expression
* status
* retry_count
* current_retry_count
* next_run_time

### job_execution

Stores execution history.

Key fields:

* id
* job_id
* status
* started_at
* completed_at
* execution_duration_ms
* worker_thread
* logs

---

## APIs

### Job APIs

Create Job

```http
POST /jobs
```

Get Job

```http
GET /jobs/{id}
```

Update Job

```http
PUT /jobs/{id}
```

Delete Job

```http
DELETE /jobs/{id}
```

Pause Job

```http
POST /jobs/{id}/pause
```

Resume Job

```http
POST /jobs/{id}/resume
```

---

### Queue APIs

Queue Statistics

```http
GET /queue/stats
```

Failed Queue

```http
GET /queue/failed
```

Replay Failed Jobs

```http
POST /queue/failed/replay
```

---

### Execution APIs

Execution History

```http
GET /executions
```

Execution Details

```http
GET /executions/{id}
```

---

## Running Locally

### Prerequisites

* Java 21
* Maven
* PostgreSQL
* Redis

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/ai-job-scheduler-0.0.1-SNAPSHOT.jar
```

---

## Running with Docker

### Build

```bash
docker compose build
```

### Start

```bash
docker compose up
```

### Stop

```bash
docker compose down
```

---

## Reliability Features

### Retry Mechanism

Failed jobs are retried automatically until the configured retry count is exhausted.

### Dead Letter Queue

Jobs that exceed retry limits are moved to a failed queue for later inspection and replay.

### Recovery Service

On application startup:

* RUNNING jobs are recovered
* Stale executions are detected
* Scheduler resumes safely

### Optimistic Locking

Version-based locking prevents concurrent updates from multiple workers.

---

## Future Improvements

* Prometheus metrics
* Grafana dashboards
* Distributed worker nodes
* Kafka/RabbitMQ integration
* Email/Slack notifications
* Job dependencies
* Job priorities
* AI-powered failure analysis

---

## Learning Outcomes

This project demonstrates:

* Concurrent programming
* Background job processing
* Distributed queueing concepts
* Redis integration
* Database transaction handling
* Retry and recovery strategies
* Dockerized deployment
* Production-grade scheduler design
