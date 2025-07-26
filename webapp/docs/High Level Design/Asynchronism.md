## Back Pressure

1. **Back pressure** is a mechanism used in **message queues, task queues, and network systems** to prevent **overloading** a system when it cannot process incoming requests fast enough. It helps maintain system stability by slowing down producers or rejecting new tasks when consumers (workers) are overwhelmed.

### **Why Occurs**

1. Producer sends tasks/messages faster than consumer can process.
2. Workers are slow due to hight CPU/memory usage
3. Queue storage is full

### **Techniques to handle back pressure**

| Technique                | How It Works                         | Use Case                           |
| ------------------------ | ------------------------------------ | ---------------------------------- |
| **Throttling**           | Limit request rate                   | Prevent API abuse, control traffic |
| **Load Shedding**        | Drop low-priority tasks              | Logging, analytics, monitoring     |
| **Auto Scaling**         | Add more workers when needed         | Cloud-based systems, Kubernetes    |
| **Queue Acknowledgment** | Process tasks only when ready        | RabbitMQ, Kafka, Redis Streams     |
| **Circuit Breaker**      | Stop requests to overloaded services | Microservices, APIs                |
