# Bank System — Java OOP Project

A simple console-based bank system built with Java, demonstrating core Object-Oriented Programming principles: **encapsulation**, **polymorphism**, **composition**, and **multithreading**.

---

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | **Create Account** | Register a new account with auto-generated ID |
| 2 | **Login** | Authenticate using Account ID and PIN |
| 3 | **Deposit** | Add money to an account |
| 4 | **Withdraw** | Remove money from an account (with balance check) |
| 5 | **Transfer** | Move money between two accounts |
| 6 | **Check Balance** | View current balance (logged to file via background thread) |
| 7 | **View Transaction History** | Display all transactions with timestamps |
| 8 | **Get Receipt** | Generate full account statement file via background thread |
| 9 | **Logout** | End session and return to welcome screen |

---

## Architecture

```
┌─────────────────────────────────────────────┐
│              BankSystem                      │
│  (Main class - UI Controller + Session)      │
│  - loggedInAccount: Account                  │
│  - showAuthMenu() / showMainMenu()           │
└─────────────────┬───────────────────────────┘
                  │
        ┌─────────┴──────────┐
        ▼                    ▼
┌───────────────┐    ┌─────────────────┐
│   AuthMenu    │    │    MainMenu     │
│  1. Login     │    │  1. Deposit     │
│  2. Register  │    │  2. Withdraw    │
│  0. Exit      │    │  3. Check Balance│
└───────┬───────┘    │  4. Transfer    │
        │            │  5. History     │
        ▼            │  6. Get Receipt │
┌───────────────┐    │  7. Logout      │
│ AccountManager│    └────────┬────────┘
│  (Factory +   │             │
│   Registry)   │             ▼
│  - accounts   │    ┌─────────────────────┐
│  - nextId     │    │  Background Threads │
└───────┬───────┘    │  ┌───────────────┐  │
        │            │  │BalanceChecker │  │
        ▼            │  │   Task        │  │
┌───────────────┐    │  └───────────────┘  │
│    Account    │    │  ┌───────────────┐  │
│   (Entity)    │    │  │ReceiptWriter  │  │
├───────────────┤    │  │   Task        │  │
│ - accountId   │    │  └───────────────┘  │
│ - ownerName   │    └─────────────────────┘
│ - balance     │
│ - pin         │
│ - transactions│
├───────────────┤
│ + deposit()   │
│ + withdraw()  │
│ + transferTo()│
│ + getHistory()│
└───────┬───────┘
        │
        ▼
┌───────────────┐
│  Transaction  │
│   (Entity)    │
├───────────────┤
│ - type (enum) │
│ - amount      │
│ - timestamp   │
└───────────────┘
```

---

## Classes

### `BankSystem`
- Entry point with `main()` method
- Manages login session via `loggedInAccount` field
- Displays **Auth Menu** when logged out, **Main Menu** when logged in
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
- Validates PIN on login (not on every operation)

### `Transaction`
- Immutable record of every money movement
- Fields: type (enum), amount, timestamp
- Types: `DEPOSIT`, `WITHDRAW`, `TRANSFER_OUT`, `TRANSFER_IN`

### `TransactionType` (enum)
- Defines the four possible transaction types
- Used for categorizing and displaying transactions

### `ReceiptWriterTask` (implements `Runnable`)
- Background thread task for generating receipt files
- Takes a **snapshot** of account data at creation time
- Writes formatted receipt to `receipt_ACCxxxx_YYYYMMDD_HHMMSS.txt`

### `BalanceCheckerTask` (implements `Runnable`)
- Background thread task for logging balance checks
- Writes balance snapshot to `balance_log_ACCxxxx_YYYYMMDD_HHMMSS.txt`

---

## Design Patterns Used

| Pattern | Where | Why |
|---------|-------|-----|
| **Factory** | `AccountManager.createAccount()` | Centralized object creation, auto-generates IDs |
| **Registry** | `AccountManager` HashMap | Lookup accounts by ID in O(1) time |
| **Session** | `BankSystem.loggedInAccount` | Maintains authenticated state across operations |

---

## OOP Principles Demonstrated

| Principle | Application |
|-----------|-------------|
| **Encapsulation** | All fields are `private`; accessed via getters/setters |
| **Composition** | `Account` has a `List<Transaction>`; `AccountManager` has a `Map` of `Account`s |
| **Single Responsibility** | Each class has one job: `Account` holds data, `AccountManager` manages registry, `BankSystem` handles UI |
| **Information Hiding** | PIN is never exposed; `validatePin()` checks internally |

---

## Login Session Flow

```
┌─────────────────────────────────────────┐
│         loggedInAccount = null          │
│              → Show Auth Menu            │
└─────────────────────────────────────────┘
                   │
         ┌─────────┴─────────┐
         ▼                   ▼
   ┌──────────┐       ┌──────────┐
   │  Login   │       │ Register │
   │ validate │       │ create   │
   │   PIN    │       │ account  │
   └────┬─────┘       └────┬─────┘
        │                  │
        └────────┬─────────┘
                 ▼
┌─────────────────────────────────────────┐
│      loggedInAccount = account           │
│           → Show Main Menu               │
└─────────────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│  1. Deposit                             │
│  2. Withdraw                            │
│  3. Check Balance (→ thread)           │
│  4. Transfer                            │
│  5. View History                        │
│  6. Get Receipt (→ thread)             │
│  7. Logout                              │
└─────────────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────┐
│      loggedInAccount = null              │
│         → Back to Auth Menu              │
└─────────────────────────────────────────┘
```

---

## Multithreading Design

### Why Threads?

File I/O operations can be slow. By running them in background threads, the main UI remains responsive and the user gets instant feedback.

