# SOLID Principle

The **SOLID principles** are five design principles intended to make software designs more understandable, flexible, and maintainable.

## **S - Single Responsibility Principle (SRP)**

1. A class should have only one reason to change.
2. It should only do one thing.

### Example:

```ts
class Invoice {
  constructor(private amount: number) {}

  calculateTotal(): number {
    return this.amount + this.amount * 0.18; // 18% tax
  }
}

class InvoicePrinter {
  print(invoice: Invoice) {
    console.log(`Invoice total is: ${invoice.calculateTotal()}`);
  }
}
```

- `Invoice` handles invoice logic.
- `InvoicePrinter` handles printing logic.
- Each class has a **single responsibility**.

## **O - Open/Closed Principle (OCP)**

1. Software entities should be open for extension, but closed for modification.
2. You should be able to add new functionality without modifying existing code.
3. essential because it promotes software extensibility and maintainability. By allowing software entities to be extended without modification, developers can add new functionality without the risk of breaking existing code. This results in code that is easier to maintain, extend, and reuse.

### Example:

```ts
interface Shape {
  area(): number;
}

class Circle implements Shape {
  constructor(private radius: number) {}

  area(): number {
    return Math.PI * this.radius * this.radius;
  }
}

class Square implements Shape {
  constructor(private side: number) {}

  area(): number {
    return this.side * this.side;
  }
}

class AreaCalculator {
  totalArea(shapes: Shape[]): number {
    return shapes.reduce((sum, shape) => sum + shape.area(), 0);
  }
}
```

- You can add more shapes like `Triangle`, `Rectangle`, etc., **without changing** `AreaCalculator`.

## **L - Liskov Substitution Principle (LSP)**

1. Subclasses should be substitutable for their base classes.
2. If `S` is a subclass of `T`, then objects of type `T` may be replaced with objects of type `S`.
3. The importance of the LSP lies in its ability to ensure that the behavior of a program remains consistent and predictable when substituting objects of different classes. Violating the LSP can lead to unexpected behavior, bugs, and maintainability issues.

### Example:

```ts
class Bird {
  fly() {
    console.log('Flying');
  }
}

class Sparrow extends Bird {}
class Eagle extends Bird {}

//  This works fine
const bird: Bird = new Sparrow();
bird.fly();
```

### Bad Example (Violates LSP):

```ts
class Ostrich extends Bird {
  fly() {
    throw new Error("Ostrich can't fly");
  }
}
```

- `Ostrich` shouldn’t inherit from `Bird` if it can’t fly. Use a different base class.

## **I - Interface Segregation Principle (ISP)**

1. Clients should not be forced to depend on interfaces they do not use.
2. The principle suggests that instead of creating a large interface that covers all the possible methods, it's better to create smaller, more focused interfaces for specific use cases. This approach results in interfaces that are more cohesive and less coupled.

### Example:

```ts
interface Printer {
  print(): void;
}

interface Scanner {
  scan(): void;
}

class AllInOnePrinter implements Printer, Scanner {
  print() {
    console.log('Printing...');
  }
  scan() {
    console.log('Scanning...');
  }
}

class SimplePrinter implements Printer {
  print() {
    console.log('Just printing...');
  }
}
```

- Each class implements **only** what it needs.
- No unused methods forced by a bloated interface.

## **D - Dependency Inversion Principle (DIP)**

1. High-level modules should not depend on low-level modules. Both should depend on abstractions.
2. This principle aims to reduce coupling between modules, increase modularity, and make the code easier to maintain, test, and extend

### Example:

```ts
interface Database {
  save(data: string): void;
}

class MySQLDatabase implements Database {
  save(data: string) {
    console.log(`Saving to MySQL: ${data}`);
  }
}

class App {
  constructor(private db: Database) {}

  saveData(data: string) {
    this.db.save(data);
  }
}
```

- `App` depends on the **abstraction** `Database`, not on the concrete class `MySQLDatabase`.
- You can switch to `MongoDatabase` or `PostgresDatabase` without changing the `App`.

### Bad Example (Violates DIP):

```ts
// Low-level module
class MySQLDatabase {
  save(data: string) {
    console.log(`Saving to MySQL: ${data}`);
  }
}

// High-level module directly depends on low-level module
class App {
  private db: MySQLDatabase;

  constructor() {
    this.db = new MySQLDatabase(); // tightly coupled
  }

  saveData(data: string) {
    this.db.save(data);
  }
}
```

- `App` directly depends on the concrete class `MySQLDatabase`.
- You **cannot switch** to another database without modifying the `App` class.
- This violates the **Dependency Inversion Principle** because there's no abstraction in between.
