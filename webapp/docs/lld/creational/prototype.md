# Prototype Pattern

- It is a **Creational design pattern** that lets you create new objects by cloning existing ones, instead of instantiating them from scratch.

## Useful in situations

- When creating a new object is expensive, time-consuming and resource intensive.
- Want to avoid complex object initialization logic.

## Challenges of Cloning Objects

- Create a new object and manually copy each field from the original object to new one.
- Most of the systems many field are **private** or **hidden behind encapsulation** that means our cloning logic can't access them directly.
- Even if we can access all properties still we need to know **concrete class** of the object to instantiate a copy.

## One small solution

- Instead of copying object manually we let object know how to clone it, object will have a function like copy or clone in itself.
- It has many benefits
  - No need to know concrete class
  - presevers encapsulation

## Problem

- Suppose we have a enemy class and we need to spawn it in some code so we will do it like

```
// type speed ... weaponType
Enemy flying1 = new Enemy("Flying", 100, 10.5, false, "Laser");
Enemy flying2 = new Enemy("Flying", 100, 10.5, false, "Laser");
```

- To solve above problem we will use the prototype pattern.

## The Prototype pattern

- The Prototype pattern specifies the kinds of objects to create using a prototypical instance and creates new objects by copying (cloning) this prototype.

### Class Diagram

<p align="center">
    <img src="/img/lld/prototype.png"/>
</p>

- Defines the **Prototype Interface** declares a `clone` method which concrete class should implement.
- Implement **Concrete Prototypes** which will implement logic to clone itself.
- When **Client** need it just ask object to clone itself.

#### Optional : with registry

<p align="center">
    <img src="/img/lld/prototype-with-registry.png"/>
</p>

- As our system grows with more enemy types or components, we can introduce a Prototype Registry:
  - It stores a collection of named prototypes (e.g., "flying", "armored").
  - Clients request a clone of a named prototype using a simple key.
  - This separates the concern of what to create from how to configure it.

### Implementation

#### Define Prototype

```java
public interface EnemyPrototype {
    EnemyProtype clone();
}
```

#### Create Concrete Class

```java
public class Enemy implements EnemyPrototype {
  private String type;
  private int health;
  private double speed;
  private boolean armoured;
  private String weapon;

  public Enemy(...takes all variable){
    ...assign variable to local ones
  }

  @Override
  Enemy clone(){
    return new Enemy(type,health,speed,armoured,weapon);
  }

  .... remaining methods

}
```

- while we are cloning we can have issue with cloning if have some non-primitive types to clone.
  - **Shallow copy**
    - It’s fine if all fields are primitives or immutable (like String). But if Enemy had a field like a List, both the original and cloned enemies would share the same list object, which can cause subtle bugs.
  - **Deep copy**
    - If your object contains mutable reference types, you should create a deep copy in the copy constructor. For e.g.:
    ```
    this.inventory = new ArrayList<>();
    for (Item item : source.inventory) {
        this.inventory.add(item.clone()); // Assuming Item also implements clone()
    }
    ```

#### Optional: Creat registry

- stores pre-configured prototype instances. This keeps your code organized, especially when you have many types of enemies.

```java
public class EnemyRegistry {
  private Map<String,Enemy> prototypes = new HashMap<>();

  public void register(String key, Enemy prototype){
    prototype.push(key,prototype);
  }

  public Enemy get(String key){
    Enemy prototype = prototypes.get(key);
    if(prototype!=null){
      return prototype.clone();
    }
    // throw for no registered exception
  }
}
```

#### Copy with use of registry

```java
public class Game {
  psvm(...){
    EnemyRegistry registry = new EnemyRegistry();

    registry.register("flying",new Enemy(..enemy with all input));
    registry.register(...go for armored);

    // Clone

    Enemy e1 = registry.get("flying");
    Enemy e2 = registry.get("armored");


    ... we can use further
  }
}
```