### Snapshot Pattern (Critical for Thread Safety)

When spawning a thread, you must pass a **frozen copy** of the data, not a live reference:

```java
// WRONG - shares live reference
this.transactions = transactions;  // list may change while thread writes!

// RIGHT - deep copy (snapshot)
this.transactions = new ArrayList<>(transactions);  // frozen at creation time
```

| Data | Snapshot Method |
|------|---------------|
| `accountId` | `loggedInAccount.getAccountId()` (String is immutable) |
| `ownerName` | `loggedInAccount.getOwnerName()` (String is immutable) |
| `balance` | `loggedInAccount.getBalance()` (primitive double, copied by value) |
| `transactions` | `new ArrayList<>(loggedInAccount.getTransactionHistory())` (deep copy) |

### Thread Flow

```
User selects "Get Receipt"
        │
        ▼
┌─────────────────────────────┐
│ 1. Take snapshot of data    │
│ 2. Print "Generating..."    │
│ 3. Spawn new Thread()       │────┐
│ 4. Print "Started..."        │    │
│ 5. Return to menu (instant)  │    │
└─────────────────────────────┘    │
                                   ▼
                          ┌─────────────────┐
                          │ ReceiptWriterTask│
                          │   (background)   │
                          │ + run()           │
                          │   → create file   │
                          │   → write data    │
                          │   → close file    │
                          │   → print confirm │
                          └─────────────────┘
```

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
            int value = scanner.nextInt();
            scanner.nextLine(); // consume leftover newline
            return value;
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid integer.");
            scanner.nextLine(); // consume invalid token
        }
    }
}
```

### Scanner Buffer Management

`Scanner.nextInt()` and `Scanner.nextDouble()` do **not** consume the trailing newline (`\n`). This causes issues when switching to `nextLine()`:

```
User enters: 2\n
nextInt() reads "2" → leaves "\n" in buffer
nextLine() reads that leftover "\n" → returns empty string!
```

**Fix:** Always call `scanner.nextLine()` after `nextInt()`/`nextDouble()` to consume the newline.

---

## Receipt File Format

```
========================================
         BANK RECEIPT
========================================
Generated: 2026-06-22 21:15:30
Thread: Thread-5

----------------------------------------
ACCOUNT INFORMATION
----------------------------------------
Account ID:     ACC1000
Owner Name:     Ehab
Current Balance: $1250.00

----------------------------------------
TRANSACTION HISTORY
----------------------------------------
2026-06-22 14:30:15 | DEPOSIT      | $500.00
2026-06-22 15:45:22 | WITHDRAW     | $200.00
2026-06-22 16:10:05 | TRANSFER_OUT | $800.00
2026-06-22 18:20:33 | DEPOSIT      | $1750.00
----------------------------------------
Total Transactions: 4
Closing Balance: $1250.00
========================================
```

---

## Balance Log Format

```
[2026-06-22 21:15:30] | ACC1000 | $1250.00 | checked by Thread-5
[2026-06-22 21:20:15] | ACC1000 | $1500.00 | checked by Thread-7
```

---

## Sample Run

```
>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 2
Enter owner name: Ehab
Set PIN: 1234
Account created! Your ID is: ACC1000

Enter choice: 1
Enter account ID: ACC1000
Enter PIN: 1234
Welcome, Ehab!

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 1
Enter amount to deposit: 500
Deposited successfully. New balance: $500.0

Enter choice: 3
Balance: $500.0
[Thread] Balance log saved: balance_log_ACC1000_20260622_211530.txt

Enter choice: 6
Generating receipt...
Receipt generation started in background.
[Thread] Receipt saved: receipt_ACC1000_20260622_211545.txt

Enter choice: 7
Goodbye, Ehab!

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
```

---

## How to Run

```bash
javac BankSystem.java
java BankSystem
```

> All classes are in a single file. Only `BankSystem` is `public`, matching the filename.

---

## Running

```log

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 2
Enter owner name: Ehab Magdy
Set PIN: 123
Account created! Your ID is: ACC1000

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 1
Enter account ID: ACC1000
Enter PIN: 123
Welcome, Ehab Magdy!

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 3
Balance: $0.0

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 1
Enter amount to deposit: 3500
Deposited successfully. New balance: $3500.0

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 2
Enter amount to withdraw: 800
Withdrawn successfully. New balance: $2700.0

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 5

--- Transaction History ---
2026-06-22T21:24:41.710183630 | DEPOSIT | $3500.0
2026-06-22T21:24:48.695482909 | WITHDRAW | $800.0

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 7
Goodbye, Ehab Magdy!

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 2
Enter owner name: Inner Valencia
Set PIN: 567
Account created! Your ID is: ACC1001

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 1
Enter account ID: ACC1001
Enter PIN: 567
Welcome, Inner Valencia!

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Inner Valencia (ACC1001)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 1
Enter amount to deposit: 6300
Deposited successfully. New balance: $6300.0

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Inner Valencia (ACC1001)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 7
Goodbye, Inner Valencia!

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 1
Enter account ID: ACC1000 
Enter PIN: 123
Welcome, Ehab Magdy!

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 4
Enter target account ID: ACC1001
Enter amount to transfer: 500
Transferred successfully.
Your new balance: $2200.0

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 6
Generating receipt...
Receipt generation started in background.

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 6

>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<
Logged in as: Ehab Magdy (ACC1000)
1. Deposit
2. Withdraw
3. Check Balance
4. Transfer to Another Account
5. View Transaction History
6. Get Receipt (Save to File)
7. Logout
=====================================
Enter choice: 7
Goodbye, Ehab Magdy!

>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<
1. Login
2. Create Account
0. Exit
===================================
Enter choice: 0
Goodbye!
```