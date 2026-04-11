# 📸 Database Snapshots

## 🧠 What is a Snapshot?

A **snapshot** is a **read-only, point-in-time view** of the database.

- It is NOT a full copy (in modern systems)
- It is just a **logical view based on data versions**

---

## 🤔 Why Snapshots Exist

### 1. Backup & Recovery

- Restore database after failure

### 2. Analytics

- Run heavy queries without affecting production

### 3. Replication

- Create replicas quickly

### 4. Testing / Debugging

- Reproduce past state

### 5. Point-in-Time Recovery

- Restore DB to exact timestamp

---

## ⚙️ How Snapshots Work Internally (MVCC)

Instead of copying data, databases store **multiple versions of rows**.

Each row has metadata:

- `xmin` → when version was created
- `xmax` → when version became invalid

### Example

| Value | xmin | xmax |
| ----- | ---- | ---- |
| A=10  | T1   | T2   |
| A=100 | T2   | ∞    |

### Snapshot Rule

A snapshot reads rows where:

```
xmin ≤ snapshot_time < xmax
```

👉 Snapshot = just a **timestamp + visibility rules**

---

## 📦 How Data is Stored

> Database stores **versions of data**, NOT snapshots

Storage looks like:

```
[Row1: A=10, xmin=T1, xmax=T2]
[Row2: A=100, xmin=T2, xmax=∞]
```

---

## 🔁 Multiple Snapshots

- Many snapshots can exist at the same time
- Each query/transaction gets its own snapshot

Example:

| Snapshot | Value Seen |
| -------- | ---------- |
| S1       | A = 10     |
| S2       | A = 100    |
| Current  | A = 200    |

---

## 🧹 Garbage Collection (GC)

Old versions are deleted when:

- No active snapshot needs them

### Flow

1. Data updated → new versions created
2. Old snapshots finish
3. GC removes unused versions

---

## 📈 Storage Growth

### Case 1: Few updates

- when some rows or DB is updated

```
100GB DB + 10GB changes = 110GB
```

### Case 2: Full DB updated

- when almost all rows present in data is updated

```
100GB old + 100GB new = 200GB
```

👉 Temporary **2x storage possible**

---

## ⚠️ Storage Does NOT Grow Forever

Because:

- Old versions are cleaned by GC

Final storage:

```
≈ Current DB size
```

---

## 🔴 Real-World Issue: Bloat

If:

- Long-running queries exist
- Snapshots live too long

👉 Old data cannot be deleted

Result:

- Storage increases
- Performance degrades

---

## 💾 Backup Using Snapshots

### Step 1: Take Snapshot (Base Backup)

```
A = 10, B = 20
```

### Step 2: Record Logs (WAL)

```
A → 100
B → 200
```

### Step 3: Store Both

- Snapshot
- Logs

---

## 🔁 Restore Process

1. Load snapshot
2. Replay logs

Final state:

```
A = 100
B = 200
```

---

## 🧠 Key Insights (Must Remember)

- Snapshot is **not stored as full data**
- It is a **logical view over versions**
- Database stores **multiple versions of rows**
- Old versions are cleaned via **garbage collection**
- Storage may temporarily grow but stabilizes
