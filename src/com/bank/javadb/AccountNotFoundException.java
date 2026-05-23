package com.bank.javadb;

/**
 * Custom exception thrown when an operation refers to an account that does not exist.
 */
public class AccountNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String message) {
        super(message);
    }
}
