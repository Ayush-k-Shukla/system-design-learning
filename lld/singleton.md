# Singleton Design Pattern

1. In software development, we often require classes that can only have one object. For example: thread pools, caches, loggers etc.
2. Creating more than one objects of these could lead to issues such as incorrect program behavior, overuse of resources, or inconsistent results.
3. This is where Singleton Design Pattern comes into play.

## Implementation

1. We need to make sure that only class itself allowed to instantiate not any other class.
2. We need to make constructor as `private` so no other class can create an object.
3. we will expose a public `getInstance` method for singleton class which will give instance of our class.

### Lazy Initialization

1. this allows us to create instance only when needed. if no singleton is needed so the resource will be saved.
2. This implementation is not thread safe as if multiple thread call `getInstance` same time and instance is null then may be multiple instances get created.

```java
class LazySingleton {
    // The single instance, initially null
    private static LazySingleton instance;

    // Private constructor to prevent instantiation
    private LazySingleton() {}

    // Public method to get the instance
    public static LazySingleton getInstance() {
        // Check if instance is null
        if (instance == null) {
            // If null, create a new instance
            instance = new LazySingleton();
        }
        // Return the instance (either newly created or existing)
        return instance;
    }
}
```

### Thread-safe Singleton

1. similar to lazy one but added `synchronized` so only thread can execute this at once. Need to acquire lock when executed.
2. Can cause extra overhead and time.

```java
class ThreadSafeSingleton {
    // The single instance, initially null
    private static ThreadSafeSingleton instance;

    // Private constructor to prevent instantiation
    private ThreadSafeSingleton() {}

    // Public method to get the instance, with synchronized keyword
    public static synchronized ThreadSafeSingleton getInstance() {
        // Check if instance is null
        if (instance == null) {
            // If null, create a new instance
            instance = new ThreadSafeSingleton();
        }
        // Return the instance (either newly created or existing)
        return instance;
    }
}
```

### Double checked locking

1. Only synchronize if instance is not present.
2. It uses the `volatile` keyword to ensure that changes to the instance variable are immediately visible to other threads.

```java
class DoubleCheckedSingleton {
    // The single instance, initially null, marked as volatile
    private static volatile DoubleCheckedSingleton instance;

    // Private constructor to prevent instantiation
    private DoubleCheckedSingleton() {}

    // Public method to get the instance
    public static DoubleCheckedSingleton getInstance() {
        // First check (not synchronized)
        if (instance == null) {
            // Synchronize on the class object
            synchronized (DoubleCheckedSingleton.class) {
                // Second check (synchronized)
                if (instance == null) {
                    // Create the instance
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        // Return the instance (either newly created or existing)
        return instance;
    }
}
```

### Eager Initialization

1. In this method, we rely on the JVM to create the singleton instance when the class is loaded. The JVM guarantees that the instance will be created before any thread access the instance variable.
2. This implementation is one of the simplest and inherently thread-safe without needing explicit synchronization.
3. static variable ensures there's only one instance shared across all instances of the class.
4. final prevents the instance from being reassigned after initialization.

```java
class EagerSingleton {
    // The single instance, created immediately
    private static final EagerSingleton instance = new EagerSingleton();

    // Private constructor to prevent instantiation
    private EagerSingleton() {}

    // Public method to get the instance
    public static EagerSingleton getInstance() {
        return instance;
    }
}
```

## Real world usage

1. Managing Shared resources (DB connections, caches, config settings)
2. Managing state (user session, app state)
3. Logger class (provides a global logger)
