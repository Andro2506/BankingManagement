package com.bank.javadb;

/**
 * JAVA / DB MODULE - US003 (Inheritance) - base class.
 *
 * Holds attributes and methods common to all account types.
 */
public class BankAccount {

    protected String accountNumber;
    protected String holderName;
    protected double balance;
    protected String accountType;   // set by subclasses

    public BankAccount(String accountNumber, String holderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialBalance;
        this.accountType = "Generic";
    }

    /** Adds amount to the balance. */
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }
        balance = balance + amount;
        System.out.println("Deposited " + amount + ". New balance: " + balance);
    }

    /**
     * Withdraws amount. Throws InsufficientFundsException if balance is too low.
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be greater than zero.");
            return;
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Insufficient funds for " + accountNumber +
                ". Balance: " + balance + ", Requested: " + amount);
        }
        balance = balance - amount;
        System.out.println("Withdrew " + amount + ". New balance: " + balance);
    }

    /** Print the current balance. */
    public void balanceInquiry() {
        System.out.println("Account " + accountNumber + " (" + accountType +
            ") balance: " + balance);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }
}
