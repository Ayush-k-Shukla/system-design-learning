# Blob Storage

- Blob Storage (Binary Large Object storage) is an **object storage system** designed for storing **large-scale unstructured data** such as images, videos, logs, backups, and documents.

- It provides simple operations:

  - PUT (upload)
  - GET (download)
  - DELETE
  - LIST

- No querying, no transactions, no joins.

## Why Blob Storage Exists ?

- DBs are optimized for **structured data and transactions**, not large files.

### Problems with using DBs for files

- Requires expensive SSDs
- Heavy DB engine overhead (indexes, locks, MVCC, ACID)
- High CPU and RAM usage
- Replication = 3× storage cost

### Blob storage solves this by

- Removing the DB engine
- Using cheap HDDs (for some prime Blob storage we use SSDs as well)
- Using simple APIs
- Optimizing for scale and durability
- Uses **erasure coding** instead of full replication

---

## Core Idea

- Write once, read many (append-only)
- Large sequential I/O
- Flat namespace (no real folders)
- Metadata separated from actual data
- High durability, relaxed latency requirements

---

## How Blob Storage Stores Data

- Uploaded file is split into **chunks** (4–16 MB each).
- Chunks are distributed across many servers.
- Stored on **cheap HDDs**, not SSDs.
- Metadata (object name, size, chunk locations) is stored separately on fast storage.
- Reads and writes happen in parallel.

---

## Blob Storage vs Database (Quick Comparison)

| Aspect     | Database     | Blob Storage   |
| ---------- | ------------ | -------------- |
| Engine     | Heavy        | Minimal        |
| Disk       | SSD          | HDD            |
| Updates    | Frequent     | Rare           |
| Redundancy | Replication  | Erasure Coding |
| Cost       | High         | Low            |
| Use case   | Transactions | Files & media  |
