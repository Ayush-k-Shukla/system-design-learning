# Builder Pattern

- It is a creational design pattern that lets you construct complex objects step-by-step, separating the construction logic from the final representation.

## Useful in situations

- An object requires many optional fields, and not all of them are needed every time.
- You want to avoid telescoping constructors or large constructors with multiple parameters.
- The object construction process involves multiple steps that need to happen in a particular order.

## Initial problem we have

- We are building a system to configure the HTTP requests. Each request is mix of some fields where some are required and some are totally optional. like
  - url (required)
  - method (require)
  - headers (optional)
  - body (optiona)
  - ....so on
- There are two naive approach to build this is to use **constructor overloading** or **telescoping constructor pattern**

### Constructor overloading

Each constructor has different sets of parameters, but they don’t chain, leading to repetition.

```
class HttpRequest {
    String url;
    String method;
    Map<String, String> headers;
    Map<String, String> queryParams;
    String body;
    int timeout;

    // Constructor with only URL and method
    public HttpRequest(String url, String method) {
        this.url = url;
        this.method = method;
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();
        this.body = "";
        this.timeout = 0;
    }

    // Constructor with URL, method, and headers
    public HttpRequest(String url, String method, Map<String, String> headers) {
        this(url, method); // This could be chained, but here it's a copy
        this.headers = headers;
    }

    // Constructor with all fields
    public HttpRequest(String url, String method, Map<String, String> headers,
                       Map<String, String> queryParams, String body, int timeout) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.queryParams = queryParams;
        this.body = body;
        this.timeout = timeout;
    }
}
```

### Telescoping Constructor pattern

Each constructor calls another, chaining parameters — becomes unreadable with many parameters.

```
class HttpRequest {
    String url;
    String method;
    Map<String, String> headers;
    Map<String, String> queryParams;
    String body;
    int timeout;

    public HttpRequest(String url, String method) {
        this(url, method, new HashMap<>());
    }

    public HttpRequest(String url, String method, Map<String, String> headers) {
        this(url, method, headers, new HashMap<>());
    }

    public HttpRequest(String url, String method, Map<String, String> headers,
                       Map<String, String> queryParams) {
        this(url, method, headers, queryParams, "", 0);
    }

    public HttpRequest(String url, String method, Map<String, String> headers,
                       Map<String, String> queryParams, String body, int timeout) {
        this.url = url;
        this.method = method;
        this.headers = headers;
        this.queryParams = queryParams;
        this.body = body;
        this.timeout = timeout;
    }
}
```

- In above approach we have to remeber full order and pass default values when ever we use it and it has other issue as well like
  - hard to read and write
  - Inflexible as if you want to set parameter 5 but not 3 and 4, you’re forced to pass null for 3 and 4.
  - Adding a new parameter is hard

## Builder Pattern

- The Builder pattern separates the construction of a complex object from its representation

### Class diagram

<p align="center">
    <img src="/img/lld/builder.png"/>
</p>

- **Builder (e.g., HttpRequestBuilder)**

  - Defines methods to configure or set up the product.
  - Typically returns `this` from each method to support a fluent interface.
  - Often implemented as a static nested class inside the product class.

- **ConcreteBuilder (e.g., StandardHttpRequestBuilder)**

  - Implements the Builder interface or defines the fluent methods directly.
  - Maintains state for each part of the product being built.
  - Implements the `build()` method that returns the final product instance.

- **Product (e.g., HttpRequest)**

  - The final object being constructed.
  - May be immutable and built only via the Builder.
  - Has a private constructor that takes in the builder’s internal state.

- **Director (Optional) (e.g., HttpRequestDirector)**
  - Orchestrates the building process using the builder.
  - Useful when you want to encapsulate standard configurations or reusable construction sequences.
  - In modern usage (especially in Java with fluent builders), the Director is often omitted, and the client takes on this role by chaining methods.

### Implementing Builder

#### Create Product Class

```java
class HttpRequest {
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final Map<String, String> queryParams;
    private final String body;
    private final int timeout;

    // private called by builder
    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.body = builder.body;
        this.timeout = builder.timeout;
    }

    public static class Builder {
        private String url;
        private String method;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParams = new HashMap<>();
        private String body = "";
        private int timeout = 0;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
```

#### Using on client side

```java
HttpRequest request = new HttpRequest.Builder()
    .setUrl("https://api.example.com")
    .setMethod("POST")
    .setHeaders(Map.of("Authorization", "Bearer token"))
    .setBody("{\"key\":\"value\"}")
    .setTimeout(5000)
    .build();
```

#### Benefits

- Avoids telescoping constructors and improves readability.
- Makes object construction flexible and clear.
- Can enforce immutability and validation before object creation.

#### When to Use

- When an object has many optional parameters.
- When you want to make the construction process readable and maintainable.
- When you want to enforce certain construction steps or validation.
