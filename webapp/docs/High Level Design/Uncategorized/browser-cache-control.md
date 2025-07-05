# HTTP Cache Control

1. The http `cache-control` header contains some directives in both request and response that control [caching](../Scalability-files/Caches.md) in browser and shared caches(CDN, proxies).
2. We can only modify `CORS-safelisted response header` with cache-control directives as `Forbidden-request header` can not be updated programatically (user agent update this).

## Cache-Control Directives in Requests vs. Responses

| **Directive**                          | **Request (C → S)** | **Response (S → C)** | **Description & Use Case**                                                                    |
| -------------------------------------- | ------------------- | -------------------- | --------------------------------------------------------------------------------------------- |
| **`no-cache`**                         | ✅ Yes              | ✅ Yes               | Forces revalidation before serving cached content. Used when the client wants fresh data.     |
| **`no-store`**                         | ✅ Yes              | ✅ Yes               | Prevents caching entirely. Used for sensitive data like authentication, banking pages.        |
| **`max-age=<seconds>`**                | ✅ Yes              | ✅ Yes               | Specifies how long content can be considered fresh. Used to control caching behavior.         |
| **`s-maxage=<seconds>`**               | ❌ No               | ✅ Yes               | Similar to `max-age`, but applies to shared caches (CDNs, proxies).                           |
| **`public`**                           | ❌ No               | ✅ Yes               | Allows caching by any cache (browser, proxies, CDNs). Used for static assets.                 |
| **`private`**                          | ❌ No               | ✅ Yes               | Restricts caching to the end-user’s browser only. Used for user-specific data.                |
| **`must-revalidate`**                  | ❌ No               | ✅ Yes               | Forces caches to revalidate content before serving stale responses. used along with `max-age` |
| **`proxy-revalidate`**                 | ❌ No               | ✅ Yes               | Forces shared caches (CDNs, proxies) to revalidate stale content.                             |
| **`no-transform`**                     | ❌ No               | ✅ Yes               | Prevents caches from modifying content (e.g., image compression by proxies).                  |
| **`stale-while-revalidate=<seconds>`** | ❌ No               | ✅ Yes               | Allows serving stale content while revalidating in the background.                            |
| **`stale-if-error=<seconds>`**         | ❌ No               | ✅ Yes               | Serves stale content when the origin server is down.                                          |
| **`immutable`**                        | ❌ No               | ✅ Yes               | Indicates that a resource will not change while cached. Used for versioned assets.            |

### Note

`immutable` and `stale-while-revalidate` are not have all browser support.

## Common used patterns

### Cache Static assests

```sh
Cache-Control: public, immutable, max-age=432432434
```

1. used for versioned assets.(e.g. logo-v1.png,app-v2.js)
2. public: Allows caching for all (CDN, browser, proxies)
3. immutable: Did this so no need to revalidate by browser

### Prevent Caching entirely

```sh
Cache-Control: no-store
```

**Usecase:** Login pages, Transaction pages

### Ensure fresh data always

```sh
Cache-Control: no-cache, must-revalidate
```

**Usecase:** Dashboard analytics

### Serve cached content if server failure

```sh
Cache-Control: public, max-age=600, stale-if-error=3600
```

**Usecase:** For good user experience

### Optimise content delivery on cdn

```sh
Cache-Control: public, max-age=600, s-maxage=86400
```

CDN caches content for 1 day and browser for 10 mins.
