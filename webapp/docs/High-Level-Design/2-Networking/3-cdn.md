# Content Delivery Network (CDN)

- A globally distributed network of connected servers, serving data from a location near the user.
- Generally used for serving static data like images, HTML, CSS, JS, but some CDNs also support dynamic content.

## Types of CDN

### Pull CDN

- When you add new static data to your app, and a user from a different location tries to access it, the CDN will take time to load the data the first time as it is not present there. When a user accesses data not on the CDN, it pulls and stores it nearby.

### Push CDN

- Instead of waiting for the CDN to pull content when needed, you upload the entire content to the CDN beforehand. Your pictures, theme files, videos, and other assets are always on CDN servers around the world.

- Setting up a **pull CDN** is generally easy. Once configured, a pull CDN seamlessly stores and updates content on its servers as requested. The data typically stays there for 24 hours if not modified.

  - However, the ease of use can be a drawback. When making changes, you typically don't control how long the pull CDN cache lasts. If you update an image, it might take up to 24 hours to reflect changes, unless you clear the CDN cache.

- The decision on which CDN type to use depends on traffic and downloads. Blogs hosting large downloads (videos, podcasts) may find push CDNs cheaper and more efficient, as content is only updated when pushed. Pull CDNs help high-traffic, small-download sites by keeping popular content on CDN servers. Updates for content are infrequent enough to keep costs lower than push CDNs.

## Benefits of CDN

- Requests fulfilled by your CDN do not go to the origin server.
- Less distance traveled means faster response time.

## Disadvantages of CDN

- Content might be stale if updated before the TTL expires.
- CDN costs could be significant depending on traffic, but should be weighed against costs incurred without a CDN.
