# **Asynchronism**

## Message Queues

1. It is a communication mechanism that allows different parts of the system to send and receive messages asynchronously.
2. It acts as a **intermediary** which holds message sent from producer(or publisher) and delivers them to subscribers(or consumer).
3. It is a decoupled architecture where publisher and subscribers are not aware of each other.

### **Components**

![Image](./images/message-queue-components.jpg)

1. **Publisher** - entity that sends messages to queue
2. **Subscriber** - entity that reads message from queue
3. **Queue** - Data structure that stores message untill they are consumed
4. **Broker** - It is optional in some. It is a software that manages the message queues, handles that message are routed correctly between consumers and producers.
5. **Message** - a unit of data sent. generally contains some payload, metadata (headers, timestamps, priority) about the message.

### **How does Message Queues work**

![Image](./images/message-queue-working.jpg)

1. **Sending Message**
2. **Queueing Message**
   1. Queue stores the message temporarily, making available for one or more consumer
3. **Consuming Message**
   1. Message consumers retrieve messages from the queue when they are ready to process them. They can do this at their own pace, which enables asynchronous communication.
4. **Acknowledgement (Optional)**
   1. In some message queue systems, consumers can send acknowledgments back to the queue, indicating that they have successfully processed a message. This is essential for ensuring message delivery and preventing message loss.

### **Types**

1. **Point to Point Queue**
   ![Image](./images/p2p.jpg)

2. **Publish/Subscribe (Pub/Sub) Queue**
   ![Image](./images/pub-sub.jpg)

3. **Priority Queue**
4. **Dead Letter Queue (DLQ)**
   ![Image](./images/dlq.jpg)
