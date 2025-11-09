# Rate Limiting

Rate limiting is a technique used to control the number of requests a user or service can make in a given time window. It helps prevent abuse, protects APIs, and ensures fair usage among users.

## 💡 Why ?

- Prevents **DDoS attacks** and excessive API calls
- Ensures **fair resource allocation**
- Maintains **system stability and performance**

Without rate limiting, a system might get overwhelmed, leading to slow response times or complete service outages.

## ⚙️ Fixed Window Counter

[👉 View Implementation](https://github.com/Ayush-k-Shukla/small-dev-projects/blob/main/10.%20rate-limiting-algos/src/fixed-window-counter.ts)

### Idea

- Divide time into fixed windows (like 1s each).
- Each request increments a counter.
- When the window resets, the counter resets to zero.

### Pros

- Simple and easy to implement
- O(1) space and time

### Cons

- Allows burst traffic at window boundaries (the “boundary problem”)

## 🕒 Sliding Window Log

[👉 View Implementation](https://github.com/Ayush-k-Shukla/small-dev-projects/blob/main/10.%20rate-limiting-algos/src/sliding-window-log.ts)

### Idea

This keeps a log of timestamps for each request and uses this to determine if a new request should be allowed.

### Working

- Keep a log of request timestamps.
- When a new request arrives, **remove all entries older than the window size**.
- Count the remaining entries.
- If the count is **less than the limit**, allow the request and add its timestamp to the log.
- If the count **exceeds the limit**, deny the request.

### Pros

- Accurate per time window (no approximation)
- Easy to reason about
- Works well for low-volume APIs.

### Cons

- Uses more memory (O(N) timestamps)
- Slower under high request volume due to log cleanup
- Requires storing and searching through timestamps.

## 🔁 Sliding Window Counter

[👉 View Implementation](https://github.com/Ayush-k-Shukla/small-dev-projects/blob/main/10.%20rate-limiting-algos/src/sliding-window-counter.ts)

### Idea

- This algorithm improves upon the **Fixed Window Counter** by reducing the burst problem at window boundaries.
- It does this by taking into account requests from both the **current** and **previous** windows and **weighting them** based on the time elapsed in the current window.
- This makes the rate limiting smoother and more consistent over time.

### Working

1. Track the number of requests in the **current** and **previous** time windows.
2. When a new request comes in:
   - Determine how far into the current window the request arrived (the **elapsed time fraction**).
   - Calculate a **weighted sum**:

```
     weighted_requests = current_count + (1 - elapsed_fraction_of_current_window) * previous_count
```

3. If the `weighted_requests` is **less than the allowed limit**, allow the request and increment the current window count.
4. If it exceeds the limit, **deny the request**.
5. When the time moves into the next window, shift counts:
   - The current window count becomes the old window count.
   - Start a new window with a fresh counter.

### Pros

- Smooths request rate
- Reduces bursts
- More accurate than Fixed Window Counter.
- More memory-efficient than Sliding Window Log.
- Smooths out edges between windows.

### Cons

- Slightly more complex to implement

## 🪣 Token Bucket Algorithm

[👉 View Implementation](https://github.com/Ayush-k-Shukla/small-dev-projects/blob/main/10.%20rate-limiting-algos/src/token-bucket.ts)

### Idea

Tokens represent permission to make requests.A fixed number of tokens are added at fixed rate. Each request consumes one token.

### Working

- There is bucket that holds tokens and has a maximum capacity of tokens.
- Tokens are added to the bucket at a fixed rate (e.g., x tokens per second).
- When a request arrives, it must obtain a token from the bucket to proceed.
- If there are enough tokens, the request is allowed and tokens are removed.
- If there aren't enough tokens, the request is dropped.

### Pros

- Allows short bursts (requests up to the bucket's capacity, accommodating short-term spikes)
- Simple and efficient

### Cons

- Memory usage scales if implemeted per user.

## 💧 Leaky Bucket

[👉 View Implementation](https://github.com/Ayush-k-Shukla/small-dev-projects/blob/main/10.%20rate-limiting-algos/src/leaky-bucket.ts)

### Idea

Imagine a bucket that leaks water at a fixed rate.
Requests add water; if it overflows → reject.

### Pros

- Smooths traffic perfectly
- Prevents sudden spikes

### Cons

- Fixed outflow rate might delay urgent requests

## ⚖️ Choosing the Right Algorithm

Each rate-limiting algorithm has its own strengths and trade-offs.
The best choice depends on your use case, scalability needs, and fairness requirements.

| Algorithm                  | Accuracy       | Memory Usage | Allows Bursts | Smooth Traffic | Implementation Complexity | Best For                                 |
| -------------------------- | -------------- | ------------ | ------------- | -------------- | ------------------------- | ---------------------------------------- |
| **Fixed Window Counter**   | Low            | Very Low     | Yes           | ❌ No          | ✅ Very Simple            | Quick prototypes, low-traffic systems    |
| **Sliding Window Log**     | ✅ High        | High         | ⚠️ Limited    | ✅ Yes         | ⚙️ Moderate               | Precise API rate limiting per user       |
| **Sliding Window Counter** | ⚙️ Approximate | Low          | ⚠️ Slight     | ✅ Yes         | ⚙️ Moderate               | Smooth rate limiting for production APIs |
| **Token Bucket**           | ✅ High        | Low          | ✅ Yes        | ⚠️ Partial     | ✅ Common                 | General-purpose API rate limiting        |
| **Leaky Bucket**           | ✅ High        | Low          | ❌ No         | ✅ Perfect     | ⚙️ Moderate               | Network traffic shaping, message queues  |

- Smooth Traffic means request are spread out evenly over time

## 🧩 Practical Considerations

When implementing rate limiting in production systems, consider the following:

### **Distributed Environment**

If your API is served by multiple instances, in-memory counters will not synchronize.
Use a shared store such as **Redis**, **Memcached**, or **etcd** for consistent limits across nodes.

### **Key-based Rate Limiting**

Limit requests based on a unique key, such as:

- User ID
- IP Address
- API Key
- Endpoint Path

This ensures that one abusive client doesn’t affect all others.

### **Graceful Denial**

Instead of simply rejecting requests, return:

- HTTP **429 Too Many Requests**
- Include a **Retry-After** header to tell clients when to retry

### **Monitoring and Logging**

Track metrics such as:

- Number of requests denied
- Requests per client
- Rate-limit rule violations
