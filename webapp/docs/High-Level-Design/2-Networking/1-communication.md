# Communication

![Image](/img/hld/communication.jpg)

## HTTP

- HTTP is a method for encoding and transporting data between a client and a server. It is a request/response protocol: clients issue requests and servers issue responses with relevant content and completion status info about the request.
- HTTP is an application layer protocol relying on lower-level protocols such as **TCP** and **UDP**.

| Verb   | Description                                               | Idempotent\* | Safe | Cacheable                               |
| ------ | --------------------------------------------------------- | ------------ | ---- | --------------------------------------- |
| GET    | Reads a resource                                          | Yes          | Yes  | Yes                                     |
| POST   | Creates a resource or trigger a process that handles data | No           | No   | Yes if response contains freshness info |
| PUT    | Creates or replace a resource                             | Yes          | No   | No                                      |
| PATCH  | Partially updates a resource                              | No           | No   | Yes if response contains freshness info |
| DELETE | Deletes a resource                                        | Yes          | No   | No                                      |

- **Idempotent** mean Can be called many times without different outcomes.
- **Safe** mean whether changes state of DB

# HTTP/1.1 vs HTTP/2 vs HTTP/3

| Feature              | HTTP/1.1                           | HTTP/2                                       | HTTP/3 (QUIC)                              |
| -------------------- | ---------------------------------- | -------------------------------------------- | ------------------------------------------ |
| **Transport**        | TCP                                | TCP                                          | **QUIC (UDP)**                             |
| **Multiplexing**     | ❌ No (One request per connection) | ✅ Yes (Multiple requests in one connection) | ✅ Yes (No TCP limitations)                |
| **HOL Blocking**     | ❌ Yes (Request-level)             | ❌ Yes (TCP-level)                           | ✅ No (QUIC eliminates it)                 |
| **Connection Setup** | Slow (Multiple round trips)        | Faster (3-way handshake)                     | **Fastest (0-RTT)**                        |
| **TLS**              | Optional                           | Optional                                     | **Mandatory (TLS 1.3)**                    |
| **Ideal for**        | Legacy systems, basic websites     | Modern web apps & APIs                       | Low-latency apps (Streaming, Gaming, VoIP) |
| **Adoption**         | Still supported, but outdated      | Standard for web apps                        | Growing (Google, Cloudflare, Facebook)     |

- **Head-of-Line (HOL) blocking** is when the first task in a queue blocks all behind it, lowering throughput.
- The concept of **0-RTT** is often mentioned in the context of the QUIC transport protocol (which is the foundation for HTTP/3). QUIC runs over UDP and introduces a secure handshake (using TLS 1.3) that is more efficient than TCP's handshake.

## Transmission control protocol (TCP)

- It is a connection oriented protocol. Connection is established and terminated using a handshake.
- All packets are guaranteed to reach destination in original order by
  - Checksum
  - Acknowledgement
- If sender does not get correct response then it will resend packets
- All these guarantees causes delays in TCP communication
  Use TCP over UDP when:
  - You need all of the data to arrive intact
  - You want to automatically make a best estimate use of the network throughput

## User datagram protocol (UDP)

- UDP is connectionless. Datagrams (analogous to packets) are guaranteed only at the datagram level. Datagrams might reach their destination out of order or not at all. UDP does not support congestion control. Without the guarantees that TCP support, UDP is generally more efficient.
- UDP is less reliable but works well in real time use cases such as VoIP, video chat, streaming, and realtime multiplayer games.
- Use UDP over TCP when:
  - You need the lowest latency
  - Late data is worse than loss of data
  - You want to implement your own error correction

## Remote Procedure Call (RPC)

- RPC focuses on calling functions (procedures) on a remote server, like calling a local function in code. It is more action-oriented than REST.
- RPC abstracts the complexity of the communication process, allowing developers to focus on the logic of the procedure.
- **Key Characteristics**
  - **Action oriented**
  - **Function calls** - It allows you to call functions or method directly on a remote server. The client sends a reuest to execute a specific method and server returns the result.
  - **Synchronous Communication** - Client wait for the server to complete the called procedure.
  - **Variety of Protocols** - RPC can be implemented using different protocols, such as JSON-RPC, XML-RPC, or gRPC.
  - **Interface Definition Language** - RPC system often used an IDL to define the interface between client and server.
