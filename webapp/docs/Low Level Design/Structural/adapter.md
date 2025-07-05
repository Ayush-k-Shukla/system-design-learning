# Adapter Design Pattern

- It is a **structural** design pattern that allows incompatible interfaces to work together by the converting the interfaces into one class that client expects.

## Useful in situations

- where we have to integrate with the legacy system or third party library which doesn't match current interface.
- Want to make unrelated classes work together

## Example without using adapter

- I have a payment system which currently only support legacy Bank Transfer but i want to implement modern stripe so
- first soln can be with writing conditional logic based on type but it further violate **Open/Closed** principle and make code tightly coupled.
- Current interfaces look like this

```java
public interface PaymentProcessor {
    void processPayment(double amount,String currency);
    bool isPaymentSuccess();
    String getTransactionId();
}
```

- and have a Checkout service which accept payment processor

```java
public class CheckoutService {
    private PaymentProcessor processor;

    ... further it used PaymentProcessor method to complete payment and check status
}
```

- Now we get requirement of integrating a legacy method for which interface like

```java
public interface LegacyProcessor {
    void executePayment(double amount,String currency);
    String getStatus(long tid);
    long getPaymentRefrence();
}
```

- So if we want to serve with LegacyProcessor as well and we have some constraint
  - Can't change CheckoutService as it is used at lot of other place
  - Can't modify legacy code
  - both option should work together

## Adapter Pattern

- To solve above issue adapter pattern comes into picture

### Class Diagram

<p align="center">
    <img src="/img/lld/adapter.png"/>
</p>

- **Target Interface (PaymentProcessor)** - The interface that client excpects and use.
- **Adapter** - The class that implements the target interface and uses adaptee internally. it convert calls to target interface to call to adaptee interface.
- **Adaptee (LegacyProcessor)** - the existing class with an incompatible interface that needs adapting.
- **Client (CheckoutService)** - part of systme that uses the target interface.

### Implementation

- Below is adapter implementation which implement PaymentProcessor (Target Interface) so that it use functions of LegacyProcessor(Adaptee).

```java
public class LegacyProcessorAdapter implements PaymentProcessor {
    private final LegacyProcessor legacyProcessor;
    private long ref;

    public LegacyProcessorAdapter(LegacyProcessor legacyp){
        this.legacyProcessor = legacyp;
    }

    @Override
    void processPayment(double amount,String currency){
        legacyProcessor.executePayment(amount,curreny); // fn of diff interface
        ref = legacyProcessor.getPaymentRefrence();
    }

    @Override
    bool isPaymentSuccess(){
        return legacyProcessor.getStatus(re) == "SUCCESS";
    }

    @Override
    String getTransactionId(){
        return "LEGACY_" + ref;
    }
}
```

- By this way our client code will be unchanged.

```java
public class MainApp {
    psvm(String[] args){

        // With Existing processor
        PaymentProcessor processor = new PaymentProcessor();
        CheckoutService checkout = new CheckoutService(processor);

        // With new legacy processor
        LegacyProcessor processor = new LegacyProcessor();
        processor = new LegacyProcessorAdapter(processor);
        CheckoutService legacyCheckout = new CheckoutService(processor);

    }
}

```

### What it actually does?

#### Composition

- the adapter wraps the legacy code instead of subclassing it. which benefit in
  - loose coupling
  - Flexible to change

#### Method Transalation

- Each method of PaymentProcessor is trnaslated into the equivalents call to the legacy API. this includes
  - renaming or remaping method name and return types
  - reorg parameters
