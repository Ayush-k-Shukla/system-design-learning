# 📝 Task Queues

> Task queues receive tasks and their related data, run them, and then deliver their results. They can support scheduling and are used to run computationally intensive jobs in the background.

---

### ⚙️ Working

- **Producer (Main App) submits a task.**
- **Task Queue stores the task:** The task is temporarily stored in a queue (e.g., Redis, RabbitMQ).
- **Worker picks up the task:** A background worker process listens for tasks and processes them.
- **Worker executes the task:** The task (e.g., sending an email, resizing an image) is executed asynchronously.
- **Result is stored or sent back:** The task result is stored in a database, logged, or sent back to the producer.

---

### 📦 Types

- **Distributed task queues:**
  - Tasks are distributed across multiple worker nodes for scalability.
  - Use case: High-traffic applications requiring parallel processing.
- **Priority task queues:**
  - Tasks are processed based on priority.
- **Delayed task queues:**
  - Tasks are executed after a delay.
  - Use case: Auto-retrying failed mechanisms.
- **Scheduled task queues:**
  - Used for sending daily reports, data backups, etc.
- **Persistent task queues:**
  - Used for financial transactions, order processing, etc.

---

### ⏰ When to Use

- **Background jobs:** Email sending, PDF generation, video processing, etc.
- **Task scheduling:** Running periodic jobs.

---

### ⭐ Popular Task Queues

- Celery
- BullMQ
