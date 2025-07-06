# 🔁 Idempotency in Distributed Systems

<p>
   <img src="/img/hld/idempotency.webp" />
</p>

1. In certain scenarios, we need an operation to produce the same result even if it is called multiple times. This property is called idempotency.
2. For example, in a payment processing system, the call may fail for the client but be submitted successfully on the backend. When the user retries, the request will be submitted again and the user may be charged twice.

## Ways to Implement

### Unique Request Identifier with Idempotency Key

1. **Working**
   1. The client generates a unique key (maybe a UUID) for each operation and sends it in the request header.
   2. The server stores the key and response in a central DB like Redis (for distributed systems).
   3. If a request with the same key is found again, the server returns the cached response instead of processing it again.
2. **Use case**
   1. API-based transactions
   2. Retry handling
3. **Disadvantages**
   1. Requires persistent storage
   2. Needs key expiry handling

### In Messaging Systems

1. In a messaging system, we can enforce idempotency by storing a log of processed message IDs and checking against it for every incoming message.
2. Each message has a unique messageId. Before processing, we check if the messageId is already in processedMessages. If it is, the message is ignored; otherwise, it is processed and added to the set to avoid duplicates.

### In HTTP Methods

1. GET, PUT, and DELETE are idempotent
2. POST is not idempotent

### Some Other Ways

1. Enforce unique constraints on database fields (e.g., unique transaction_id). If a duplicate request tries to insert the same record, the DB rejects it.
2. Instead of a client-generated key, the server generates a hash of the request payload. If the same hash is detected again, the request is considered a duplicate.
3. Before processing a request, a lock is acquired. If the same request is received while the lock is active, it is rejected.

## Challenges and Considerations

1. **Distributed Systems:** Ensuring idempotency across distributed systems can be challenging and may require distributed locking or consensus algorithms.
2. **Time Window:** How long should idempotency guarantees be maintained? Forever, or for a limited time?
3. **Database Constraints:** Not all operations are idempotent by default; unique constraints or upsert logic may be necessary to avoid duplication.
4. **Performance Overhead:** Storing idempotency keys or checking for duplicate operations can add overhead and increase overall latency.

## Best Practices

1. Use a unique identifier (UUID or user-based)
2. Design for idempotency from the start
3. Implement retry with backoff
