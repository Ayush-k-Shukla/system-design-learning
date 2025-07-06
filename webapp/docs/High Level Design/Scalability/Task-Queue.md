## 📝 Task Queues

> Task queues receive tasks and their related data, run them, and then deliver their results. They can support scheduling and are used to run computationally intensive jobs in the background.

---

### ⚙️ Working

1. **Producer (Main App) submits a task.**
2. **Task Queue stores the task:** The task is temporarily stored in a queue (e.g., Redis, RabbitMQ).
3. **Worker picks up the task:** A background worker process listens for tasks and processes them.
4. **Worker executes the task:** The task (e.g., sending an email, resizing an image) is executed asynchronously.
5. **Result is stored or sent back:** The task result is stored in a database, logged, or sent back to the producer.

---

### 📦 Types

1. **Distributed task queues**
   - Tasks are distributed across multiple worker nodes for scalability.
   - Use case: High-traffic applications requiring parallel processing.
2. **Priority task queues**
   - Tasks are processed based on priority.
3. **Delayed task queues**
   - Tasks are executed after a delay.
   - Use case: Auto-retrying failed mechanisms.
4. **Scheduled task queues**
   - Used for sending daily reports, data backups, etc.
5. **Persistent task queues**
   - Used for financial transactions, order processing, etc.

---

### ⏰ When to Use

- **Background jobs:** Email sending, PDF generation, video processing, etc.
- **Task scheduling:** Running periodic jobs.

---

### ⭐ Popular Task Queues

- Celery
- BullMQ
