# Java Calculator with Factory Design Pattern

## Project Overview

A simple console calculator that uses the **Factory Design Pattern** to decouple operation creation from execution. The calculator supports `+`, `-`, `*`, and `/` operations.

---

## Class Diagram

```
┌─────────────────────┐
│    Calculator       │
├─────────────────────┤
│ + compute(a,b,op)   │────────┐
│ + main(args)        │        │
└─────────────────────┘        │
                               │
                               ▼
                    ┌─────────────────────┐
                    │   OperationFactory  │
                    ├─────────────────────┤
                    │ - operationMap      │
                    ├─────────────────────┤
                    │ + getOperation(op)  │
                    │ + isValidOperator() │
                    └─────────────────────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
              ▼                ▼                ▼
     ┌─────────────┐  ┌─────────────┐  ┌─────────────┐
     │  Operation  │  │  Operation  │  │  Operation  │
     │ (interface) │  │ (interface) │  │ (interface) │
     └──────┬──────┘  └──────┬──────┘  └──────┬──────┘
            │                │                │
     ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐
     │AddOperation │  │SubOperation │  │MultiplyOp   │
     │+ calculate()│  │+ calculate()│  │+ calculate()│
     └─────────────┘  └─────────────┘  └─────────────┘
```

---

## 1. The `Operation` Interface

```java
interface Operation {
    double calculate(double a, double b);
}
```

### What it does
Defines a **contract** that all operations must follow. Every operation class must implement the `calculate(a, b)` method.

### Why use an interface?
- **Polymorphism**: The factory returns `Operation` type, but the actual object could be `AddOperation`, `SubOperation`, etc.
- **Decoupling**: `Calculator` only knows about `Operation`, not concrete classes.
- **Extensibility**: Add new operations without changing existing code.

---

## 2. Concrete Operation Classes

### `AddOperation`
```java
class AddOperation implements Operation {
    @Override
    public double calculate(double a, double b) {
        return a + b;
    }
}
```

### `SubOperation`
```java
class SubOperation implements Operation {
    @Override
    public double calculate(double a, double b) {
        return a - b;
    }
}
```

### `MultiplyOperation`
```java
class MultiplyOperation implements Operation {
    @Override
    public double calculate(double a, double b) {
        return a * b;
    }
}
```

### `DivideOperation`
```java
class DivideOperation implements Operation {
    @Override
    public double calculate(double a, double b) {
        if (b == 0)
            throw new ArithmeticException("Divide by zero");
        return a / b;
    }
}
```

### Key Points
- Each class **encapsulates** one operation's logic
- `@Override` ensures the method correctly implements the interface
- `DivideOperation` includes validation (no division by zero)

---

## 3. The `OperationFactory` — Heart of the Pattern

```java
class OperationFactory {
    private static final Map<String, Operation> operationMap = new HashMap<>();

    // Static initializer block
    static {
        operationMap.put("+", new AddOperation());
        operationMap.put("-", new SubOperation());
        operationMap.put("*", new MultiplyOperation());
        operationMap.put("/", new DivideOperation());
    }

    public static Operation getOperation(String operator) {
        Operation op = operationMap.get(operator);
        if (op == null) {
            throw new IllegalArgumentException("Unknown operator: " + operator);
        }
        return op;
    }

    public static boolean isValidOperator(String operator) {
        return operationMap.containsKey(operator);
    }
}
```

### 3.1 The HashMap

```java
private static final Map<String, Operation> operationMap = new HashMap<>();
```

| Modifier | Meaning |
|----------|---------|
| `private` | Only `OperationFactory` can access the map |
| `static` | One map shared across all uses (belongs to class, not instances) |
| `final` | Reference cannot be reassigned after initialization |
| `Map<String, Operation>` | Keys are `String` operators, values are `Operation` objects |
| `HashMap<>` | Implementation: fast O(1) average lookup time |

**The Map acts as a lookup table:**

| Key | Value (Object) |
|-----|----------------|
| `"+"` | `AddOperation` instance |
| `"-"` | `SubOperation` instance |
| `"*"` | `MultiplyOperation` instance |
| `"/"` | `DivideOperation` instance |

### 3.2 Static Initializer Block

