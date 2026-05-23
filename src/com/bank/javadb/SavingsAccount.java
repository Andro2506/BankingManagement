package com.bank.javadb;

/**
 * JAVA / DB MODULE - US003 - Savings sub-class of BankAccount.
 *
 * Adds an interest rate field and a small "minimum balance" rule.
 */
public class SavingsAccount extends BankAccount {

    private double interestRate;       // example: 0.04 = 4% per year
    private static final double MIN_BALANCE = 1000.0;

    public SavingsAccount(String accountNumber, String holderName,
                          double initialBalance, double interestRate) {
        super(accountNumber, holderName, initialBalance);
        this.accountType = "Savings";
        this.interestRate = interestRate;
    }

    /**
     * Override withdraw so we keep at least MIN_BALANCE in the account.
     */
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            System.out.println("Withdraw amount must be greater than zero.");
            return;
        }
        if ((balance - amount) < MIN_BALANCE) {
            throw new InsufficientFundsException(
                "Cannot withdraw - would go below minimum balance of " + MIN_BALANCE);
        }
        balance = balance - amount;
        System.out.println("Withdrew " + amount + ". New balance: " + balance);
    }

    /** Add one year of simple interest to the balance. */
    public void addAnnualInterest() {
        double interest = balance * interestRate;
        balance = balance + interest;
        System.out.println("Added interest " + interest + ". New balance: " + balance);
    }
}
