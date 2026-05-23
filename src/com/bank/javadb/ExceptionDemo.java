package com.bank.javadb;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * JAVA / DB MODULE - US002 (Exception Handling).
 *
 * Demonstrates three custom error scenarios:
 *   1. Invalid input (e.g. typing letters into a number) -> NumberFormatException handler
 *   2. Withdraw greater than balance -> InsufficientFundsException
 *   3. Operating on a non-existent account -> AccountNotFoundException
 *
 * Run:  java com.bank.javadb.ExceptionDemo
 */
public class ExceptionDemo {

    // Simple in-memory account map: accountNumber -> balance
    private Map<String, Double> accounts = new HashMap<String, Double>();

    public ExceptionDemo() {
        // Seed two accounts
        accounts.put("ACC1001", 5000.0);
        accounts.put("ACC1002", 12000.0);
    }

    /**
     * Withdraw money. Throws AccountNotFoundException or InsufficientFundsException
     * with clear messages.
     */
    public void withdraw(String acc, double amount)
            throws AccountNotFoundException, InsufficientFundsException {

        if (!accounts.containsKey(acc)) {
            throw new AccountNotFoundException("Account does not exist: " + acc);
        }
        double bal = accounts.get(acc);
        if (amount > bal) {
            throw new InsufficientFundsException(
                "Insufficient funds. Requested: " + amount + ", Balance: " + bal);
        }
        accounts.put(acc, bal - amount);
        System.out.println("Withdrew " + amount + " from " + acc +
            ". New balance: " + accounts.get(acc));
    }

    public static void main(String[] args) {
        ExceptionDemo demo = new ExceptionDemo();
        Scanner sc = new Scanner(System.in);

        System.out.println("---- Exception Handling Demo ----");
        System.out.println("Available accounts: ACC1001 (5000), ACC1002 (12000)");

        // 1) Invalid input -> NumberFormatException
        System.out.print("Enter withdraw amount (try typing letters to test invalid input): ");
        String amountText = sc.nextLine();
        double amount = 0.0;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please correct and re-enter a number.");
            sc.close();
            return;
        }

        // 2 & 3) Account not found / insufficient funds
        System.out.print("Enter account number: ");
        String acc = sc.nextLine();

        try {
            demo.withdraw(acc, amount);
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        sc.close();
    }
}