- **Advantages**
  - High Performance
  - Simpler to implement when need to perform specific actions
  - More flexible in handeling complex which do not fit CRUD model
- **Disadvantages**
  - Tight coupling of client and server
  - Less standardization
  - Limited Tooling

## Representational state transfer (REST)

- It is an architectural style for designing networked applications.
- **Key Characteristics**
  - **Stateless**
  - **Resource oriented** - REST treats everything as a resource, such as users, products, or orders. Each resource is identified by a unique URL.
  - Leverages Standard HTTP methods
  - Follow client server architecture
  - **Cacheable** - REST response can be cached
- **Advantages**
  - **Scalability**
  - **Flexibility** - Allows different data formats likes JSON, XML or plaintext
  - **Standardization** - follow a standarized approcah that makes them easy to understand
  - **Wide Adoption**
- **Disadvantages**
  - **Verbosity** - when dealing with complex objects or relationships can be verbose
  - **Over-fetching/Under-fetching**
  - Inefficient for real-time applications (websockets and gRPC are better)

## Google Remote Procedure Call(gRPC)

- gRPC is a high-performance, open-source RPC framework that uses Protocol Buffers (Protobuf) for communication instead of JSON. It supports streaming and is widely used in microservices.
- **Advantages**
  - **Fast & Efficient** – Uses Protobuf (binary format) instead of JSON, reducing payload size.
  - **Streaming Support** – Supports real-time communication (unlike REST).
  - **Strong Typing** – Uses `.proto` files for schema definition
  - **Great for Microservices** – Optimized for inter-service communication
- **Disadvantages**
  - **Not Human-Readable** – Protobuf is binary, making debugging harder.
  - **More Complex Setup** – Requires defining .proto files and generating client/server code.
  - **Limited Browser Support** – Requires a gRPC-web proxy to work in web applications.

## GraphQL

- GraphQL is a query language that allows clients to request exactly the data they need, avoiding over-fetching and under-fetching.
- **Advantage**
  - No over/under fetching
  - Strog typing
  - Single endpoint
  - flexible queries - can fetch multiple related resources in one request
- **Disadvantages**
  - Complex to learn anf more setup than REST
  - Caching is harder
  - Increased server load (as we fetch all data from DB , only we dont send it to client)

## Comparison

| Feature               | REST                   | RPC                           | GraphQL            | gRPC                                 |
| --------------------- | ---------------------- | ----------------------------- | ------------------ | ------------------------------------ |
| **Data Structure**    | Resource-based (nouns) | Function-based (verbs)        | Query-based        | Function-based                       |
| **Communication**     | HTTP (JSON/XML)        | HTTP (JSON)                   | HTTP (JSON)        | HTTP/2 (Protobuf)                    |
| **Performance**       | Medium                 | Medium                        | Medium-High        | High (Binary)                        |
| **Over-fetching**     | Yes                    | Yes                           | No                 | No                                   |
| **Under-fetching**    | Yes                    | Yes                           | No                 | No                                   |
| **Streaming Support** | No                     | No                            | No                 | Yes                                  |
| **Caching**           | Easy (HTTP Caching)    | Hard                          | Hard               | Hard                                 |
| **Best Use Case**     | Web APIs, Public APIs  | Simple actions, Internal APIs | Client-driven APIs | Microservices, High-performance APIs |

- Caching is hard in others as because there are a lot of combination possible and also shape of response for each parameter compare to REST, so cache hit ration becomes very less

## Websockets

- It enables full-duplex, bidirectional communication between a client over a single TCP connection.

### Working

<p align="center">
   <img src="/img/hld/ws-working.jpg" />
</p>

- **Handshake**
  - The client initiates connection request using HTTP GET with and `upgrade` header set to `websocket`
  - if server support ws and accepts the request, it responds with a special `101` statud code, indicating protocol will be changed to websocket.
- **Connection**
  - Once handshake complete, the ws connection is established, connection will remain open untill it is closed by either the client and server.
- **Data transfer**
  - Both the client and server now sends message in realtime. these messages are sent in small packets called `frames`, and carry minimal overhead compared to HTTP.
