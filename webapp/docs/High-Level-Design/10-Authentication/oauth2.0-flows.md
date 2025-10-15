# 🔐 OAuth 2.0 Flows

OAuth (Open Authorization) is an open standard that allows **applications to access resources on behalf of a user without requiring their password**.

**Example:**
When you log in to a new website using “Login with Google,” you don’t give your Google password to that website. Instead, Google gives a **temporary access token** that allows limited access to your information.

---

## 🧩 Core Components

| Role                     | Description                      | Example      |
| ------------------------ | -------------------------------- | ------------ |
| **Resource Owner**       | The user who owns the data       | You          |
| **Client**               | The app that wants access        | Spotify      |
| **Authorization Server** | Issues tokens after user consent | Google OAuth |
| **Resource Server**      | Hosts the protected data         | Google APIs  |

---

## 🧠 Why Multiple OAuth 2.0 Flows Exist

Different app types have different security levels and capabilities.

- **Web servers** can store secrets safely.
- **Mobile / SPA** apps cannot.
- **Machine-to-machine** systems have no user at all.

Hence, OAuth 2.0 defines **different flows** for different use cases.

---

# 🚀 OAuth 2.0 Flows

## 1️⃣ Authorization Code Flow (For Server-side Web Apps)

Used by apps with a **secure backend** that can store a client secret.

### 🔄 Steps

1. User clicks “Login with Google.”
2. App redirects the user to Google’s OAuth endpoint.
3. User logs in and approves permissions.
4. Google redirects back with an **authorization code**.
5. Backend exchanges that code (using clientid) for an **access token** and optional **refresh token**.
6. Further Backend communicate securily using **access_token**

### ✅ Pros

- Very secure (tokens never exposed to the browser).
- Can get refresh tokens for long sessions.

### ❌ Cons

- Requires a backend server.
- Server should use HTTPS for security

---

## 2️⃣ Authorization Code Flow with PKCE (For Mobile & SPAs)

> PKCE = Proof Key for Code Exchange

Used when the app **cannot store a client_secret**, e.g. mobile apps or SPA.

### 🔒 How It Works

1. App generates a random **code_verifier** and a **code_challenge** (hash of it).
   1. code_verifier = SHA256(code_challenge)
2. Sends `code_challenge` with the auth request.
3. After login, receives the **authorization code**.
4. Exchanges the code + original `code_verifier` for tokens.
5. Auth Server validates the hash to ensure it’s the same app that initiated login.

### ✅ Pros

- Secure even for public clients (no secret needed).
- Prevents **authorization code interception** attacks.
- No XSS attack possible as code_verifier is generated and stored in frontend app state not in local storage or something.

### ❌ Cons

- Slightly more complex setup.

---

## 3️⃣ Implicit Flow (Deprecated)

Used by SPAs **before PKCE existed**. Tokens were returned directly in the redirect URL.

### ⚠️ Why Not to Use

Tokens in URLs can be exposed to browser history, extensions, or logs.
Now replaced by **PKCE flow**.

---

## 4️⃣ Client Credentials Flow (Machine-to-Machine)

Used when **no user is involved** — just two backend services communicating.

### ✅ Pros

- Simple and efficient for service-to-service communication.

### ❌ Cons

- Represents the **application** only, not a user.
- Must secure the client_secret.

---

## 5️⃣ Resource Owner Password Credentials (ROPC) Flow (Legacy)

User provides their **username and password directly** to the app.

### ⚠️ Not Recommended

Breaks OAuth’s purpose — the app could steal the password.
Use only for trusted internal systems (and even then, avoid it).

---

## 6️⃣ Device Code Flow (For TVs / IoT Devices)

Used when device has no browser or keyboard.

### 🔄 How It Works

1. Device shows a code and asks user to visit a URL (e.g., `google.com/device`).
2. User logs in from another device and enters the code.
3. Device polls the server until approved, then receives a token.

### ✅ Pros

- Works on limited-input devices.

### ❌ Cons

- Slower, more complex UX.

