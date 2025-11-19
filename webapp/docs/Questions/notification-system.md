# Notification System

- Summary Generate by ChatGPT when I was a interviewee

## 🧩 Problem Statement

Design a **Notification System** that can send notifications (Email, SMS, Push) to users in real-time.

## 🧠 Clarifications

| Question           | Answer                              |
| ------------------ | ----------------------------------- |
| Supported Channels | Email, SMS, Push Notifications      |
| Mode               | Real-time                           |
| Scale              | 10M notifications/day, ~2K/sec peak |
| Retry Logic        | Yes, with DLQ for failed retries    |

## 🏗️ High-Level Flow

1. **Trigger Event:** Some user or system action triggers a notification event.
2. **Notification Service:** Receives the event and validates user preferences.
3. **Publish to Queue:** Notification service publishes the message to Kafka/RabbitMQ.
4. **Consumers:** Channel-specific workers (Email/SMS/Push) consume messages.
5. **Delivery:** Workers send notifications through external providers (e.g., SendGrid, Twilio).
6. **Retries/DLQ:** Failed attempts are retried or moved to a Dead Letter Queue.

## 🧩 High-Level Architecture

**Core Components:**

- **Notification Service:** Handles validation, preference check, and publishing.
- **Kafka/RabbitMQ:** Message broker for decoupled communication.
- **Channel Workers:** Email, SMS, Push consumers.
- **External Providers:** SendGrid, Twilio, Firebase, etc.
- **DLQ:** For failed messages and manual reprocessing.
- **Redis:** For caching idempotency keys and user preferences.
- **Monitoring Layer:** Grafana/Prometheus for metrics and alerts.

**Flow:**
`Trigger → Notification Service → Kafka → Channel Workers → Providers → User`

## ⚙️ Reliability & Fault Tolerance

- **Retries:** Implement retry logic with exponential backoff.
- **DLQ:** Store permanently failed messages for manual handling.
- **Idempotency:** Use Redis-based `eventId` or `messageId` with TTL to prevent duplicate notifications.
- **Backup Providers:** Fallback to alternate providers when primary fails.

## 🧭 Scalability

- Use **Kafka partitioning** (by `userId` or `notificationType`) for parallel consumption.
- Scale workers horizontally based on partitions.
- Use **idempotency keys** to prevent duplicate sends when consumers crash and recover.

## 🔐 Idempotency Design

- **Storage:** Redis
- **Key:** `eventId` or `userId:templateType`
- **TTL:** ~24 hours to avoid unbounded growth
- **Locking:** Ues Redis locks to handle concurrent sends
- **Cleanup:** Expire automatically via TTL or cron job

## 📊 Monitoring & Metrics

| Metric       | Component        | Description                    |
| ------------ | ---------------- | ------------------------------ |
| Success Rate | All              | Percentage of successful sends |
| Error Rate   | All              | Failure count / total attempts |
| Latency      | Service + Worker | Time from trigger to delivery  |
| Queue Depth  | Kafka            | Number of pending messages     |
| Retry Count  | DLQ              | Number of retried messages     |
| CPU/Memory   | All              | System health of services      |

**Alerting:**

- Alert if error rate > 5% in 5 minutes.
- Alert if queue depth > threshold.
- Alert on DLQ growth or worker unresponsiveness.

## ⚙️ Extensibility - User Preferences

- Store preferences in persistent DB (Postgres/DynamoDB).
- Cache in Redis using `userId → preferences` hash.
- On updates, invalidate Redis or update event-driven.
- Apply preference checks before publishing to Kafka.

## 🧾 Optional API Contract Example

```http
POST /notify
{
  "userId": "123",
  "type": "EMAIL",
  "template": "ORDER_SHIPPED",
  "data": { "orderId": "A123" }
}
```

## 📚 NFRs

| Requirement  | Description                                |
| ------------ | ------------------------------------------ |
| Availability | High, since delay is tolerable             |
| Latency      | less than 3 seconds for real-time delivery |
| Durability   | Guaranteed message persistence via Kafka   |
| Scalability  | Horizontally scalable consumers            |
| Reliability  | Retry + DLQ + backup provider              |
