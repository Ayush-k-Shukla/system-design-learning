# 🔐 OpenID Connect (OIDC)

## 1. Overview

- **OAuth 2.0**: Protocol for **authorization** — “Can this app access this resource?”
- **OpenID Connect (OIDC)**: Layer on top of OAuth 2.0 for **authentication** — “Who is this user?”
- OIDC enables **login, SSO, and identity verification** using the same OAuth 2.0 flows.

---

## 4. OAuth2 vs OIDC

| Feature       | OAuth 2.0       | OIDC                                      |
| ------------- | --------------- | ----------------------------------------- |
| Purpose       | Authorization   | Authentication + Authorization            |
| Token         | Access Token    | Access Token + ID Token                   |
| Identity Info | ❌ No user info | ✅ Contains user info (name, email, etc.) |
| Use Case      | API access      | User login / SSO                          |

---

## 5. OIDC Flow (Authorization Code)

### ⚙️ Steps

1. **User initiates login**

   - Click “Login with Google”
   - Request `/authorize` with `scope=openid email profile` and `state`.

2. **User authenticates**

   - Logs into Authorization Server and consents.

3. **Authorization code returned**

   ```
   https://yourapp.com/callback?code=abc123&state=xyz123
   ```

4. **Exchange code for tokens**

   - POST `/token` with code + client_secret (if confidential client)
   - Response includes:
     ```json
     {
       "access_token": "ACCESS_TOKEN",
       "id_token": "ID_TOKEN",
       "refresh_token": "REFRESH_TOKEN",
       "token_type": "Bearer",
       "expires_in": 3600
     }
     ```

5. **Validate and decode ID Token**

   - JWT contains claims like `sub`, `name`, `email`, `iss`, `aud`.

6. **Optional: Fetch User Profile**

   - `/userinfo` endpoint using access token.

7. **User logged in**
   - App uses ID Token to establish session.

---

## 6. Access Tokens Without OIDC

- Access tokens **do not contain user identity** — they only authorize access to APIs.
- Example JWT payload:

```json
{
  "scope": "read write",
  "exp": 1708886000,
  "iss": "https://auth.example.com",
  "aud": "api.example.com",
  "sub": "user_12345"
}
```

- Fields:
  - `sub`: internal user ID
  - `scope`: allowed actions
  - `iss`: issuer
  - `aud`: intended audience
  - `exp`: expiry
- ❌ No `name`, `email`, or profile info — **authentication info missing**

---

## 7. Key Takeaways

1. **OAuth 2.0** = authorization (API access)
2. **OIDC** = authentication + authorization (user login)
3. **Access Token** = grants access to resources
4. **ID Token** = grants verified identity of the user
