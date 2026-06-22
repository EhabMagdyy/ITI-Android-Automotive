import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    public Transaction(TransactionType type, double amount){
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public TransactionType getType(){ return type; }
    public double getAmount(){ return amount; }
    public LocalDateTime getTimestamp(){ return timestamp; }

    @Override
    public String toString(){
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

    public Account(String accountId, String ownerName, String pin){
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.pin = pin;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getAccountId(){ return accountId; }
    public String getOwnerName(){ return ownerName; }
    public double getBalance(){ return balance; }

    public boolean validatePin(String inputPin){
        return this.pin.equals(inputPin);
    }

    public void deposit(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount));
    }

    public void withdraw(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        if(amount > balance){
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
        transactions.add(new Transaction(TransactionType.WITHDRAW, amount));
    }

    public void transferTo(Account target, double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Amount must be positive");
        }
        if(amount > balance){
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_OUT, amount));
        target.balance += amount;
        target.transactions.add(new Transaction(TransactionType.TRANSFER_IN, amount));
    }

    public List<Transaction> getTransactionHistory(){
        return new ArrayList<>(transactions);
    }
}

// Account manager
class AccountManager {
    private Map<String, Account> accounts;
    private int nextId;

    public AccountManager(){
        this.accounts = new HashMap<>();
        this.nextId = 1000;
    }

    public Account createAccount(String ownerName, String pin){
        String accountId = "ACC" + nextId++;
        Account account = new Account(accountId, ownerName, pin);
        accounts.put(accountId, account);
        return account;
    }

    public boolean removeAccount(String accountId){
        Account account = accounts.get(accountId);
        if(account == null){
            return false;
        }
        if(account.getBalance() != 0){
            throw new IllegalArgumentException("Account balance must be zero to remove");
        }
        accounts.remove(accountId);
        return true;
    }

    public Account getAccount(String accountId){
        return accounts.get(accountId);
    }

    public boolean accountExists(String accountId){
        return accounts.containsKey(accountId);
    }

    public void listAllAccounts(){
        if(accounts.isEmpty()){
            System.out.println("No accounts found.");
            return;
        }
        System.out.println("\n--- All Accounts ---");
        for(Account acc : accounts.values()){
            System.out.println(acc.getAccountId() + " | " + acc.getOwnerName() + " | $" + acc.getBalance());
        }
    }
}

// Receipt Writer Task(Runnable for Thread)
class ReceiptWriterTask implements Runnable {
    private String accountId;
    private String ownerName;
    private double balance;
    private List<Transaction> transactions;
    private LocalDateTime generatedAt;

    public ReceiptWriterTask(String accountId, String ownerName, double balance, List<Transaction> transactions){
        this.accountId = accountId;
        this.ownerName = ownerName;
        this.balance = balance;
        this.transactions = new ArrayList<>(transactions); // deep copy
        this.generatedAt = LocalDateTime.now();
    }

    @Override
    public void run(){
        String timestamp = generatedAt.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "receipt_" + accountId + "_" + timestamp + ".txt";

        try(PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            writer.println("========================================");
            writer.println("         BANK RECEIPT");
            writer.println("========================================");
            writer.println("Generated: " + generatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Thread: " + Thread.currentThread().getName());
            writer.println();
            writer.println("----------------------------------------");
            writer.println("ACCOUNT INFORMATION");
            writer.println("----------------------------------------");
            writer.println("Account ID:     " + accountId);
            writer.println("Owner Name:     " + ownerName);
            writer.println("Current Balance: $" + String.format("%.2f", balance));
            writer.println();
            writer.println("----------------------------------------");
            writer.println("TRANSACTION HISTORY");
            writer.println("----------------------------------------");

            if(transactions.isEmpty()){
                writer.println("No transactions found.");
            }
            else {
                for(Transaction tx : transactions){
                    writer.println(tx.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            + " | " + tx.getType()
                            + " | $" + String.format("%.2f", tx.getAmount()));
                }
            }

            writer.println("----------------------------------------");
            writer.println("Total Transactions: " + transactions.size());
            writer.println("Closing Balance: $" + String.format("%.2f", balance));
            writer.println("========================================");
        } 
        catch(IOException e){
            System.out.println("[Thread] Error writing receipt: " + e.getMessage());
        }
    }
}

// Balance Checker Task(Runnable for Thread)
class BalanceCheckerTask implements Runnable {
    private String accountId;
    private double balance;
    private LocalDateTime checkedAt;

    public BalanceCheckerTask(String accountId, double balance){
        this.accountId = accountId;
        this.balance = balance;
        this.checkedAt = LocalDateTime.now();
    }

    @Override
    public void run(){
        String timestamp = checkedAt.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "balance_log_" + accountId + "_" + timestamp + ".txt";

        try(PrintWriter writer = new PrintWriter(new FileWriter(filename, true))){
            writer.println("[" + checkedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "]"
                    + " | " + accountId
                    + " | $" + String.format("%.2f", balance)
                    + " | checked by " + Thread.currentThread().getName());
            System.out.println("[Thread] Balance log saved: " + filename);
        }
        catch(IOException e){
            System.out.println("[Thread] Error writing balance log: " + e.getMessage());
        }
    }
}

// Bank system UI with Login / Logout
public class BankSystem {
    private AccountManager accountManager;
    private Scanner scanner;
    private Account loggedInAccount;

    public BankSystem(){
        this.accountManager = new AccountManager();
        this.scanner = new Scanner(System.in);
        this.loggedInAccount = null;
    }

