# ☁️ Serverless Architecture

> Serverless architecture is an approach that allows developers to build and run services without having to manage the underlying infrastructure. Developers just write their code and deploy, while a cloud provider provisions servers to run their applications, databases, and storage systems at any scale.

## ⚙️ Working

- One of the most popular serverless architectures is **Function as a Service (FaaS)**.
  - where developers write their application code as a set of discrete functions. Each function performs a specific task when triggered by an event, such as an incoming email or an HTTP request.
  - After testing, developers deploy their functions, along with their triggers, to a cloud provider account.
  - When a function is invoked, the cloud provider either executes the function on a running server or, if there is no server currently running, spins up a new server to execute the function.
  - This execution process is abstracted away from developers, who focus on writing and deploying the application code.
- Another popular option is serverless containers.

<p>
   <img src="/img/hld/faas.jpg" />
</p>

---

## 🌟 Benefits

- **Cost efficiency** (Pay per use)
- **Scalability** (Auto scaling)
- **Developer Productivity** (Focus on code)
- **Resilience and Availability**
  - Each function/container runs in isolation
  - Built-in fault tolerance

## ⚠️ Challenges and Considerations

- **Cold start latency**
  - The first invocation of a function after a long period of inactivity can experience higher latency.
  - Can be mitigated by using techniques like function warmers or provisioned concurrency.
- **Complexity in State Management**
  - Since serverless functions are stateless, managing state across multiple sessions can be challenging.
- **Vendor Lock-In**
  - Heavily reliant on services provided by a vendor.
  - Can be mitigated using an open-source serverless framework like Knative.
- **Debugging and Monitoring**
- **Resource Limits**
  - Serverless functions generally have execution time limits (e.g., 15 minutes for AWS Lambda).

## 🛠️ Common Use Cases

- Web applications
- Data processing
- **Event-Driven Automation:** Serverless functions can automate routine tasks such as backups, monitoring, and notifications based on predefined events.
- DevOps and CI/CD

## 🚀 Examples

- Google Cloud Functions
- AWS Lambda
- Microsoft Azure Functions
