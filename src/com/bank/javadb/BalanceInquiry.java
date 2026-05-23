package com.bank.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.bank.util.DBConnection;

/**
 * JAVA / DB MODULE - US005 (Balance Inquiry).
 *
 * Console program that asks the user for an account number and prints the
 * balance in a friendly format.
 *
 * Run:  java com.bank.javadb.BalanceInquiry
 */
public class BalanceInquiry {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("---- Balance Inquiry ----");
        System.out.print("Enter account number: ");
        String acc = sc.nextLine();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT first_name, last_name, account_number, account_balance " +
                "FROM customer WHERE account_number = ?");
            ps.setString(1, acc);
            rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                String accountNumber = rs.getString("account_number");
                double balance = rs.getDouble("account_balance");

                System.out.println();
                System.out.println("=== Account Summary ===");
                System.out.println("Holder:         " + name);
                System.out.println("Account Number: " + accountNumber);
                System.out.println("Current Balance:Rs. " + String.format("%.2f", balance));
            } else {
                System.out.println("No account found with number: " + acc);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
            sc.close();
        }
    }
}
