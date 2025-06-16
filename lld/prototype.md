# Builder Pattern

- It is a **Creational design pattern** that lets you create new objects by cloning existing ones, instead of instantiating them from scratch.

## Useful in situations

- When creating a new object is expensive, time-consuming and resource intensive.
- Want to avoid complex object initialization logic.

## Challenges of Cloning Objects

- Create a new object and manually copy each field from the original object to new one.
- Most of the systems many field are **private** or **hidden behind encapsulation** that means our cloning logic can't access them directly.
- Even if we can access all properties still we need to know **concrete class** of the object to instantiate a copy.