---

# 🔐 PKCE — Deep Dive

PKCE (Proof Key for Code Exchange) prevents attackers from stealing the authorization code.

| Step | Term                  | Description                                 |
| ---- | --------------------- | ------------------------------------------- |
| 1    | `code_verifier`       | Random string kept secret in the app        |
| 2    | `code_challenge`      | SHA256 hash of verifier sent to auth server |
| 3    | `authorization_code`  | Received after user login                   |
| 4    | `code_verifier` again | Sent later to prove it’s the same app       |

✅ This ensures even if someone steals the authorization code, they **can’t exchange it for tokens** without the `code_verifier`.

---

# 🧰 Security Concepts to Know

| Concept             | Description                                           |
| ------------------- | ----------------------------------------------------- |
| **Access Token**    | Used to access protected resources (short-lived)      |
| **Refresh Token**   | Used to get new access tokens without login           |
| **Client Secret**   | Private credential for confidential clients           |
| **Scope**           | Defines what access is granted (e.g., email, profile) |
| **Redirect URI**    | Callback URL after user authorization                 |
| **State Parameter** | Random string to prevent CSRF attacks                 |

- `state` param is used like, when client redirect to auth provider it adds a random generate state in query param and store it locally. so when auth server redirect back to client it verifies that state should be present and same. The issue it solves is even if attacker tricks user to visit malicious link on redirect back client will know it is a csrf issue as token will be wrong or not present.

---

# ⚔️ Common Attacks & Mitigations

| Attack                                | Description                        | Prevention                    |
| ------------------------------------- | ---------------------------------- | ----------------------------- |
| **Authorization Code Interception**   | Attacker steals code from redirect | Use PKCE                      |
| **Token Replay**                      | Reuse of old token                 | Short-lived tokens, use HTTPS |
| **CSRF (Cross-site request forgery)** | Fake redirect or callback          | Use `state` param             |
| **Token Leakage in URLs**             | Tokens in fragment or query        | Avoid Implicit Flow           |
| **Refresh Token Abuse**               | Compromised long-term token        | Use rotation and revocation   |

---

# 🆚 OAuth 1.0 vs OAuth 2.0

| Feature                    | OAuth 1.0                | OAuth 2.0                  |
| -------------------------- | ------------------------ | -------------------------- |
| **Signing method**         | Cryptographic signatures | Bearer tokens (simpler)    |
| **Ease of implementation** | Complex                  | Easier, but requires HTTPS |
| **Token type**             | Request/Access tokens    | Access/Refresh tokens      |
| **Mobile/SPA support**     | Poor                     | Excellent (via PKCE)       |
| **Extensibility**          | Limited                  | Highly extensible          |
| **Security**               | Built-in signing         | Relies on HTTPS + PKCE     |
| **Use today?**             | Deprecated               | Standard                   |

OAuth 2.0 simplified the protocol but shifted more responsibility to developers for security.

---

# 📚 Extra Things You Should Know

- **OIDC (OpenID Connect)**: Layer built on OAuth 2.0 for authentication (identity), adds `id_token` (JWT).
- **JWT (JSON Web Token)**: Common token format — signed, optionally encrypted, self-contained.
- **Token Introspection**: Endpoint to verify token validity.
- **Token Revocation**: Endpoint to revoke access or refresh tokens.
- **Scopes & Consent UI**: Always request minimum scopes necessary for principle of least privilege.
- **Use HTTPS always**: OAuth 2.0 assumes all communication is encrypted.
- **Avoid storing tokens in localStorage** in SPAs — use memory or HTTP-only cookies instead.

---

# 🧭 Quick Decision Table

| Use Case          | Recommended Flow               |
| ----------------- | ------------------------------ |
| Web App (Backend) | Authorization Code Flow        |
| SPA / Mobile App  | Authorization Code + PKCE      |
| Service ↔ Service | Client Credentials             |
| Smart TV / IoT    | Device Code Flow               |
| Legacy System     | Avoid, but ROPC if unavoidable |
