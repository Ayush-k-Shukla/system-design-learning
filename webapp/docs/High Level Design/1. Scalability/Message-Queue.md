## 📬 Message Queues

> A message queue is a communication mechanism that allows different parts of a system to send and receive messages asynchronously. It acts as an intermediary, holding messages sent from producers (or publishers) and delivering them to subscribers (or consumers). This enables a decoupled architecture where publishers and subscribers are not aware of each other.

---

### 🧩 Components

![Image](/img/hld/message-queue-components.jpg)

- **Publisher** – Entity that sends messages to the queue.
- **Subscriber** – Entity that reads messages from the queue.
- **Queue** – Data structure that stores messages until they are consumed.
- **Broker** – (Optional) Software that manages the message queues and ensures messages are routed correctly between consumers and producers.
- **Message** – A unit of data sent, generally containing some payload and metadata (headers, timestamps, priority).

---

### 🔄 How Do Message Queues Work?

![Image](/img/hld/message-queue-working.jpg)

- **Sending Message**
- **Queueing Message**
- The queue stores the message temporarily, making it available for one or more consumers.
- **Consuming Message**
- Message consumers retrieve messages from the queue when they are ready to process them. They can do this at their own pace, which enables asynchronous communication.
- **Acknowledgment (Optional)**
- In some message queue systems, consumers can send acknowledgments back to the queue, indicating that they have successfully processed a message. This is essential for ensuring message delivery and preventing message loss.

---

### 📦 Types

---

**Point-to-Point Queue**

![Image](/img/hld/p2p.jpg)

- The simplest type. When a producer sends a message, the message is stored in the queue until the consumer retrieves it.
- Once a message is retrieved, it is removed from the queue and cannot be processed by another consumer.
- **Used in:**
  - Task processing systems
  - Log processing systems
  - Order processing systems

**Publish/Subscribe (Pub/Sub) Queue**

![Image](/img/hld/pub-sub.jpg)

- In this model, messages are published to a topic and multiple consumers can subscribe to that topic to receive messages.
- Messages are published to a topic instead of being sent directly to a queue.
- A Message Broker (Pub/Sub system) is a central system (e.g., Kafka, Google Pub/Sub, Redis) that distributes messages from publishers to all subscribed consumers.
- Subscribers receive messages: Any service subscribed to the topic will get the message.
- **Use cases:**
  - Inventory Service → Updates stock
  - Payment Service → Processes the payment
  - Shipping Service → Starts delivery
  - Notification Service → Sends email/SMS confirmation

**Priority Queue**

- Messages in the queue are assigned priorities, and higher-priority messages are processed before lower-priority ones.
- **Use case:**
  - Emergency alerts system
  - Healthcare (critical patient alerts)
  - Customer support system (e.g., premium customers get faster response)

**Dead Letter Queue (DLQ)**

![Image](/img/hld/dlq.jpg)

- Stores messages that could not be processed successfully after multiple retries.
- **Use case:**
  - Handling failed transactions in e-commerce
  - Useful for troubleshooting and handling failed messages

### **Advantages**

- **Decoupling:**
  - Producer and consumer are decoupled.
- **Asynchronous Processing:**
  - Producer can send messages to the queue and move on to other tasks. Similarly, the consumer can consume messages based on availability.
- **Fault Tolerance:**
  - Persistent queues ensure that messages are not lost even if the consumer fails. They also allow retries and error handling.
- **Scalability:**
  - Can scale horizontally by adding more consumers and producers.
- **Throttling:**
  - This can control the rate of message processing, preventing the consumer from being overloaded.

### **When to use**

- **Microservice architecture:**
  - Microservices need to communicate with each other, but direct calling can lead to tight coupling and cascading failure.
- **Task scheduling and background processing:**
  - Certain tasks, such as image processing or sending emails, are time-consuming and should not block the main application flow. Offload these tasks to a message queue and have background workers (consumers) process them asynchronously.
- **Event-driven architecture:**
  - Events need to be propagated to multiple services, but direct communication can lead to tight coupling. Use a Pub/Sub queue to broadcast events to all interested consumer services.
- **Reliable communication:**
  - Using persistent and retry-handled queues can make communication reliable.
- **Load leveling:**
  - Sudden spikes in requests can overwhelm a system, leading to degraded performance or failures. Queue incoming requests using a message queue and process them at a steady rate, ensuring that the system remains stable under load.

### **Best Practices for Implementing**

- **Idempotency:**
  - Ensure that duplicate messages are handled correctly.
- **Message Durability:**
  - Based on use case, implement persistent or transient messages, as persistent comes with some trade-offs.
- **Error Handling:**
  - Implement robust error handling by including retries, DLQ, and alerting mechanisms for failed message processing.
- **Security:**
  - Implement security by encryption and authentication.
- **Monitoring:**
  - Set up monitoring to check performance and health of the message queues, including throughput, queue length, and consumer lag.
- **Scalability:**

### **Popular Message Queues**

- **Apache Kafka:**
  - A distributed streaming platform that excels at handling large volumes of data. Most widely used for high throughput and event-driven systems.
- **RabbitMQ:**
  - A widely used open-source message broker that supports multiple messaging protocols, including AMQP. Supports P2P, Pub/Sub, and Priority.
- **Google Cloud Pub/Sub:**
  - A fully managed message queue service offered by Google Cloud, designed for real-time analytics and event-driven applications.
- **Amazon SQS:**
  - A fully managed message queue service provided by AWS. SQS is highly scalable and integrates well with other AWS services.
