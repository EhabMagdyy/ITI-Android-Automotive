# Java Fundamentals Guide

## Table of Contents

* [1. Java Overview](#1-java-overview)
* [2. Java Program Structure](#2-java-program-structure)
* [3. Compilation and Execution Flow](#3-compilation-and-execution-flow)
* [4. Classes and Objects](#4-classes-and-objects)
* [5. Packages](#5-packages)
* [6. Access Modifiers](#6-access-modifiers)
  * [public](#public)
  * [private](#private)
  * [protected](#protected)
  * [default](#default)
* [7. Variables and Data Types](#7-variables-and-data-types)
* [8. Type Casting](#8-type-casting)
* [9. Constants](#9-constants)
* [10. Operators](#10-operators)
* [11. Input and Output](#11-input-and-output)
  * [Printing](#printing)
  * [Scanner](#scanner)
* [12. Conditional Statements](#12-conditional-statements)
  * [if](#if)
  * [if-else](#if-else)
  * [switch](#switch)
* [13. Loops](#13-loops)
  * [for loop](#for-loop)
  * [while loop](#while-loop)
  * [do-while loop](#do-while-loop)
* [14. Methods](#14-methods)
* [15. Arrays](#15-arrays)
* [16. Strings](#16-strings)
* [17. Exception Handling](#17-exception-handling)
  * [1. What is an Exception?](#1-what-is-an-exception)
  * [2. Why Exception Handling is Needed](#2-why-exception-handling-is-needed)
  * [3. Exception Hierarchy](#3-exception-hierarchy)
  * [4. try Block](#4-try-block)
  * [5. catch Block](#5-catch-block)
  * [6. Exception Object](#6-exception-object)
  * [7. Multiple catch Blocks](#7-multiple-catch-blocks)
  * [8. Catch Order Rules](#8-catch-order-rules)
  * [9. finally Block](#9-finally-block)
  * [10. throw Keyword](#10-throw-keyword)
  * [11. throws Keyword](#11-throws-keyword)
  * [12. Checked Exceptions](#12-checked-exceptions)
  * [13. Unchecked Exceptions](#13-unchecked-exceptions)
  * [14. Common Exception Types](#14-common-exception-types)
  * [15. Custom Exceptions](#15-custom-exceptions)
  * [16. Exception Handling Best Practices](#16-exception-handling-best-practices)
  * [17. Exception Handling Flow Summary](#17-exception-handling-flow-summary)

* [18. Static Keyword](#18-static-keyword)
* [19. final Keyword](#19-final-keyword)
* [20. Object-Oriented Programming Basics](#20-object-oriented-programming-basics)
  * [Inheritance](#inheritance)
  * [Polymorphism](#polymorphism)
  * [Encapsulation](#encapsulation)
  * [Abstraction](#abstraction)

---

## 1. Java Overview

Java is a high-level, object-oriented programming language.

Java code is compiled into bytecode, then executed by the JVM.

Flow:

```
 Java Source (.java)
        |
        v
Java Compiler (javac)
        |
        v
  Bytecode (.class)
        |
        v
       JVM
        |
        v
  Machine Code
        |
        v
       CPU
```

Android uses a similar idea:

```
 Java/Kotlin
      |
 DEX Bytecode
      |
 ART Runtime
      |
     CPU
```

---

## 2. Java Program Structure

Example:

```java
public class Main {

    public static void main(String[] args) {

        System.out.println("Hello Java");

    }

}
```

### Class

```java
public class Main
```

Defines a class.

A class contains:

* variables
* methods
* logic

### main()

```java
public static void main(String[] args)
```

The entry point of a Java application.

The JVM starts execution from this method.

---

## 3. Compilation and Execution Flow

Compile:

```bash
javac Main.java
```

Output:

```
Main.class
```

Run:

```bash
java Main
```

The JVM loads:

```
Main.class
```

and executes:

```
main()
```

---

## 4. Classes and Objects

A class is a blueprint.

Example:

```java
class Car {

    String color;

    void drive(){

        System.out.println("Driving");

    }

}
```

Object:

```java
Car myCar = new Car();
```

Memory:

```
Class
 |
 |
Object created in memory
```

---

## 5. Packages

A package is a namespace used to organize classes.

Example:

```java
package ehab.calculator;
```

Means:

```
ehab
 |
 calculator
       |
       Calculator.java
```

Folder structure:

```
src/

 └── ehab/

      └── calculator/

             └── Calculator.java
```

Full class name:

```
ehab.calculator.Calculator
```

Import:

```java
import ehab.calculator.Calculator;
```

---

## 6. Access Modifiers

Access modifiers control visibility.

### public

Accessible everywhere.

Example:

```java
public class Main {

}
```

Used when external code needs access.

---

### private

Accessible only inside the same class.

Example:

```java
class Bank {

    private int balance;

}
```

Cannot:

```java
bank.balance = 100;
```

---

### protected

Accessible by:

* same package
* child classes

Example:

```java
class Animal {

    protected String name;

}
```

---

### default

No keyword.

Example:

```java
class Test {

}
```

Accessible only inside the same package.

---

Visibility:

| Modifier  | Same Class | Package | Child | Everywhere |
| --------- | ---------- | ------- | ----- | ---------- |
| private   | Yes        | No      | No    | No         |
| default   | Yes        | Yes     | No    | No         |
| protected | Yes        | Yes     | Yes   | No         |
| public    | Yes        | Yes     | Yes   | Yes        |

---

## 7. Variables and Data Types

A variable stores data.

Example:

```java
int age = 20;
```

---

### Primitive Types

#### Integer

```java
int x = 10;
```

#### Decimal

```java
double price = 10.5;
```

#### Character

```java
char grade = 'A';
```

#### Boolean

```java
boolean active = true;
```

---

### Reference Types

Example:

```java
String name = "Ehab";
```

Objects are reference types.

---

## 8. Type Casting

Converting between types.

### Automatic casting

Small → large

```java
int x = 10;

double y = x;
```

---

### Manual casting

Large → small

```java
double x = 10.5;

int y = (int)x;
```

---

## 9. Constants

Use:

```java
final
```

Example:

```java
final double PI = 3.14;
```

Cannot change:

```java
PI = 5;
```

---

## 10. Operators

### Arithmetic

```java
+
-
*
/
%
```

Example:

```java
int result = a + b;
```

---

### Comparison

```java
==
!=
>
<
>=
<=
```

---

### Logical

```java
&&
||
!
```

Example:

```java
if(age > 18 && active)
{

}
```

---

## 11. Input and Output

### Printing

```java
System.out.println("Hello");
```

Print with new line.

```java
System.out.print("Hello");
```

No new line.

---

### Scanner

Import:

```java
import java.util.Scanner;
```

Create:

```java
Scanner input = new Scanner(System.in);
```

Read:

```java
int x = input.nextInt();

String name = input.nextLine();
```

---

## 12. Conditional Statements

### if

```java
if(x > 10)
{

}
```

---

### if-else

```java
if(x > 10)
{

}
else
{

}
```

---

### switch

Used for multiple choices.

```java
switch(operator)
{

case '+':
    result=a+b;
    break;


case '-':
    result=a-b;
    break;

}
```

---

## 13. Loops

### for loop

Used when number of iterations is known.

```java
for(int i=0;i<5;i++)
{

}
```

---

### while loop

Runs while condition is true.

```java
while(x < 10)
{

}
```

---

### do-while

Runs at least once.

```java
do{

}
while(condition);
```

---

## 14. Methods

Methods are functions.

Example:

```java
public int add(int a,int b)
{

    return a+b;

}
```

Call:

```java
add(5,3);
```

---

## 15. Arrays

Store multiple values.

Example:

```java
int numbers[] = {1,2,3};
```

Access:

```java
numbers[0];
```

---

## 16. Strings

Example:

```java
String name="Java";
```

Common methods:

```java
name.length();

name.equals("Java");

name.toUpperCase();
```

---

## 17. Exception Handling

### 1. What is an Exception?

An exception is an event that happens during program execution that interrupts the normal flow of the program.

Examples:

- Division by zero
- File not found
- Invalid array index
- Null object access
- Network failure


Example:

```java
int result = 10 / 0;
```

Output:

```
ArithmeticException
```

The program stops because an error occurred.


---

### 2. Why Exception Handling is Needed

Without exception handling:

```
Program
   |
   |
Error happens
   |
   |
Program crashes
```

With exception handling:

```
Program
   |
   |
Error happens
   |
   |
catch block handles it
   |
   |
Program continues
```


Benefits:

- Prevent program crashes
- Display meaningful messages
- Recover from errors
- Release resources safely


---

### 3. Exception Hierarchy

All exceptions are objects.

Java exception hierarchy:

```
Throwable
    |
    |
    +----------------+
    |                |
 Error           Exception
                     |
                     |
        +------------+-------------+
        |            |             |
 Arithmetic    IOException    RuntimeException
 Exception
```


#### Throwable

The parent class for all errors and exceptions.


#### Exception

Represents problems that applications can handle.


#### Error

Represents serious system problems.

Example:

```
OutOfMemoryError
StackOverflowError
```


---

### 4. try Block

The try block contains code that may cause an exception.

Syntax:

```java
try {

    // risky code

}
```

Example:

```java
try {

    int x = 10 / 0;

}
```

If no error happens:

```
try executes normally
```


If an error happens:

```
try stops
catch executes
```


---

### 5. catch Block

The catch block handles exceptions.

Syntax:

```java
try {

}
catch(Exception e){

}
```


Example:

```java
try {

    int result = 10 / 0;

}

catch(Exception e){

    System.out.println("Error happened");

}
```


Output:

```
Error happened
```


---

### 6. Exception Object

The variable inside catch stores information about the error.

Example:

```java
catch(Exception e)
{

}
```


`e` contains:

- Exception type
- Error message
- Stack trace


Print exception:

```java
System.out.println(e);
```


Example output:

```
java.lang.ArithmeticException: / by zero
```


Get only message:

```java
e.getMessage();
```


Print stack trace:

```java
e.printStackTrace();
```


---

### 7. Multiple Catch Blocks

A try block can have multiple catches.

Example:

```java
try {

    int x = array[10];

}

catch(ArrayIndexOutOfBoundsException e){

    System.out.println("Wrong index");

}

catch(Exception e){

    System.out.println("General error");

}
```


Different exceptions can have different handling.


---

### 8. Catch Order Rules

Order is important.

Wrong:

```java
try {

}

catch(Exception e)
{

}

catch(IOException e)
{

}
```


Why?

Because:

```
Exception
    |
    |
 IOException
```

The first catch already catches everything.


Correct:

```java
try {

}

catch(IOException e)
{

}

catch(Exception e)
{

}
```


Rule:

```
Specific exceptions first

General exceptions last
```


Example:

```
NullPointerException
        |
ArithmeticException
        |
Exception
```


Correct order:

```
NullPointerException

ArithmeticException

Exception
```


---

### 9. finally Block

The finally block always executes.

Syntax:

```java
try {

}

catch(Exception e){

}

finally {

}
```


Example:

```java
try {

    System.out.println("Try");

}

catch(Exception e){

    System.out.println("Catch");

}

finally {

    System.out.println("Finally");

}
```


Output:

```
Try
Finally
```


Even if an exception happens:

```
Catch
Finally
```


---

### Why use finally?

Used for cleanup:

- Close files
- Close database connections
- Release resources


Example:

```java
File file = null;

try {

    file = openFile();

}

finally {

    closeFile(file);

}
```


---

### 10. throw Keyword

`throw` is used to manually create an exception.


Example:

```java
if(age < 18)
{

    throw new Exception("Not allowed");

}
```


Meaning:

```
Create an error here
```


---

### 11. throws Keyword

`throws` declares that a method may generate an exception.


Example:

```java
public void readFile()
throws IOException
{

}
```


Meaning:

```
This function may fail.
Caller must handle it.
```


Example:

```java
try {

    readFile();

}

catch(IOException e)
{

}
```


---

### 12. Checked Exceptions

Checked exceptions are checked during compilation.


The compiler forces handling them.


Examples:

```
IOException
SQLException
FileNotFoundException
```


Example:

```java
try {

    FileReader file =
    new FileReader("data.txt");

}

catch(IOException e)
{

}
```


---

### 13. Unchecked Exceptions

Unchecked exceptions happen during runtime.


They are subclasses of RuntimeException.


Examples:

```
ArithmeticException

NullPointerException

ArrayIndexOutOfBoundsException

InputMismatchException
```


Example:

```java
int x = 10 / 0;
```


The compiler does not force handling.


---

### 14. Common Exception Types


#### ArithmeticException

Math error.

Example:

```java
10 / 0;
```


---

#### NullPointerException

Using an object that is null.


Example:

```java
String name = null;

name.length();
```


---

#### ArrayIndexOutOfBoundsException

Invalid array index.


Example:

```java
int a[] = {1,2};

a[5];
```


---

#### InputMismatchException

Wrong Scanner input.


Example:

```java
int x = scanner.nextInt();
```

User enters:

```
hello
```


---

#### IOException

Input/output failure.

Example:

- File operations
- Network operations


---

### 15. Custom Exceptions

You can create your own exception type.


Example:

```java
class AgeException extends Exception
{

    AgeException(String message)
    {

        super(message);

    }

}
```


Use:

```java
throw new AgeException("Invalid age");
```


---

### 16. Exception Handling Best Practices


#### Do not catch everything blindly

Bad:

```java
catch(Exception e)
{

}
```


Better:

```java
catch(IOException e)
{

}
```


Handle the exact problem.


---

#### Do not ignore exceptions


Bad:

```java
catch(Exception e)
{

}
```


The error disappears.


---

#### Always release resources

Use:

- finally
- try-with-resources


Example:

```java
try(FileReader f = new FileReader("a.txt"))
{

}
```


---

### 17. Exception Handling Flow Summary


Normal case:

```
try

 |

No exception

 |

Continue program
```


Error case:

```
try

 |

Exception happens

 |

Search matching catch

 |

Execute catch

 |

finally executes

 |

Continue
```


Full structure:

```
try
 |
 |
 +---- success -----> continue
 |
 |
 +---- exception
              |
              v
          catch block
              |
              v
          finally block
              |
              v
          continue
```


### Final Notes

Exception handling is used everywhere in Java:

- Android applications
- File handling
- Networking
- Databases
- User input
- Hardware communication

The main keywords are:

```
try
catch
finally
throw
throws
```

Understanding exceptions is essential before working with large Java applications.
```

---

## 18. Static Keyword

Static belongs to the class, not object.

Example:

```java
class Test{

static int counter;

}
```

Access:

```java
Test.counter;
```

---

## 19. final Keyword

Prevents modification.

Variable:

```java
final int x=10;
```

Method:

```java
final void print()
{

}
```

Class:

```java
final class Test
{

}
```

---

## 20. Object-Oriented Programming Basics

### Encapsulation

Hide internal data.

Example:

```java
private int balance;
```

Access using methods.

---

### Inheritance

One class inherits another.

Example:

```java
class Dog extends Animal
{

}
```

---

### Polymorphism

Same interface, different behavior.

Example:

```java
Animal a = new Dog();
```

---

### Abstraction

Hide implementation details.

Example:

```java
abstract class Shape
{

}
```

---

## Java Learning Order

Recommended order:

```
1. Syntax
2. Variables
3. Operators
4. Conditions
5. Loops
6. Methods
7. Arrays
8. Classes
9. Objects
10. Packages
11. Access modifiers
12. OOP
13. Exceptions
```

This is enough foundation before moving into Android Java/Kotlin development.
