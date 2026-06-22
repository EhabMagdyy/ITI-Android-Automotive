import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;

enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_OUT,
    TRANSFER_IN
}

// Transaction entity
class Transaction {
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(TransactionType type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return timestamp + " | " + type + " | $" + amount;
    }
}

// Account entity
class Account {
    private String accountId;
    private String ownerName;
    private double balance;
    private String pin;
    private List<Transaction> transactions;

    public Account(String accountId, String ownerName, String pin) {
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.pin = pin;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getAccountId() { return accountId; }
    public String getOwnerName() { return ownerName; }
    public double getBalance() { return balance; }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount));
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
        transactions.add(new Transaction(TransactionType.WITHDRAW, amount));
    }

    public void transferTo(Account target, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_OUT, amount));
        target.balance += amount;
        target.transactions.add(new Transaction(TransactionType.TRANSFER_IN, amount));
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}

// Account manager
class AccountManager {
    private Map<String, Account> accounts;
    private int nextId;

    public AccountManager() {
        this.accounts = new HashMap<>();
        this.nextId = 1000;
    }

    public Account createAccount(String ownerName, String pin) {
        String accountId = "ACC" + nextId++;
        Account account = new Account(accountId, ownerName, pin);
        accounts.put(accountId, account);
        return account;
    }

    public boolean removeAccount(String accountId) {
        Account account = accounts.get(accountId);
        if (account == null) {
            return false;
        }
        if (account.getBalance() != 0) {
            throw new IllegalArgumentException("Account balance must be zero to remove");
        }
        accounts.remove(accountId);
        return true;
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    public boolean accountExists(String accountId) {
        return accounts.containsKey(accountId);
    }

    public void listAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        System.out.println("\n--- All Accounts ---");
        for (Account acc : accounts.values()) {
            System.out.println(acc.getAccountId() + " | " + acc.getOwnerName() + " | $" + acc.getBalance());
        }
    }
}

// Bank system UI
public class BankSystem {
    private AccountManager accountManager;
    private Scanner scanner;

    public BankSystem() {
        this.accountManager = new AccountManager();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BankSystem bank = new BankSystem();
        bank.run();
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = getIntInput("Enter choice: ");

            try {
                switch (choice) {
                    case 1: deposit(); break;
                    case 2: withdraw(); break;
                    case 3: checkBalance(); break;
                    case 4: transfer(); break;
                    case 5: receiveHistory(); break;
                    case 6: manageAccount(); break;
                    case 0: System.out.println("Goodbye!"); return;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n>>>>>>>>>>>> BANK SYSTEM <<<<<<<<<<<<<");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Check Balance");
        System.out.println("4. Transfer to Another Account");
        System.out.println("5. View Transaction History");
        System.out.println("6. Create / Remove Account");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    private void deposit() {
        Account account = authenticateAccount();
        double amount = getDoubleInput("Enter amount to deposit: ");
        account.deposit(amount);
        System.out.println("Deposited successfully. New balance: $" + account.getBalance());
    }

    private void withdraw() {
        Account account = authenticateAccount();
        double amount = getDoubleInput("Enter amount to withdraw: ");
        account.withdraw(amount);
        System.out.println("Withdrawn successfully. New balance: $" + account.getBalance());
    }

    private void checkBalance() {
        Account account = authenticateAccount();
        System.out.println("Account: " + account.getAccountId());
        System.out.println("Owner: " + account.getOwnerName());
        System.out.println("Balance: $" + account.getBalance());
    }

    private void transfer() {
        Account source = authenticateAccount();
        String targetId = getStringInput("Enter target account ID: ");
        Account target = accountManager.getAccount(targetId);
        if (target == null) {
            System.out.println("Target account not found.");
            return;
        }
        double amount = getDoubleInput("Enter amount to transfer: ");
        source.transferTo(target, amount);
        System.out.println("Transferred successfully.");
        System.out.println("Your new balance: $" + source.getBalance());
    }

    private void receiveHistory() {
        Account account = authenticateAccount();
        List<Transaction> history = account.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\n--- Transaction History ---");
        for (Transaction tx : history) {
            System.out.println(tx);
        }
    }

    private void manageAccount() {
        System.out.println("\n1. Create Account");
        System.out.println("2. Remove Account");
        System.out.println("3. List All Accounts");
        int subChoice = getIntInput("Enter choice: ");

        switch (subChoice) {
            case 1:
                String name = getStringInput("Enter owner name: ");
                String pin = getStringInput("Set PIN: ");
                Account newAcc = accountManager.createAccount(name, pin);
                System.out.println("Account created: " + newAcc.getAccountId());
                break;
            case 2:
                String removeId = getStringInput("Enter account ID to remove: ");
                if (accountManager.removeAccount(removeId)) {
                    System.out.println("Account removed successfully.");
                } else {
                    System.out.println("Account not found.");
                }
                break;
            case 3:
                accountManager.listAllAccounts();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private Account authenticateAccount() {
        String accountId = getStringInput("Enter account ID: ");
        Account account = accountManager.getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        String pin = getStringInput("Enter PIN: ");
        if (!account.validatePin(pin)) {
            throw new IllegalArgumentException("Invalid PIN");
        }
        return account;
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } 
            catch(InputMismatchException e) {
                System.out.println("Error: Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private String getStringInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = scanner.next();
                if (value.trim().isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty.");
                }
                return value;
            } 
            catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}