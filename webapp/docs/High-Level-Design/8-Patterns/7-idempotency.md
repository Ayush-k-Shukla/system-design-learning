# 🔁 Idempotency in Distributed Systems

<p>
   <img src="/img/hld/idempotency.webp" />
</p>

- In certain scenarios, we need an operation to produce the same result even if it is called multiple times. This property is called idempotency.
- For example, in a payment processing system, the call may fail for the client but be submitted successfully on the backend. When the user retries, the request will be submitted again and the user may be charged twice.

## Ways to Implement

### Unique Request Identifier with Idempotency Key

- **Working**
  - The client generates a unique key (maybe a UUID) for each operation and sends it in the request header.
  - The server stores the key and response in a central DB like Redis (for distributed systems).
  - If a request with the same key is found again, the server returns the cached response instead of processing it again.
- **Use case**
  - API-based transactions
  - Retry handling
- **Disadvantages**
  - Requires persistent storage
  - Needs key expiry handling

### In Messaging Systems

- In a messaging system, we can enforce idempotency by storing a log of processed message IDs and checking against it for every incoming message.
- Each message has a unique messageId. Before processing, we check if the messageId is already in processedMessages. If it is, the message is ignored; otherwise, it is processed and added to the set to avoid duplicates.

### In HTTP Methods

- GET, PUT, and DELETE are idempotent
- POST is not idempotent

### Some Other Ways

- Enforce unique constraints on database fields (e.g., unique transaction_id). If a duplicate request tries to insert the same record, the DB rejects it.
- Instead of a client-generated key, the server generates a hash of the request paylo- If the same hash is detected again, the request is considered a duplicate.
- Before processing a request, a lock is acquired. If the same request is received while the lock is active, it is rejected.

## Challenges and Considerations

- **Distributed Systems:** Ensuring idempotency across distributed systems can be challenging and may require distributed locking or consensus algorithms.
- **Time Window:** How long should idempotency guarantees be maintained? Forever, or for a limited time?
- **Database Constraints:** Not all operations are idempotent by default; unique constraints or upsert logic may be necessary to avoid duplication.
- **Performance Overhead:** Storing idempotency keys or checking for duplicate operations can add overhead and increase overall latency.

## Best Practices

- Use a unique identifier (UUID or user-based)
- Design for idempotency from the start
- Implement retry with backoff
