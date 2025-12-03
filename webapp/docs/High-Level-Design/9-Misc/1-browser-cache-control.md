# 🗂️ HTTP Cache Control

- The HTTP `cache-control` header contains some directives in both requests and responses that control [caching](../5-Scalability/1-caches.md) in browsers and shared caches (CDNs, proxies).
- We can only modify `CORS-safelisted response header` with cache-control directives, as `Forbidden-request header` cannot be updated programmatically (the user agent updates this).

## Cache-Control Directives in Requests vs. Responses

| **Directive**                          | **Request (C → S)** | **Response (S → C)** | **Description & Use Case**                                                                    |
| -------------------------------------- | ------------------- | -------------------- | --------------------------------------------------------------------------------------------- |
| **`no-cache`**                         | ✅ Yes              | ✅ Yes               | Forces revalidation before serving cached content. Used when the client wants fresh data.     |
| **`no-store`**                         | ✅ Yes              | ✅ Yes               | Prevents caching entirely. Used for sensitive data like authentication, banking pages.        |
| **`max-age=<seconds>`**                | ✅ Yes              | ✅ Yes               | Specifies how long content can be considered fresh. Used to control caching behavior.         |
| **`s-maxage=<seconds>`**               | ❌ No               | ✅ Yes               | Similar to `max-age`, but applies to shared caches (CDNs, proxies).                           |
| **`public`**                           | ❌ No               | ✅ Yes               | Allows caching by any cache (browser, proxies, CDNs). Used for static assets.                 |
| **`private`**                          | ❌ No               | ✅ Yes               | Restricts caching to the end-user’s browser only. Used for user-specific data.                |
| **`must-revalidate`**                  | ❌ No               | ✅ Yes               | Forces caches to revalidate content before serving stale responses. Used along with `max-age` |
| **`proxy-revalidate`**                 | ❌ No               | ✅ Yes               | Forces shared caches (CDNs, proxies) to revalidate stale content.                             |
| **`no-transform`**                     | ❌ No               | ✅ Yes               | Prevents caches from modifying content (e.g., image compression by proxies).                  |
| **`stale-while-revalidate=<seconds>`** | ❌ No               | ✅ Yes               | Allows serving stale content while revalidating in the background.                            |
| **`stale-if-error=<seconds>`**         | ❌ No               | ✅ Yes               | Serves stale content when the origin server is down.                                          |
| **`immutable`**                        | ❌ No               | ✅ Yes               | Indicates that a resource will not change while cached. Used for versioned assets.            |

### Note

`immutable` and `stale-while-revalidate` do not have full browser support.

## Commonly Used Patterns

### Cache Static Assets

```sh
Cache-Control: public, immutable, max-age=432432434
```

- Used for versioned assets (e.g., logo-v1.png, app-v2.js)
- `public`: Allows caching for all (CDN, browser, proxies)
- `immutable`: No need to revalidate by browser

### Prevent Caching Entirely

```sh
Cache-Control: no-store
```

**Use case:** Login pages, transaction pages

### Ensure Fresh Data Always

```sh
Cache-Control: no-cache, must-revalidate
```

- **Use case:** Dashboard analytics

### Serve Cached Content if Server Failure

```sh
Cache-Control: public, max-age=600, stale-if-error=3600
```

- **Use case:** For good user experience

### Optimize Content Delivery on CDN

```sh
Cache-Control: public, max-age=600, s-maxage=86400
```

- CDN caches content for 1 day and browser for 10 minutes.