- **Closure**
  - closed by with a `close` frame.

### Usecase

- Real time collaboration tools
- Real time chat apps
- Live notifications
- Multiplayer online game
- Live streaming

### Challenges and Considerations

- Some Proxy server don't support WS, and some firewall can also block.
- There should be a `fallback` implemented in case if client or network don't support WS. fallback mechanism can be `long-polling`.
- Need to secure by authentication and secure ws connection(wss://).

### Long Polling

<p align="center">
   <img src="/img/hld/long-polling.png" />
</p>

### Polling

<p align="center">
   <img src="/img/hld/polling.jpg" />
</p>

## Server Sent Events (SSE)

- It allows server to send un-directional messages/events to client over HTTP.
- SSE is a technology that provides asynchronous communication with event stream from server to the client over HTTP for web applications. The server can send un-directional messages/events to the client and can update the client asynchronously. Almost every browser is supporting the SSE except Internet Explorer.

### Working

<p align="center">
   <img src="/img/hld/sse.webp" />
</p>

- The server-sent events streaming can be started by the client’s GET request to Server.
  ```
  GET /api/v1/live-scores
  Accept: text/event-stream
  Cache-Control: no-cache
  Connection: keep-alive
  ```
- `Accept: text/event-stream` indicates the client waiting for event stream from the server, `Cache-Control: no-cache` indicates that disabling the caching and `Connection: keep-alive` indicates the persistent connection. This request will give us an open connection which we are going to use to fetch updates. After the connection, the server can send messages when the events are ready to send by the server. The important thing is that events are text messages in `UTF-8` encoding.
- List of pre-defined SSE field names include:
  - **event:** the event type defined by application
  - **data:** the data field for the event or message.
  - **retry:** The browser attempts to reconnect to the resource after a defined time when the connection is lost or closed.
  - **id:** id for each event/message

### Usecase

- E-commerce Projects (notify whenever the user needs the information)
- Tracking system
- Alarm/Alert Projects
- IoT Projects (Alarm, notify, events, rules, actions)
- Stock Markets (Bitcoin etc.)
- Breaking news, Sports Score Updates
- Delivery projects
- In-app notifications

### Challenges and Considerations

- One potential downside of using Server-Sent Events is the limitations in data format. Since SSE is restricted to transporting UTF-8 messages, binary data is not supported.
- When not used over HTTP/2, another limitation is the restricted number of concurrent connections per browser. With only six concurrent open SSE connections allowed at any given time, opening multiple tabs with SSE connections can become a bottleneck.
  - In HTTP/1.1, each SSE connection requires a separate TCP connection because there is no built-in multiplexing. However, HTTP/2 solves this by allowing multiple independent streams over a single TCP connection.

## WebSocket vs. SSE vs. Long Polling vs. Polling

| Feature                   | WebSocket                                   | SSE (Server-Sent Events)            | Long Polling                            | Polling                        |
| ------------------------- | ------------------------------------------- | ----------------------------------- | --------------------------------------- | ------------------------------ |
| **Communication Type**    | Full-duplex (bidirectional)                 | Unidirectional (server to client)   | Half-duplex (client-initiated)          | Half-duplex (client-initiated) |
| **Latency**               | Very low                                    | Low                                 | Medium                                  | High                           |
| **Efficiency**            | High (single persistent connection)         | High (single persistent connection) | Medium (delayed responses)              | Low (frequent requests)        |
| **Connection Overhead**   | Single handshake, then persistent           | Single HTTP connection (kept open)  | New request for each update             | New request for each update    |
| **Server Load**           | Low                                         | Low (compared to polling)           | Higher than SSE/WebSockets              | Very high                      |
| **Client Requests**       | One per session                             | One per session                     | Multiple but less frequent              | Frequent requests              |
| **Multiplexing (HTTP/2)** | Yes                                         | Yes (multiple streams possible)     | No                                      | No                             |
| **Best Use Cases**        | Real-time apps (chats, games, live updates) | Notifications, live data feeds      | Notifications, moderate real-time needs | Basic periodic updates         |
| **Scalability**           | High                                        | High                                | Moderate                                | Low                            |
