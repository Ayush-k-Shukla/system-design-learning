# OOP

## Encapsulation

1. Hide internal details and expose only what is necessary.
2. Ususally define getter and setter for a value instead of fetching it directly for a class.
3. **Benefit**
   1. Protects internal state and maintains control over how it's accessed or modified.

```sh
class BankAccount {
  private balance: number;

  constructor(initialBalance: number) {
    this.balance = initialBalance;
  }

  deposit(amount: number) {
    this.balance += amount;
  }

  getBalance(): number {
    return this.balance;
  }
}
```

## Inheritance

1. Allows a class to inherit properties and methods from another class.
2. **Types**
   1. **Single**
      1. One child class inherits from one parent class.
      2. class a extends b
   2. **Multilevel**
      1. A class inherits from a derived class which itself inherited from another.
      2. class a extends b
      3. class c extends a
   3. **Hierarchical**
      1. Multiple classes inherit from the same parent class.
      2. class a extends b
      3. class c extends b
   4. **Multiple (Not supportef in TS, JAVA)**
      1. One class inherits from more than one class — TypeScript/JavaScript do not support this directly due to ambiguity (diamond problem).
      2. class a extends b,c
      3. diamond problem is if both class b and c have same function then will not know on extend which one to preserve.
3. Benefit is it increases code reusability.

## Polymorphism

1.  Polymorphism allows the same method name to behave differently depending on the class.
2.  Makes code extensible and flexible for future changes.
3.  **Types**

    1. **Compile Time(Function overloading)**

       1. Same method name with different parameters in the same class.

       ```sh
           class MathUtils {
                   int add(int a, int b) {
                       return a + b;
                   }

                   double add(double a, double b) {
                       return a + b;
                   }

                   String add(String a, String b) {
                       return a + b;
                   }
           }
       ```

    2. **Run Time(Method overloading)**

       1. Subclasses override parent class methods.

       ```sh

       class Animal {
           void sound() {
               System.out.println("Animal makes sound");
           }
       }

       class Dog extends Animal {
           @Override
           void sound() {
               System.out.println("Dog barks");
           }
       }

       public class Main {
           public static void main(String[] args) {
               Animal a = new Dog(); // Reference of parent, object of child
               a.sound(); // Output: Dog barks (child method is called)
           }
       }

       ```

## Abstraction

1. Defines what an object does, not how it does it.
2. Hides implementation details and exposes only the interface.

```sh
abstract class Shape {
  abstract getArea(): number;
}

class Circle extends Shape {
  constructor(private radius: number) {
    super();
  }

  getArea(): number {
    return Math.PI * this.radius * this.radius;
  }
}

```
