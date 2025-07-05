## Task Queues

1. Tasks queues receive tasks and their related data, runs them, then delivers their results. They can support scheduling and can be used to run computationally-intensive jobs in the background.

### Working

1. Producer (Main App) submits a task
2. Task Queue stores the task - The task is temporarily stored in a queue (e.g., Redis, RabbitMQ).
3. Worker picks up the task - A background worker process listens for tasks and processes them.
4. Worker executes the task - The task (e.g., sending an email, resizing an image) is executed asynchronously.
5. Result is stored or sent back - The task result is stored in a database, logged, or sent back to the producer.

### Types

1. **Distributed task queues**
   1. Task distributed across multiple worker nodes for scalability.
   2. Usecase - High traffic application requiring parallel processing
2. **Priority task queues**
   1. Task processed based on priority
3. **Delayed task queues**
   1. Task executed after a delay
   2. usecase - Auto retrying failed mechanisms
4. **Scheduled task queues**
   1. Sending daily reports, data backups
5. **Persistent task queues**
   1. Financial transactions, Order processing

### **When to use**

1. Background jobs - Email sending, PDF generation, video processing, etc.
2. Task scheduling - Running periodic jobs

### **Popular**

1. Celery
2. BullMQ
