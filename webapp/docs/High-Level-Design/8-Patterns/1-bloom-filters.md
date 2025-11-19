# 🌸 Bloom Filters

- Suppose we want to query some existence but we are okay with some false positives. For example, Netflix doesn't want to show you already watched movies.
- It is a kind of probabilistic data structure which allows us to quickly check whether an element might be in a set.

## Components

### Bit Array

- A bit array of size `n` where initially all bits are set to zero.

### Hash Functions

- A hash function takes an input and maps it to an index of the bit array.
- In filters, we use `k` hash functions, so for each value there will be k indexes to be mapped in the array.

## Working

- For each value, we get k indexes and mark bits as 1 there.
- In lookup, we see if all bits for a given value's hash are 1, then the value is present in the set.
- If any of the indexes is off, then the value is not present.
- There can be false positives but not false negative, as if anyone of the value for hash is not set means value not exist.

## Things to Consider

- There is one drawback: traditional Bloom filters don't support deletion.
- Size `n` must be chosen correctly, as large can reduce false positives but can increase memory usage.
- Higher `k` improves accuracy but slows down insertions and lookups.
- For more dynamic datasets, we can use Scalable Bloom filters, etc.

## Where Not to Use

- Where false positives are not acceptable.
- If exact checks are needed.
- If deleting an element is necessary.
- Storage is not an issue.

## Real World Applications

| **Use Case**         | **Company/Service**               | **Why Bloom Filters?**                |
| -------------------- | --------------------------------- | ------------------------------------- |
| Database Indexing    | Google Bigtable, HBase, Cassandra | Avoids unnecessary disk lookups       |
| Web Caching          | Facebook, Memcached               | Prevents unnecessary database queries |
| URL Blacklist        | Google Chrome Safe Browsing       | Faster security checks                |
| Password Leak Checks | Have I Been Pwned?                | Checks without exposing passwords     |
| Spam Filtering       | Gmail, Yahoo Mail                 | Speeds up blocklist lookups           |

## Traditional vs Counting vs Scalable Bloom Filter

| Feature                   | Traditional (BF)           | Counting (CBF)              | Scalable (SBF)               |
| ------------------------- | -------------------------- | --------------------------- | ---------------------------- |
| **Memory Usage**          | Low                        | Higher (stores counters)    | Dynamic (expands as needed)  |
| **False Positive Rate**   | Fixed, increases with size | Fixed, similar to BF        | Controlled, grows with data  |
| **Supports Deletions?**   | No                         | Yes (uses counters)         | No                           |
| **Handles Growing Data?** | No (fixed size)            | No (fixed size)             | Yes (expands dynamically)    |
| **Use Case**              | Fast membership checks     | Cache eviction, DB indexing | Web crawling, large datasets |
| **Hash Functions Used**   | Multiple                   | Multiple                    | Multiple (per sub-filter)    |
| **Example Usage**         | Caching, DB queries        | Memcached, DB indexing      | Google Bigtable, log storage |

- Counting stores data in an array and supports deletion, as in each insertion count is increased and in deletion reduced.
- Scalable uses a dynamic array.
