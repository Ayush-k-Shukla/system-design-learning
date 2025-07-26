# ☁️ Serverless Architecture

> Serverless architecture is an approach that allows developers to build and run services without having to manage the underlying infrastructure. Developers just write their code and deploy, while a cloud provider provisions servers to run their applications, databases, and storage systems at any scale.

---

## ⚙️ Working

1. One of the most popular serverless architectures is Function as a Service (FaaS), where developers write their application code as a set of discrete functions. Each function performs a specific task when triggered by an event, such as an incoming email or an HTTP request. After testing, developers deploy their functions, along with their triggers, to a cloud provider account. When a function is invoked, the cloud provider either executes the function on a running server or, if there is no server currently running, spins up a new server to execute the function. This execution process is abstracted away from developers, who focus on writing and deploying the application code.
2. Another popular option is serverless containers.

<p>
<img src="/img/hld/faas.jpg" />
</p>

---

## 🌟 Benefits

1. **Cost efficiency** (Pay per use)
2. **Scalability** (Auto scaling)
3. **Developer Productivity** (Focus on code)
4. **Resilience and Availability**
   - Each function/container runs in isolation
   - Built-in fault tolerance

---

## ⚠️ Challenges and Considerations

1. **Cold start latency**
   - The first invocation of a function after a long period of inactivity can experience higher latency.
   - Can be mitigated by using techniques like function warmers or provisioned concurrency.
2. **Complexity in State Management**
   - Since serverless functions are stateless, managing state across multiple sessions can be challenging.
3. **Vendor Lock-In**
   - Heavily reliant on services provided by a vendor.
   - Can be mitigated using an open-source serverless framework like Knative.
4. **Debugging and Monitoring**
5. **Resource Limits**
   - Serverless functions generally have execution time limits (e.g., 15 minutes for AWS Lambda).

---

## 🛠️ Common Use Cases

1. Web applications
2. Data processing
3. **Event-Driven Automation:** Serverless functions can automate routine tasks such as backups, monitoring, and notifications based on predefined events.
4. DevOps and CI/CD

---

## 🚀 Examples

1. Google Cloud Functions
2. AWS Lambda
3. Microsoft Azure Functions
