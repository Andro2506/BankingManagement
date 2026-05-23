package com.bank.javadb;

/**
 * JAVA / DB MODULE - US003 driver: shows BankAccount, SavingsAccount,
 * and CheckingAccount working together.
 *
 * Run:  java com.bank.javadb.AccountInheritanceDemo
 */
public class AccountInheritanceDemo {

    public static void main(String[] args) {
        // Create one of each subtype
        SavingsAccount sav = new SavingsAccount("SAV001", "Alice", 5000, 0.04);
        CheckingAccount chk = new CheckingAccount("CHK001", "Bob", 2000, 1000);

        sav.balanceInquiry();
        chk.balanceInquiry();

        // Deposits
        sav.deposit(1500);
        chk.deposit(500);

        // Withdraws (try a large one to trigger error handling)
        try {
            sav.withdraw(8000); // will fail (would go below min 1000)
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            chk.withdraw(3500); // exceeds balance + overdraft (allowed up to -1000)
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // Successful operations
        try {
            sav.withdraw(2000);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        try {
            chk.withdraw(2500);   // pushes into overdraft of -500
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        sav.addAnnualInterest();
        sav.balanceInquiry();
        chk.balanceInquiry();
    }
}
