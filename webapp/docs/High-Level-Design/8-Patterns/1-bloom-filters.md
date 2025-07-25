# 🌸 Bloom Filters

1. Suppose we want to query some existence but we are okay with some false positives. For example, Netflix doesn't want to show you already watched movies.
2. It is a kind of probabilistic data structure which allows us to quickly check whether an element might be in a set.

---

### Components

1. **Bit Array**
   1. A bit array of size `n` where initially all bits are set to zero.
2. **Hash Functions**
   1. A hash function takes an input and maps it to an index of the bit array.
   2. In filters, we use `k` hash functions, so for each value there will be k indexes to be mapped in the array.

---

### Working

1. For each value, we get k indexes and mark bits as 1 there.
2. In lookup, we see if all bits for a given value's hash are 1, then the value is present in the set.
3. If any of the indexes is off, then the value is not present.
4. There can be false positives as well.

---

### Things to Consider

1. There is one drawback: traditional Bloom filters don't support deletion.
2. Size `n` must be chosen correctly, as large can reduce false positives but can increase memory usage.
3. Higher `k` improves accuracy but slows down insertions and lookups.
4. For more dynamic datasets, we can use Scalable Bloom filters, etc.

---

### Where Not to Use

1. Where false positives are not acceptable.
2. If exact checks are needed.
3. If deleting an element is necessary.
4. Storage is not an issue.

---

### Real World Applications

| **Use Case**         | **Company/Service**               | **Why Bloom Filters?**                |
| -------------------- | --------------------------------- | ------------------------------------- |
| Database Indexing    | Google Bigtable, HBase, Cassandra | Avoids unnecessary disk lookups       |
| Web Caching          | Facebook, Memcached               | Prevents unnecessary database queries |
| CDN Optimization     | Akamai                            | Reduces backend fetches               |
| URL Blacklist        | Google Chrome Safe Browsing       | Faster security checks                |
| Blockchain           | Bitcoin SPV Wallets               | Reduces storage & bandwidth usage     |
| Password Leak Checks | Have I Been Pwned?                | Checks without exposing passwords     |
| Spam Filtering       | Gmail, Yahoo Mail                 | Speeds up blocklist lookups           |

---

### Traditional vs Counting vs Scalable Bloom Filter

| Feature                   | Traditional Bloom Filter (BF) | Counting Bloom Filter (CBF) | Scalable Bloom Filter (SBF)  |
| ------------------------- | ----------------------------- | --------------------------- | ---------------------------- |
| **Memory Usage**          | Low                           | Higher (stores counters)    | Dynamic (expands as needed)  |
| **False Positive Rate**   | Fixed, increases with size    | Fixed, similar to BF        | Controlled, grows with data  |
| **Supports Deletions?**   | No                            | Yes (uses counters)         | No                           |
| **Handles Growing Data?** | No (fixed size)               | No (fixed size)             | Yes (expands dynamically)    |
| **Use Case**              | Fast membership checks        | Cache eviction, DB indexing | Web crawling, large datasets |
| **Hash Functions Used**   | Multiple                      | Multiple                    | Multiple (per sub-filter)    |
| **Example Usage**         | Caching, DB queries           | Memcached, DB indexing      | Google Bigtable, log storage |

1. Counting stores data in an array and supports deletion, as in each insertion count is increased and in deletion reduced.
2. Scalable uses a dynamic array.
