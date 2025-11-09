# Rate limiter

## Requirements

- Should allow limiting based On
  - userid
  - ip
  - api key
- Scale
  - Upto 1M req/sec
- gloabl or per instance - global
- what needed when limit exceed - 429
- should burst allowed? - yes

## HLD

```
Client → Rate Limiter → Downstream Service
```

as burst is allowed we will choose one of

- token bucket
- fixed window
- sliding window counter

but we want little smooth traffic (avoid fixed window as traffic is not dispersed much evenly)

- sliding window counter
- token bucket

As our scale is also very high so i will choose token bucket as sliding window counter will take extra space

```
 Rate Limiter will depend on Redis for atomic counters
```

## Data flow

- Request -> rl service
- rl service checks redis for token count (keyed by user/IP/API key).
- If tokens available → decrement and forward request.
- If not → respond with 429 Too Many Requests.
- Tokens are refilled periodically at a fixed rate.

### Token refilling normal

- we dont need to run any cron job or new thread for filling because this can be done mathemetically by taking care of lastRefillTime and current time
- Then we can calculate how many token present
  For e.g

```
suppose if fill rate is refill_rate
and we fillter at second 'last_refill_time' last time and current time is 'current_time' second
so available tokens will be

new_tokens = (current_time - last_refill_time) * refill_rate
current_tokens = min(max_tokens, current_tokens + new_tokens)
last_refill_time = current_time
```

- this above logic can be called everytime we handle request

### Token refilling distributed

- Each rate limiter server doesn’t have its own refill thread — instead, Redis handles this logic atomically using a Lua script. which will run at time of request handeling
