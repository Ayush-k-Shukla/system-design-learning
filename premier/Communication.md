# **Communication**

    ![Image](./images/communication.jpg)

## **HTTP**

1. HTTP is a method for encoding and transporting data between a client and a server. It is a request/response protocol: clients issue requests and servers issue responses with relevant content and completion status info about the request.
2. HTTP is an application layer protocol relying on lower-level protocols such as **TCP** and **UDP**.

| Verb   | Description                                               | Idempotent\* | Safe | Cacheable                               |
| ------ | --------------------------------------------------------- | ------------ | ---- | --------------------------------------- |
| GET    | Reads a resource                                          | Yes          | Yes  | Yes                                     |
| POST   | Creates a resource or trigger a process that handles data | No           | No   | Yes if response contains freshness info |
| PUT    | Creates or replace a resource                             | Yes          | No   | No                                      |
| PATCH  | Partially updates a resource                              | No           | No   | Yes if response contains freshness info |
| DELETE | Deletes a resource                                        | Yes          | No   | No                                      |

\*Can be called many times without different outcomes.

## Transmission control protocol (TCP)

1. It is a connection oriented protocol. Connection is established and terminated using a handshake.
2. All packets are guaranteed to reach destination in original order by
   1. Checksum
   2. Acknowledgement
3. If sender does not get correct response then it will resend packets
4. All these guarantees causes delays in TCP communication
   Use TCP over UDP when:
   1. You need all of the data to arrive intact
   2. You want to automatically make a best estimate use of the network throughput

## User datagram protocol (UDP)

1. UDP is connectionless. Datagrams (analogous to packets) are guaranteed only at the datagram level. Datagrams might reach their destination out of order or not at all. UDP does not support congestion control. Without the guarantees that TCP support, UDP is generally more efficient.
2. UDP is less reliable but works well in real time use cases such as VoIP, video chat, streaming, and realtime multiplayer games.
3. Use UDP over TCP when:
   1. You need the lowest latency
   2. Late data is worse than loss of data
   3. You want to implement your own error correction

-----PENDING-----

## Remote Procedure Call (RPC)

## Representational state transfer (REST)

## GRPC

## GraphQL
