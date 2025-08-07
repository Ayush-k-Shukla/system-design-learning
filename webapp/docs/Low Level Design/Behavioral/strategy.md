# Strategy Design Pattern

- It is a **behavioral** design pattern that allow define a family of algorithms, and put them in separate class, we can use those objects intercchangble.

## Useful in Situations

- Multiple ways to perform a task or calculation
- Behaviour of class needs to change at runtime

## Problem

- Suppose we have a logic class to calculate price for a movie booking
- There can be multiple types of strategies possible for like
  - premium seat
  - weekend booking
  - time based
- One solution will be to add if else logic for each type but it will have following issue
  - violate Open/Closed principle like, everytime a new strategy added we need to change our logic for calculation
  - multiple if-else

## Strategy Pattern

- Here comes the solution that is strategy pattern, where we define family of all possible algorithms variation as a different strategy.
- All strategy can be used interchangaeable

### Class Diagram

<img src="/img/lld/strategy-1.png" />

- **Context**: It maintains refrence to one of the concrete strategies and communicate with the object only via strategy interface.
- **Strategy Interface**: Base class for Strategy. declares methods used by context to execute strategy.
- **Concrete Implementations**: Implements Strategy interface, Each concrete class has its own implementation.
- **Client**: Creates a strategy and passes it to context and then context expose a setter by which client can change strategy at run time.

### Pseudo code

```js
// Interface
interface Strategy is
    method execute(a, b)

// one type of strategy impl
class ConcreteStrategyAdd implements Strategy is
    method execute(a, b) is
        return a + b

// 2nd type of strategy impl
class ConcreteStrategySubtract implements Strategy is
    method execute(a, b) is
        return a - b

// 3rd type of strategy impl
class ConcreteStrategyMultiply implements Strategy is
    method execute(a, b) is
        return a * b

// context
class Context is
    private strategy: Strategy

    method setStrategy(Strategy strategy) is
        this.strategy = strategy

    method executeStrategy(int a, int b) is
        return strategy.execute(a, b)


// client
class ExampleApplication is
    method main() is
        Create context object.

        Read first number.
        Read last number.
        Read the desired action from user input.

        if (action == addition) then
            context.setStrategy(new ConcreteStrategyAdd())

        if (action == subtraction) then
            context.setStrategy(new ConcreteStrategySubtract())

        if (action == multiplication) then
            context.setStrategy(new ConcreteStrategyMultiply())

        result = context.executeStrategy(First number, Second number)

        Print result.
```

## Pros and Cons

### Pros

- can swap algos at runtime
- isolate impl of algo from the code that use it
- Open/Closed

### Cons

- If not much variation of algorithm then it overcomplicate
- clients need to be aware of all possible strategies and set them
