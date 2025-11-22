# 🌐 What Happens When You Hit a URL in the Browser?

<p align="center">
<img src="/img/hld/HTTP-high-level-workflow.webp" />
</p>

---

## 📝 Steps

### [DNS Resolution](../2-Networking/2-dns.md)

### **TCP Three-Way Handshake**

- The client creates a connection to the server to transfer and receive data. TCP is one of the underlying protocols in Hypertext Transfer Protocol (HTTP).
- The client performs a three-way handshake with the server to establish a TCP connection. TCP requires a three-way handshake because of the bi-directional communication channel. If you make a two-way handshake, you can only start a single-directional communication channel.
<p align="center">
   <img src="/img/hld/TCP-three-way-handshake.webp" />
</p>
- The following synchronize (SYN) and acknowledge (ACK) messages are sent between the client and the server to open a TCP connection:
  - The client sends a SYN request with a random sequence number (x).
  - The server responds with SYN-ACK. The acknowledgment number is set to one more than the received sequence number (x+1). The server sends another random sequence number (y).
  - The client sends ACK. The client sends an acknowledgment number that is one more than the received sequence number (y+1).
