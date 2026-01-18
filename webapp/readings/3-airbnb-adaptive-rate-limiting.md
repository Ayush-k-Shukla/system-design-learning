---
slug: 3-abnb-adaptive-lb
title: Adaptive Traffic Management in Airbnb's Key‑Value Store
authors: [ayush]
date: 2025-12-10
tags: [Backend]
---

## Overview

Mussel, a multi‑tenant key‑value store used
by Airbnb evolved from static rate limiting to an adaptive, layered
traffic‑management system to handle volatile workloads.

## Problems with Static Rate Limiting

Static QPS‑based quotas caused issues:

- Cost variance across requests
  - It will treat 1 row scan and 1000 row scan as same always
- Hot‑key and skew issues
- Poor ability to adapt to traffic bursts

## Key Improvements made

- In order to make Adaptive Traffic Management some changes are done.

### 1. Resource‑Aware Rate Control (RARC)

- Introduces Request Units (RU) based on rows, bytes, and latency
- Token bucket at each dispatcher with certain rate of RU filling
- Expensive operations cost more RU

### 2. Load Shedding with Criticality Tiers

- Monitors real‑time latency ratios
- Dynamic request penalization

#### Latency Ratio

Each dispatcher computes a _latency ratio_, defined as:

```jsx
latency ratio = long-term p95 latency / short-term p95 latency
```

- A healthy system has a ratio ≈ **1.0**
- A drop toward **0.3** indicates latency is rising quickly

- **When the ratio crosses the threshold:**
  - The dispatcher **temporarily increases RU cost** for certain client classes
  - Their token buckets drain faster → they automatically back off
  - If the ratio falls further, penalties expand to more classes until latency normalize.

#### The Control-Delay (CoDel) inspired Queue policy

- The CoDel thread pool tackles the second hazard:
  - queue buildup inside the dispatcher itself.
- It monitors the time a request waits in the queue. If that sojourn time proves the system is already saturated, the request fails early, freeing up memory and threads for higher-priority work.

### 3. Hot‑Key Detection & DDoS Mitigation

#### Space‑Saving algorithm to track hot keys

- Every dispatcher streams incoming keys into an in-memory top-k counter.
- In just a few megabytes, it tracks approximate hit counts, maintains a frequency-ordered heap, and surfaces the hottest keys in real time in each individual dispatcher.
- Algo used not read much in details.

#### Local LRU cache and request coalescing

- When a key crosses the hot threshold, the dispatcher serves it from a process-local LRU cache.
- Entries expire after roughly three seconds, so they vanish as soon as demand cools; no global cache is required.
- A cache miss can still arrive multiple times in the same millisecond, so the dispatcher tracks in-flight reads for hot keys.
- New arrivals attach to the pending future; the first backend response then fans out to all waiters.
- In most cases only one request per hot key per dispatcher pod ever reaches the storage layer.

## Key Takeaways

- Measure real cost, not request count
- Keep control loops local (single node)
  - local cache
  - codel queue
- Continuous tuning needed as workloads evolve

## Context : Resource unit (RU) calculation working and Load Shedding

- Our Key-Value Store must decide whether to allow or throttle a request **before** the database executes it.
- Since actual cost (rows, bytes, latency) is only known afterward, Mussel uses a predictive cost model.

### RU is Estimated Using Request Shape

Before execution, the dispatcher knows:

- Number of keys requested based on output schema requested
- Query type (point lookup, multi-get, range scan)
- body size for writes
- Estimated number of rows based on key ranges
- Request payload size

This metadata allows an **upper-bound estimate** of resource cost.

A Simple formula could be like

```jsx
RU = α * estimated_rows + β * request_bytes + γ * expected_latency_factor;
```

Where:

- `α`, `β`, `γ` are constants tuned using historical profiling & benchmarks

- `expected_latency_factor` comes from past p95 for similar queries

- `estimated_rows` comes from the query shape (range start/end)

So even before execution, the system can compute an estimated RU charge.

### After DB runs the query, they sometimes adjust RU (but async)

**The backend shard knows:**

- exact rows scanned
- actual bytes read
- actual CPU/latency

But they do NOT retro-adjust the user’s token bucket, because that would require cross-component coordination.

**Instead:**

- backend reports metrics
- system updates the cost model coefficients for future requests
- So next time, the estimate becomes more accurate.

This is called an **adaptive cost model**.

### load shedding

- It is a resilience strategy where a system deliberately drops or rejects non-essential incoming requests or data during periods of high traffic or overload to protect core functionality, prevent crashes, and maintain low latency for critical operations, ensuring overall system stability and availability

[👉 Airbnb Article Link](https://medium.com/airbnb-engineering/from-static-rate-limiting-to-adaptive-traffic-management-in-airbnbs-key-value-store-29362764e5c2)

<!-- truncate -->
