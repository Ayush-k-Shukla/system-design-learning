# What Happens when we hit URL in browser

<p align="center">
<img src="/img/hld/HTTP-high-level-workflow.webp" />
</p>

## Steps

1. [DNS Resolution](./DNS-CDN-Load_balancer-Proxies.md#domain-name-system)
2. **TCP three-way Handshake**

   1. The client should create a connection to the server to transfer and receive data. TCP is one of the underlying protocols in Hypertext Transfer Protocol (HTTP).
   2. The client performs a three-way handshake with the server to establish a TCP connection. TCP requires a three-way handshake because of the bi-directional communication channel. If you make a two-way handshake, you can only start a single-directional communication channel.
   <p align="center">
      <img src="/img/hld/TCP-three-way-handshake.webp" />
   </p>

   3. The following synchronize (SYN) and acknowledge (ACK) messages are sent between the client and the server to open a TCP connection:
      1. The client sends an SYN request with a random sequence number (x).
      2. The server responds with SYN-ACK. The acknowledgment number is set to one more than the received sequence number (x+1). The server sends another random sequence number (y).
      3. The client sends ACK. The client sends an acknowledgment number that is one more than the received sequence number (y+1).
