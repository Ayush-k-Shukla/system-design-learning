# Zero-downtime schema migration

- It means changing your database schema **without taking the application offline** and without breaking running requests.

- This becomes important in real systems because:
  - multiple app servers may be running different versions during deployment
  - old and new code can hit DB at same time
  - large tables can lock for long time if altered carelessly

- A safe migration is usually done in **phases**, not one big change.

### Example: Adding a New Column

     you have:

```sql
users(id, name)
```

and you want:

```sql
users(id, full_name)
```

instead of `name`.

Bad approach:

```sql
ALTER TABLE users DROP COLUMN name;
ALTER TABLE users ADD COLUMN full_name;
```

This breaks old code immediately.

Instead use an **expand → migrate → contract** strategy.

---

### Phase 1: Expand Schema (Backward Compatible)

Add new column first:

```sql
ALTER TABLE users ADD COLUMN full_name TEXT NULL;
```

Nothing breaks because old code still works.

Now schema is:

```sql
users(id, name, full_name)
```

---

### Phase 2: Dual Write

Update application:

Old:

```ts
INSERT INTO users(name)
```

New:

```ts
INSERT INTO users(name, full_name)
```

Whenever app writes:

- write both columns

Example:

```ts
await db.query(`
  INSERT INTO users(name, full_name)
  VALUES($1, $1)
`);
```

Now:

- old servers read/write `name`
- new servers can use `full_name`

Both versions coexist safely.

---

### Phase 3: Backfill Existing Data

Existing rows still have:

```sql
full_name = NULL
```

So run background migration:

```sql
UPDATE users
SET full_name = name
WHERE full_name IS NULL;
```

For huge tables:

- do batch updates
- avoid locking millions of rows

Example:

```sql
UPDATE users
SET full_name = name
WHERE id BETWEEN 1 AND 10000;
```

then next batch.

---

### Phase 4: Switch Reads

Application now reads from `full_name`.

Maybe temporarily:

```ts
const displayName = row.full_name ?? row.name;
```

This makes rollback safe too.

---

### Phase 5: Contract Schema

Once:

- all app servers use new code
- backfill completed
- no old readers/writers exist

remove old column:

```sql
ALTER TABLE users DROP COLUMN name;
```

Done without downtime.

---

### Core Principle

Every migration step should be:

- backward compatible
- forward compatible

Meaning:

- old app version works with new schema
- new app version works with old schema

---

### Common Zero-Downtime Patterns

#### 1. Renaming Column

Databases often lock or break dependencies.

Instead:

- create new column
- dual write
- backfill
- switch reads
- delete old column

---

#### 2. Changing Data Type

Example:

```sql
price VARCHAR -> BIGINT
```

Do:

- add new column `price_v2`
- dual write
- backfill converted data
- switch reads
- drop old column

---

#### 3. Splitting Table

Old:

```sql
users(id, name, address)
```

New:

```sql
users(id, name)
addresses(user_id, address)
```

Strategy:

- create new table
- dual write both places
- backfill
- switch reads
- remove old column

---

### Why This Is Hard in Distributed Systems

During deployment:

- some pods may run old code
- some pods run new code

For a few minutes both versions coexist.

If migration is not compatible:

- requests fail
- serialization errors happen
- null issues happen
- app crashes

---

### Large Table Migration Challenges

For huge tables:

- `ALTER TABLE` can lock writes
- index creation may block
- backfill can overload DB

Solutions:

- batch migration
- online index creation
- throttled workers
- shadow tables

Example in PostgreSQL:

```sql
CREATE INDEX CONCURRENTLY idx_users_email
ON users(email);
```

This avoids blocking writes.

---

### Blue-Green / Rolling Deployment Connection

Zero-downtime migrations are tightly connected with deployment strategies.

During rolling deploy:

1. old servers alive
2. new servers start gradually

So DB changes must support both.

That’s why destructive schema changes are delayed until the end.
