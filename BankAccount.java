import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

class BankAccount {
    private double balance;
    private List<String> transactionHistory;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        addToTransactionHistory("Account created with initial balance: $" + initialBalance);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addToTransactionHistory("Deposited: $" + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            addToTransactionHistory("Withdrawn: $" + amount);
            return true;
        }
        return false;
    }

    public void calculateInterest(double rate) {
        double interest = balance * rate / 100.0;
        balance += interest;
        addToTransactionHistory("Interest added: $" + interest);
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    private void addToTransactionHistory(String transaction) {
        transactionHistory.add(transaction);
    }
}

class BankApplication extends Frame {
    private BankAccount account;

    private Label balanceLabel;
    private TextField amountTextField;

    public BankApplication() {
        account = new BankAccount(1000.0); // Initial balance

        // Set up UI components
        balanceLabel = new Label("Balance: $" + account.getBalance());
        amountTextField = new TextField(10);

        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button interestButton = new Button("Calculate Interest");
        Button receiptButton = new Button("Receipt");

        // Add action listeners
        depositButton.addActionListener(e -> deposit());
        withdrawButton.addActionListener(e -> withdraw());
        interestButton.addActionListener(e -> calculateInterest());
        receiptButton.addActionListener(e -> showReceipt());

        // Set layout
        setLayout(new FlowLayout());

        // Add components to the frame
        add(balanceLabel);
        add(new Label("Amount:"));
        add(amountTextField);
        add(depositButton);
        add(withdrawButton);
        add(interestButton);
        add(receiptButton);

        // Set up frame properties
        setTitle("Enhanced Bank Application");
        setSize(400, 200);
        setVisible(true);

        // Handle window close event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                displayTransactionHistory();
                dispose();
            }
        });
    }

    private void deposit() {
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            account.deposit(amount);
            updateBalanceLabel();
            amountTextField.setText("");
        } catch (NumberFormatException e) {
            showError("Invalid amount format.");
        }
    }

    private void withdraw() {
        try {
            double amount = Double.parseDouble(amountTextField.getText());
            if (account.withdraw(amount)) {
                updateBalanceLabel();
                amountTextField.setText("");
            } else {
                showError("Invalid amount for withdrawal.");
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount format.");
        }
    }

    private void calculateInterest() {
        try {
            double rate = Double.parseDouble(showInputDialog("Enter interest rate (%):"));
            account.calculateInterest(rate);
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            showError("Invalid interest rate format.");
        }
    }

    private void showReceipt() {
        StringBuilder receipt = new StringBuilder("Transaction Receipt:\n");
        List<String> transactions = account.getTransactionHistory();
        for (String transaction : transactions) {
            receipt.append(transaction).append("\n");
        }
        showMessage(receipt.toString(), "Transaction Receipt");
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + formatDouble(account.getBalance()));
    }

    private void displayTransactionHistory() {
        List<String> transactions = account.getTransactionHistory();
        StringBuilder historyMessage = new StringBuilder("Transaction History:\n");
        for (String transaction : transactions) {
            historyMessage.append(transaction).append("\n");
        }
        showMessage(historyMessage.toString(), "Transaction History");
    }

    private String showInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatDouble(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    public static void main(String[] args) {
        new BankApplication();
    }
}
