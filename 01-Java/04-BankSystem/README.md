# Bank System — Java OOP Project

A simple console-based bank system built with Java, demonstrating core Object-Oriented Programming principles: **encapsulation**, **polymorphism**, and **composition**.

---

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | **Deposit** | Add money to an account |
| 2 | **Withdraw** | Remove money from an account (with balance check) |
| 3 | **Check Balance** | View account ID, owner name, and current balance |
| 4 | **Transfer** | Move money between two accounts |
| 5 | **Transaction History** | View all deposits, withdrawals, and transfers with timestamps |
| 6 | **Create / Remove Account** | Open a new account or close an existing one (must have zero balance) |

---

## Architecture

```
┌─────────────────┐
│   BankSystem    │  ← Console UI / Controller
│   (main class)  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐     ┌─────────────────┐
│ AccountManager  │────►│     Account     │
│  (Factory +     │     │   (Entity)      │
│   Registry)     │     ├─────────────────┤
│                 │     │ - accountId       │
│ - accounts: Map │     │ - ownerName       │
│ - nextId: int   │     │ - balance         │
├─────────────────┤     │ - pin             │
│ + createAccount │     │ - transactions    │
│ + removeAccount │     ├─────────────────┤
│ + getAccount()  │     │ + deposit()     │
│ + listAccounts()│     │ + withdraw()     │
└─────────────────┘     │ + transferTo()   │
                        │ + getBalance()   │
                        │ + getHistory()   │
                        └────────┬────────┘
                                 │
                                 ▼
                        ┌─────────────────┐
                        │   Transaction   │
                        │   (Entity)      │
                        ├─────────────────┤
                        │ - type (enum)   │
                        │ - amount        │
                        │ - timestamp     │
                        └─────────────────┘
```

---

## Classes

### `BankSystem`
- Entry point with `main()` method
- Displays menu and handles user input
- Delegates all business logic to other classes

### `AccountManager`
- Central registry for all accounts
- Stores accounts in a `HashMap<String, Account>`
- Auto-generates account IDs (ACC1000, ACC1001, ...)
- Handles account creation and removal

### `Account`
- Core entity representing a bank account
- Encapsulates balance, PIN, and transaction history
- Methods: `deposit()`, `withdraw()`, `transferTo()`
- Validates PIN before any sensitive operation

### `Transaction`
- Immutable record of every money movement
- Fields: type (enum), amount, timestamp
- Types: `DEPOSIT`, `WITHDRAW`, `TRANSFER_OUT`, `TRANSFER_IN`

### `TransactionType` (enum)
- Defines the four possible transaction types
- Used for categorizing and displaying transactions

---

## Design Patterns Used

| Pattern | Where | Why |
|---------|-------|-----|
| **Factory** | `AccountManager.createAccount()` | Centralized object creation, auto-generates IDs |
| **Registry** | `AccountManager` HashMap | Lookup accounts by ID in O(1) time |

---

## OOP Principles Demonstrated

| Principle | Application |
|-----------|-------------|
| **Encapsulation** | All fields are `private`; accessed via getters/setters |
| **Composition** | `Account` has a `List<Transaction>`; `AccountManager` has a `Map` of `Account`s |
| **Single Responsibility** | Each class has one job: `Account` holds data, `AccountManager` manages registry, `BankSystem` handles UI |
| **Information Hiding** | PIN is never exposed; `validatePin()` checks internally |

---

## Input Validation

All user inputs are protected with `try-catch`:

- **Integer input** (`getIntInput`): Catches `InputMismatchException`, consumes invalid token, prompts again
- **Double input** (`getDoubleInput`): Same pattern for monetary amounts
- **String input** (`getStringInput`): Rejects empty strings

```java
private int getIntInput(String prompt) {
    while (true) {
        try {
            System.out.print(prompt);
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid integer.");
            scanner.nextLine(); // consume invalid token
        }
    }
}
```

---

## Sample Run

```
>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 6

1. Create Account
2. Remove Account
3. List All Accounts
Enter choice: 1
Enter owner name: Ehab
Set PIN: 1234
Account created: ACC1000

Enter choice: 1
Enter account ID: ACC1000
Enter PIN: 1234
Enter amount to deposit: 500
Deposited successfully. New balance: $500.0

Enter choice: 4
Enter account ID: ACC1000
Enter PIN: 1234
Enter target account ID: ACC1001
Enter amount to transfer: 200
Transferred successfully.
Your new balance: $300.0

Enter choice: 5
Enter account ID: ACC1000
Enter PIN: 1234

--- Transaction History ---
2026-06-22T10:30:15.234 | DEPOSIT | $500.0
2026-06-22T10:31:42.891 | TRANSFER_OUT | $200.0
```

---

## How to Run

```bash
javac BankSystem.java
java BankSystem
```

> All classes are in a single file. Only `BankSystem` is `public`, matching the filename.

---

## Running the system
```log
>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 6

1. Create Account
2. Remove Account
3. List All Accounts
Enter choice: 1
Enter owner name: Ehab
Set PIN: 124
Account created: ACC1000

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 6

1. Create Account
2. Remove Account
3. List All Accounts
Enter choice: 1
Enter owner name: Ahmed
Set PIN: 123
Account created: ACC1001

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 1
Enter account ID: ACC1000
Enter PIN: 124
Enter amount to deposit: 2500
Deposited successfully. New balance: $2500.0

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 5
Enter account ID: ACC1000
Enter PIN: 124

--- Transaction History ---
2026-06-22T03:00:22.762832599 | DEPOSIT | $2500.0

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 3
Enter account ID: ACC1000
Enter PIN: 124
Account: ACC1000
Owner: Ehab
Balance: $2500.0

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 4
Enter account ID: ACC1000
Enter PIN: 124
Enter target account ID: ACC1001
Enter amount to transfer: 800
Transferred successfully.
Your new balance: $1700.0

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 5
Enter account ID: ACC1001
Enter PIN: 124
Error: Invalid PIN

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 5
Enter account ID: ACC1001
Enter PIN: 123

--- Transaction History ---
2026-06-22T03:03:03.782855500 | TRANSFER_IN | $800.0

>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Create / Remove Account
0. Exit
========================================
Enter choice: 
```