```java
static {
    operationMap.put("+", new AddOperation());
    operationMap.put("-", new SubOperation());
    operationMap.put("*", new MultiplyOperation());
    operationMap.put("/", new DivideOperation());
}
```

**What is it?**
A block of code marked with `static` that runs **once** when the class is first loaded by the JVM.

**When does it run?**
```
JVM loads OperationFactory class
        ↓
    static block executes (AUTOMATICALLY, ONCE)
        ↓
    operationMap is now populated
        ↓
    getOperation() / isValidOperator() can be called
```

**Why not initialize in declaration?**
You cannot call methods like `.put()` inside a field declaration. The static block provides a place to run setup code.

### 3.3 `getOperation()` — Factory Method

```java
public static Operation getOperation(String operator) {
    Operation op = operationMap.get(operator);  // O(1) lookup
    if (op == null) {
        throw new IllegalArgumentException("Unknown operator: " + operator);
    }
    return op;
}
```

**Flow:**
```
Input: "+"
    ↓
operationMap.get("+") → returns AddOperation object
    ↓
Return as Operation type (polymorphism)
    ↓
Caller calls .calculate(5, 3) → 8.0
```

**Why this is powerful:**
- No `switch` or `if-else` chain needed
- Adding a new operation = one `.put()` line
- Lookup is O(1) — constant time

### 3.4 `isValidOperator()` — Validation

```java
public static boolean isValidOperator(String operator) {
    return operationMap.containsKey(operator);
}
```

Checks if the operator exists in the map before attempting to use it. Returns `true` or `false`.

---

## 4. The `Calculator` Class

```java
public class Calculator {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        double a = scanner.nextDouble();

        System.out.print("Enter operator(+, -, *, /): ");
        String op = scanner.next();

        System.out.print("Enter second number: ");
        double b = scanner.nextDouble();

        try {
            double result = calc.compute(a, b, op);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    public double compute(double a, double b, String operator) {
        if (!OperationFactory.isValidOperator(operator)) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        }
        Operation operation = OperationFactory.getOperation(operator);
        return operation.calculate(a, b);
    }
}
```

### Execution Flow

```
User Input: 10, "+", 5
    ↓
compute(10, 5, "+")
    ↓
isValidOperator("+") → true
    ↓
getOperation("+") → returns AddOperation object
    ↓
operation.calculate(10, 5) → 15.0
    ↓
Print: "Result: 15.0"
```

---

## 5. Why Factory Pattern?

| Without Factory | With Factory |
|-----------------|--------------|
| `switch(operator)` in multiple places | Single map lookup |
| Adding operation = modifying Calculator | Adding operation = one `.put()` line |
| Calculator knows all concrete classes | Calculator only knows `Operation` interface |
| Hard to test | Easy to mock `Operation` |

---

## 6. Adding a New Operation (Example)

To add a power operation (`^`):

**Step 1:** Create the class
```java
class PowerOperation implements Operation {
    @Override
    public double calculate(double a, double b) {
        return Math.pow(a, b);
    }
}
```

**Step 2:** Add to factory map
```java
static {
    // ... existing entries
    operationMap.put("^", new PowerOperation());
}
```

**Done.** No changes to `Calculator` or any other class. This is the **Open/Closed Principle** in action.

---

## 7. Key Concepts Summary

| Concept | Explanation |
|---------|-------------|
| **Interface** | Contract defining what an operation can do |
| **Polymorphism** | Same `calculate()` call, different behavior |
| **Encapsulation** | Each operation's logic is hidden in its own class |
| **Factory Pattern** | Centralized object creation via `OperationFactory` |
| **HashMap** | Key-value store for O(1) operator lookup |
| **Static Block** | One-time initialization when class loads |
| **Open/Closed** | Open for extension, closed for modification |

---

## 8. Full File Structure

```
Calculator.java
├── public class Calculator
│   └── main() + compute()
├── interface Operation
├── class AddOperation
├── class SubOperation
├── class MultiplyOperation
├── class DivideOperation
└── class OperationFactory
    └── static HashMap + factory methods
```

> **Note:** Only one `public` class per `.java` file is allowed in Java, and it must match the filename. All other classes are package-private (no access modifier).