    // ========================= Main function
    public static void main(String[] args){
        BankSystem bank = new BankSystem();
        bank.run();
    }

    public void run(){
        while(true){
            if(loggedInAccount == null){
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    // ========================= Auth Menu(Login / Register / Exit)
    private void showAuthMenu(){
        System.out.println("\n>>>>>>>>>>>> WELCOME <<<<<<<<<<<<<");
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.println("0. Exit");
        System.out.println("===================================");

        int choice = getIntInput("Enter choice: ");

        try {
            switch(choice){
                case 1: login(); break;
                case 2: register(); break;
                case 0: System.out.println("Goodbye!"); System.exit(0);
                default: System.out.println("Invalid choice.");
            }
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void login(){
        String accountId = getStringInput("Enter account ID: ");
        Account account = accountManager.getAccount(accountId);
        if(account == null){
            System.out.println("Account not found.");
            return;
        }
        String pin = getStringInput("Enter PIN: ");
        if(!account.validatePin(pin)){
            System.out.println("Invalid PIN.");
            return;
        }
        loggedInAccount = account;
        System.out.println("Welcome, " + account.getOwnerName() + "!");
    }

    private void register(){
        String name = getLine("Enter owner name: ");
        String pin = getStringInput("Set PIN: ");
        Account newAcc = accountManager.createAccount(name, pin);
        System.out.println("Account created! Your ID is: " + newAcc.getAccountId());
    }

    // ========================= Main Menu(After Login)
    private void showMainMenu(){
        System.out.println("\n>>>>>>>>>>>> MAIN MENU <<<<<<<<<<<<<");
        System.out.println("Logged in as: " + loggedInAccount.getOwnerName() + "(" + loggedInAccount.getAccountId() + ")");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Check Balance");
        System.out.println("4. Transfer to Another Account");
        System.out.println("5. View Transaction History");
        System.out.println("6. Get Receipt(Save to File)");
        System.out.println("7. Logout");
        System.out.println("=====================================");

        int choice = getIntInput("Enter choice: ");

        try {
            switch(choice){
                case 1: deposit(); break;
                case 2: withdraw(); break;
                case 3: checkBalance(); break;
                case 4: transfer(); break;
                case 5: receiveHistory(); break;
                case 6: getReceipt(); break;
                case 7: logout(); break;
                default: System.out.println("Invalid choice.");
            }
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void logout(){
        System.out.println("Goodbye, " + loggedInAccount.getOwnerName() + "!");
        loggedInAccount = null;
    }

    // ========================= Bank Operations
    private void deposit(){
        double amount = getDoubleInput("Enter amount to deposit: ");
        loggedInAccount.deposit(amount);
        System.out.println("Deposited successfully. New balance: $" + loggedInAccount.getBalance());
    }

    private void withdraw(){
        double amount = getDoubleInput("Enter amount to withdraw: ");
        loggedInAccount.withdraw(amount);
        System.out.println("Withdrawn successfully. New balance: $" + loggedInAccount.getBalance());
    }

    private void checkBalance(){
        System.out.println("Balance: $" + loggedInAccount.getBalance());
    }

    private void transfer(){
        String targetId = getStringInput("Enter target account ID: ");
        Account target = accountManager.getAccount(targetId);
        if(target == null){
            System.out.println("Target account not found.");
            return;
        }
        if(target.getAccountId().equals(loggedInAccount.getAccountId())){
            System.out.println("Cannot transfer to yourself.");
            return;
        }
        double amount = getDoubleInput("Enter amount to transfer: ");
        loggedInAccount.transferTo(target, amount);
        System.out.println("Transferred successfully.");
        System.out.println("Your new balance: $" + loggedInAccount.getBalance());
    }

    private void receiveHistory(){
        List<Transaction> history = loggedInAccount.getTransactionHistory();
        if(history.isEmpty()){
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("\n--- Transaction History ---");
        for(Transaction tx : history){
            System.out.println(tx);
        }
    }

    // ========================= Get Receipt(Threaded File Write)
    private void getReceipt(){
        // Take snapshot of all data before passing to thread
        String accountId = loggedInAccount.getAccountId();
        String ownerName = loggedInAccount.getOwnerName();
        double balance = loggedInAccount.getBalance();
        List<Transaction> transactions = loggedInAccount.getTransactionHistory();

        System.out.println("Generating receipt...");

        // Spawn background thread to write receipt file
        new Thread(new ReceiptWriterTask(accountId, ownerName, balance, transactions)).start();

        System.out.println("Receipt generation started in background.");
    }

    // ========================= Input Helpers with Exception Handling
    private int getIntInput(String prompt){
        while(true){
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            }
            catch(InputMismatchException e){
                System.out.println("Error: Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private double getDoubleInput(String prompt){
        while(true){
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            }
            catch(InputMismatchException e){
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine();
            }
        }
    }

    private String getStringInput(String prompt){
        while(true){
            try {
                System.out.print(prompt);
                String value = scanner.next();
                if(value.trim().isEmpty()){
                    throw new IllegalArgumentException("Input cannot be empty.");
                }
                return value;
            }
            catch(IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private String getLine(String prompt){
        while(true){
            try {
                System.out.print(prompt);
                // flush input
                scanner.nextLine();
                String value = scanner.nextLine();
                if(value.trim().isEmpty()){
                    throw new IllegalArgumentException("Input cannot be empty.");
                }
                return value;
            }
            catch(IllegalArgumentException e){
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}