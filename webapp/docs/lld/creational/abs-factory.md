# Abstract Factory Design Pattern

- It is a creational design pattern that provides an interface for creating families of related or dependent objects — without specifying their concrete classes.
- It encapsulates object creation in factory interface.

## Initial problem we have

- We are building a cross platform desktop application that must support both windows and macos. some staring dev will be like

```
class WindowBtn {
    // Implementation of onclick and other functions
}

class WindowCheckBox {
    // Implementation of onclick and other functions
}

class LinuxBtn {
    // Implementation of onclick and other functions
}

class LinuxCheckBox {
    // Implementation of onclick and other functions
}


// Use in our app

public class App {
    psvm(...args){
        String os = System.getProperty("os.name");

        if(os.contains("Windows)){
            WindowBtn btn = new WindowBtn();
            WindowCheckBox box = new WindowCheckBox();
            // perform remaining actions
        }else if(os.contains("Linux)){
            LinuxBtn btn = new LinuxBtn();
            LinuxCheckBox box = new LinuxCheckBox();
            // perform remaining actions
        }
    }
}
```

- In this above approch we have multiple issue when our app grows
  - **Tight coupling between Concrete class** so whenever i want to use btn i have to write extra boilerplate.
  - Scalability and violation of Open/Closed

## Abstract Factory Pattern

- In our case, we want to create a family of UI components (like Button, Checkbox, TextField, etc.) that look and behave differently on different platforms — such as Windows or macOS — but expose the same interface to the application.

### Class Diagram

<p align="center">
    <img src="../images/abstract-factory.png"/>
</p>

1. **Abstract Products:** declare interfaces for a set of distinct but related products which make up a product family.
2. **Concrete Products:** are various implementations of abstract products, grouped by variants. Each abstract product (chair/sofa) must be implemented in all given variants (Victorian/Modern).
3. **The Abstract Factory** interface declares a set of methods for creating each of the abstract products.
4. **Concrete Factories** implement creation methods of the abstract factory. Each concrete factory corresponds to a specific variant of products and creates only those product variants.
5. Although concrete factories instantiate concrete products, signatures of their creation methods must return corresponding abstract products. This way the client code that uses a factory doesn’t get coupled to the specific variant of the product it gets from a factory. The Client can work with any concrete factory/product variant, as long as it communicates with their objects via abstract interfaces.

<p align="center">
    <img src="../images/abstract-factory-v2.png"/>
</p>

### Implementation

- **Abstract Product**

```
public interface Btn {
    void print();
    void onClick();
}

public interface CheckBox {
    void print();
    void onClick();
}
```

- **Concrete Products**

```
public class WindowButton implements Btn {
    // override and implement print and onclick fn
}

public class WindowCheckBox implements CheckBox {
    // override and implement print and onclick fn
}

public class LinuxButton implements Btn {
    // override and implement print and onclick fn
}

public class LinuxCheckBox implements CheckBox {
    // override and implement print and onclick fn
}
```

- **Abstract Factory**

```
public interface GUIFactory {
    Btn createButton();
    CheckBox createCheckBox();
}
```

- **Concrete Factory**

```
public class LinuxFactory implements GUIFactory {
    @Override
    public Btn createButton(){
        return new LinuxButton();
    }
    // similar fo checkBox
}

public class WindowFactory implements GUIFactory {
    @Override
    public Btn createButton(){
        return new WindowButton();
    }
    // similar fo checkBox
}
```

- **Client Code**

```
public class App {
    private final Btn button;
    private final CheckBox checkbox;

    public App(GUIFactory factory){
        this.button = factory.createButton();
        this.checkbox = factory.creatCheckBox();
    }

    public void renderUI(){
        button.pain();
        checkbox.paint();
    }
}
```

- **App entry point**

```
public class AppLauncher {
    psvm(...){
        String os = System.getProperty("os.name");
        GUIFactory factory;

        if(os.contains("Windows)){
            factory = new WindowFactory();
        }else if(os.contains("Linux)){
            factory = new LinuxFactory();
        }

        App app = new App(factory);
        app.renderUI();
    }
}
```

- From this new we acheieved
  - Consistency
  - Open/Closed principle
  - Testability and flexibility

## Factory vs Abstract factory

| Feature            | Factory Method                                                | Abstract Factory                                                 |
| ------------------ | ------------------------------------------------------------- | ---------------------------------------------------------------- |
| Purpose            | Creates a single product                                      | Creates families of related products                             |
| Focus              | One product                                                   | Multiple related products (a family)                             |
| Pattern Type       | Creational                                                    | Creational                                                       |
| Product Variants   | Handled by subclasses of a creator class                      | Handled by multiple factory interfaces/classes                   |
| Example            | `Notification` creates `GmailNotification`, `SlackNotif` etc. | `ThemeFactory` creates both `Shape` and `Color` objects          |
| Extensibility      | Easy to add new products                                      | Easy to add new product families                                 |
| Real-world Analogy | Coffee shop serving one drink                                 | Furniture store offering a set: chair, sofa, table of same style |
| Client Knowledge   | Knows which factory to use                                    | Uses a factory to create related products together               |
| Main Benefit       | Delegates instantiation logic                                 | Enforces consistency among product families                      |
| Example Use Case   | Document app creating different file types                    | UI toolkit providing widgets in different themes                 |
