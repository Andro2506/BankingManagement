package com.bank.javadb;

/**
 * Custom exception thrown when a withdraw amount exceeds the account balance.
 */
public class InsufficientFundsException extends Exception {
    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(String message) {
        super(message);
    }
}
