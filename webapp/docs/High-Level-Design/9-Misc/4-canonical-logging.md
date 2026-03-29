# Canonical Logging vs Traditional Logging

---

## 1. Core Concepts

### Traditional Logging

- Free-form logs (text or inconsistent JSON)
- No fixed schema
- Hard to query and correlate

#### Example:

```txt
User login attempt: ayush@gmail.com
Password matched for user 123
Login successful
```

---

### Structured Logging

- Logs in JSON / key-value format
- Machine-readable
- Still inconsistent across services

#### Example (Structured but NOT Canonical):

```json
{ "msg": "login success", "userId": "123" }
```

```json
{ "event": "LOGIN", "uid": "123" }
```

👉 Problem: Different field names → hard to query

---

### Canonical Logging

- Structured logging **+ standardized schema**
- Often includes **one final summary log per request**
- Acts as **source of truth**

---

## 2. Key Difference

- **Structured Logging** = JSON logs
- **Canonical Logging** = JSON logs + **consistent schema across all services**

---

## 3. Stripe Insight (Important)

- Multiple debug logs during execution
- - **One canonical log line per request per service**

👉 This log is:

- Dense
- Aggregated
- Query-friendly

---

## 4. Example: Full Flow Comparison

### ❌ Traditional Logs (scattered)

```txt
Request started /login
Auth success user=123
DB query users table
Response 200
```

👉 To debug → need to correlate manually

---

### ✅ Canonical Log (single summary)

```json
{
  "timestamp": "2026-03-29T10:15:30Z",
  "service": "auth-service",
  "event": "USER_LOGIN",
  "user_id": "123",
  "status": "SUCCESS",
  "http_status": 200,
  "db_calls": 1,
  "cache_hit": false,
  "duration_ms": 120,
  "trace_id": "abc-xyz"
}
```

👉 Everything in one place ✅

---

## 5. What Goes Into a Canonical Log

- Request metadata → method, path
- User info → user_id
- System metrics → duration, DB calls
- Identifiers → trace_id
- Status → success/failure

---

## 6. Why Canonical Logs Exist

### Problem:

- Data spread across multiple logs
- Queries require correlation

### Solution:

- One **wide event** (all info in one log)

---

## 7. Benefits (with Examples)

### 🔍 Query Simplicity

#### Without canonical:

- Find logs
- Join by `trace_id`

#### With canonical:

```sql
status = "FAILURE" AND event = "USER_LOGIN"
```

---

### ⚡ Faster Debugging

Example:

```json
{
  "event": "PAYMENT",
  "status": "FAILURE",
  "error": "INSUFFICIENT_FUNDS"
}
```

👉 Root cause visible instantly

---

### 📊 Ad-hoc Analysis

Example:

```sql
avg(duration_ms) GROUP BY event
```

👉 Works because schema is consistent

---

### 🧩 Bridge Between Logs & Metrics

- Metrics: predefined (fast)
- Logs: flexible (slow)
- Canonical logs: **flexible + queryable**

---

## 8. Canonical Logging Pipeline

```text
Service → Canonical Log → Kafka → Data Warehouse → Dashboard
```

---

## 9. Canonical Logs vs Metrics vs Traces

| Type           | Example           | Use               |
| -------------- | ----------------- | ----------------- |
| Metrics        | `login_count=100` | Fast monitoring   |
| Logs           | “user logged in”  | Debugging         |
| Canonical Logs | full JSON summary | Query + analytics |
| Tracing        | spans per service | Deep debugging    |

---

## 10. Can Canonical Logs Replace Traditional Logs?

### ❌ Not fully

#### Example Problem:

```json
{
  "event": "LOGIN",
  "duration_ms": 1200
}
```

👉 You know it’s slow
👉 But not _why_

---

## 11. Step Logs Inside Canonical

### Your Idea:

```json
{
  "event": "LOGIN",
  "steps": [
    { "name": "auth", "time": 50 },
    { "name": "db", "time": 900 }
  ]
}
```

---

### Pros:

- Good for simple breakdown

### Cons:

- Large logs
- Hard to query nested data
- Doesn’t scale

---

## 12. Best Practice (Industry)

### Use 3 Layers:

#### 1. Canonical Log (Mandatory)

```json
{
  "event": "LOGIN",
  "duration_ms": 120,
  "status": "SUCCESS"
}
```

---

#### 2. Debug Logs (Selective)

```txt
DB query took 900ms
Cache miss occurred
```

---

#### 3. Distributed Tracing

- Shows full flow with timings

---

## 13. Cost Trade-off

### Extra cost:

- More logs
- Storage & ingestion

### Mitigation:

- Sampling (log 1% success)
- Keep all failures
- Short retention for debug logs

---

## 14. Key Design Principles

- Emit canonical log **at end of request**
- Always emit (even on failure)
- Keep schema:
  - flat
  - consistent
  - stable

---

## 15. Final Mental Model

- Canonical log = **summary**
- Debug logs = **details**
- Tracing = **deep visibility**
