# Decorator (Wrapper) Design Pattern

- It is a structural design pattern that lets you dynamically add new behaviroul and functionalities without modifying underlying code.

## Problem

- Suppose we had a notification class that has a method `send` which initially send message as email, but we now want to give it to func of sms, slack and other messages as well.
- One way to implement is to use inheritance and build class on top of current Notifier. But in that approach if we have to pick a combination like (slack+sms) then in that case i have to create classes for all possible combination.

<img src="/img/lld/decorator-1.png" />

## Solution

- Most of the language in inheritance it is not possible to inherit the 2 classes so inheritance will not work.
- we need a way so we can plug and play with the new functionality based on requirements. like we can use **Aggregation** (object A contains objects B; B can live without A) or **Composition** (object A consists of objects B; A manages life cycle of B; B can’t live without A).
- Decorator is just like a wrapper which implement the same interface as wrapped object.

## Structure

Below is the general structure where we have a base implmentation and multiple decorators to add functionality.

<img src="/img/lld/decorator-2.png"/>

- **Component**: Common interface for wrapped and wrapee object.
- **Concrete Component**: A class that is already existing and need new functionalitis. It is the class or object that is being wrapped.
- **Base Decorator**: Base class of wrapper or decorator, It should have type same to Component so that it can accessed by other wrappers as well as existing client.
- **Concrete Decorator (n)**: Implementation of base decorator, typically add new features dynamically.
- **Client**: kind of place where we combine all decorators in order to use

## Implementation

- Here is the a decorator implementation for adding compress and encrypt functionalities to existing class of DataSource.

<img src="/img/lld/decorator-3.png" />

```js
// Component interface
interface DataSource is
    method writeData(data)
    method readData():data

// Component Concrete class
class FileDataSource implements DataSource is
    constructor FileDataSource(filename) { ... }

    method writeData(data) is
        // Write data to file.

    method readData():data is
        // Read data from file.

// Base decorator
class DataSourceDecorator implements DataSource is
    protected field wrappee: DataSource

    constructor DataSourceDecorator(source: DataSource) is
        wrappee = source

    // Simply delegate op to wrapee
    method writeData(data) is
        wrappee.writeData(data)

    method readData():data is
        return wrappee.readData()

// Concrete Decorator 1
class EncryptionDecorator extends DataSourceDecorator is
    method writeData(data) is
        // 1. Encrypt passed data.
        // 2. Pass encrypted data to the wrappee's writeData
        // method.

    method readData():data is
        // 1. Get data from the wrappee's readData method.
        // 2. Try to decrypt it if it's encrypted.
        // 3. Return the result.

// Concrete Decorator 2
class CompressionDecorator extends DataSourceDecorator is
    method writeData(data) is
        // 1. Compress passed data.
        // 2. Pass compressed data to the wrappee's writeData
        // method.

    method readData():data is
        // 1. Get data from the wrappee's readData method.
        // 2. Try to decompress it if it's compressed.
        // 3. Return the result.

// The app can assemble different stacks of decorators at
// runtime, depending on the configuration or environment.
class ApplicationConfigurator is
    method configurationExample() is
        source = new FileDataSource("salary.dat")
        if (enabledEncryption)
            source = new EncryptionDecorator(source)
        if (enabledCompression)
            source = new CompressionDecorator(source)

        logger = new SalaryManager(source)
        salary = logger.load()
    // ...
```

## Application

- When we want to add give extra behaviour to objects at runtime without breaking the code where objects is used.
- When it is not possible to extend the behaviour of object by using inheritance.

## Pros and Cons

### Pros

- Can extend obj behavious without making subclasses.
- add and remove responsibilities to objects at runtime
- Follow Single Resp. Principle.

### Cons

- It is hard to implement a decorator where behaviour of decorator doesn't depend on the order of decorator stack.
- Hard to remove a specific wrapper from the wrapper stack.
