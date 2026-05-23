package com.bank.javadb;

/**
 * JAVA / DB MODULE - US003 - Checking sub-class of BankAccount.
 *
 * Allows an overdraft up to a specified limit.
 */
public class CheckingAccount extends BankAccount {

    private double overdraftLimit;     // e.g. 2000 means balance can go to -2000

    public CheckingAccount(String accountNumber, String holderName,
                           double initialBalance, double overdraftLimit) {
        super(accountNumber, holderName, initialBalance);
        this.accountType = "Checking";
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * Override withdraw to allow overdraft up to overdraftLimit.
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be greater than zero.");
            return;
        }
        if ((balance - amount) < (-overdraftLimit)) {
            throw new InsufficientFundsException(
                "Insufficient funds. Overdraft limit reached for " + accountNumber);
        }
        balance = balance - amount;
        System.out.println("Withdrew " + amount + ". New balance: " + balance);
    }
}
