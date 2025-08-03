# Digital certificate and Mutual TLS

A digital certificate is like an online identity card, issued by a Certificate Authority (CA) (like Digicert and other). It helps verify the identity of a server or client during encrypted communication (like HTTPS or mTLS).

## Digital certificate contains

- Public key of the owner (server/client)
- Identity details (e.g., domain, organization)
- Expiry date
- CA’s digital signature

### Why Used

- To secure communication over the internet (e.g., HTTPS).
- To verify identity and avoid impersonation or man-in-the-middle attacks.

---

## Regular TLS (used in HTTPS)

- Client verifies the server via its digital certificate.
- Server doesn’t care who the client is — anyone can access public pages.

## Mutual TLS (mTLS)

- Both client and server verify each other's certificates.
- Enables two-way authentication.
- This adds an extra layer of trust and is commonly used in internal and secure systems.

---

## Pros of mTLS

- Strong authentication (both parties verified)
- End-to-end encrypted channel
- Resistant to impersonation and MITM attacks

## Cons of mTLS

- More complex setup
- Requires certificate rotation and management
- Not ideal for public user authentication (too heavy)

---

## TLS vs Mutual TLS

| Feature                   | TLS (Standard HTTPS)      | Mutual TLS (mTLS)                 |
| ------------------------- | ------------------------- | --------------------------------- |
| Who presents certificate? | Server only               | Both client and server            |
| Who verifies identity?    | Client only               | Both client and server            |
| Client identity verified? | No                        | Yes                               |
| Use case                  | Public websites, browsers | Internal APIs, service-to-service |
| Setup complexity          | Low                       | High                              |
| Security level            | Good                      | Very Strong                       |
