package com.bank.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.bank.util.DBConnection;

/**
 * JAVA / DB MODULE - US004 (Fund Transfer).
 *
 * Console program that:
 *  - reads source account, destination account and amount
 *  - checks that the source has sufficient funds
 *  - on success, debits the source and credits the destination in the
 *    "customer" table
 *
 * Run:  java com.bank.javadb.FundTransfer
 */
public class FundTransfer {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("---- Fund Transfer ----");
        System.out.print("Source account number: ");
        String src = sc.nextLine();
        System.out.print("Destination account number: ");
        String dst = sc.nextLine();
        System.out.print("Amount: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid amount.");
            sc.close();
            return;
        }
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            sc.close();
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();

            // Look up source balance
            double srcBalance = lookupBalance(con, src);
            if (srcBalance < 0) {
                System.out.println("Source account not found: " + src);
                sc.close();
                return;
            }
            // Look up destination balance
            double dstBalance = lookupBalance(con, dst);
            if (dstBalance < 0) {
                System.out.println("Destination account not found: " + dst);
                sc.close();
                return;
            }

            // Check funds
            if (amount > srcBalance) {
                System.out.println("Insufficient funds in source. Balance: " + srcBalance);
                sc.close();
                return;
            }

            // Update both balances
            con.setAutoCommit(false);
            try {
                updateBalance(con, src, srcBalance - amount);
                updateBalance(con, dst, dstBalance + amount);
                con.commit();
                System.out.println("Transfer of " + amount + " from " + src + " to " + dst + " completed.");
                System.out.println("New source balance:      " + (srcBalance - amount));
                System.out.println("New destination balance: " + (dstBalance + amount));
            } catch (SQLException ex) {
                con.rollback();
                System.out.println("Transfer failed, rolled back. Reason: " + ex.getMessage());
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            DBConnection.close(con);
            sc.close();
        }
    }

    /**
     * Look up the balance of an account by account_number.
     * Returns -1 if the account does not exist.
     */
    private static double lookupBalance(Connection con, String accountNumber) throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "SELECT account_balance FROM customer WHERE account_number = ?");
        ps.setString(1, accountNumber);
        ResultSet rs = ps.executeQuery();
        double bal = -1;
        if (rs.next()) {
            bal = rs.getDouble(1);
        }
        rs.close();
        ps.close();
        return bal;
    }

    /**
     * Update the balance for an account_number.
     */
    private static void updateBalance(Connection con, String accountNumber, double newBalance)
            throws SQLException {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE customer SET account_balance = ? WHERE account_number = ?");
        ps.setDouble(1, newBalance);
        ps.setString(2, accountNumber);
        ps.executeUpdate();
        ps.close();
    }
}
