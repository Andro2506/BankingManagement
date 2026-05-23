package com.bank.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.util.DBConnection;

/**
 * TRANSACTION DB MODULES
 *   US004: Create customer_transactions table and insert 10 records.
 *   US005: Retrieve customer account + personal details (join transactions + customer).
 *   US006: After a successful transaction, update records and show new balance.
 *
 * The web app uses the real customer_transactions table; this demo writes to
 * a separate transaction_demo table so it does not clash.
 *
 * Run:  java com.bank.javadb.TransactionDBDemo
 */
public class TransactionDBDemo {

    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            Statement st = con.createStatement();

            // US004: build a fresh demo transactions table
            st.execute("DROP TABLE IF EXISTS transaction_demo");
            st.execute(
                "CREATE TABLE transaction_demo (" +
                "  transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  customer_id TEXT, customer_name TEXT, account_number TEXT," +
                "  ifsc_code TEXT, account_balance REAL, aadhar_card_no TEXT," +
                "  pan_card_no TEXT, contact_number TEXT)");

            String[][] data = {
                {"1800001","Rahul Varma","ACC1000000001","BANK0001","30000.0","111122223331","ABCDE1234A","9000000001"},
                {"1800002","Sita Raman","ACC1000000002","BANK0001","50000.0","111122223332","ABCDE1234B","9000000002"},
                {"1900001","Sheetal Patil","ACC1000000003","BANK0002","40000.0","111122223333","ABCDE1234C","9000000003"},
                {"1900002","Pooja Patil","ACC1000000004","BANK0002","70000.0","111122223334","ABCDE1234D","9000000004"},
                {"2000001","Rahul Sharma","ACC1000000005","BANK0003","20000.0","111122223335","ABCDE1234E","9000000005"},
                {"2100001","Pooja Srikari","ACC1000000006","BANK0003","25000.0","111122223336","ABCDE1234F","9000000006"},
                {"2100002","Pooja Rewa","ACC1000000007","BANK0004","35000.0","111122223337","ABCDE1234G","9000000007"},
                {"2100003","Dan Stewart","ACC1000000008","BANK0004","35000.0","111122223338","ABCDE1234H","9000000008"},
                {"2800001","Sia R","ACC1000000009","BANK0005","30000.0","111122223339","ABCDE1234I","9000000009"},
                {"2200001","Sonali G","ACC1000000010","BANK0005","15000.0","111122223340","ABCDE1234J","9000000010"}
            };

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO transaction_demo (customer_id, customer_name, account_number, " +
                "ifsc_code, account_balance, aadhar_card_no, pan_card_no, contact_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < data.length; i++) {
                ps.setString(1, data[i][0]);
                ps.setString(2, data[i][1]);
                ps.setString(3, data[i][2]);
                ps.setString(4, data[i][3]);
                ps.setDouble(5, Double.parseDouble(data[i][4]));
                ps.setString(6, data[i][5]);
                ps.setString(7, data[i][6]);
                ps.setString(8, data[i][7]);
                ps.executeUpdate();
            }
            ps.close();
            System.out.println("Inserted 10 transaction_demo rows.");

            // US005: join with customer table to fetch full personal details
            // (date is converted to DD-MM-YYYY using SQLite strftime)
            System.out.println();
            System.out.println("US005 - Joined customer + transaction details:");
            String join =
                "SELECT t.customer_id, t.customer_name, t.account_number, t.ifsc_code, " +
                "       t.account_balance, t.aadhar_card_no, t.pan_card_no, " +
                "       strftime('%d-%m-%Y', c.date_of_birth) AS dob, " +
                "       c.email, c.address, t.contact_number " +
                "FROM transaction_demo t LEFT JOIN customer c " +
                "  ON t.customer_id = c.customer_ssn_id";
            ResultSet rs = st.executeQuery(join);
            while (rs.next()) {
                System.out.println("  ----");
                System.out.println("  Customer ID: " + rs.getString("customer_id"));
                System.out.println("  Name:        " + rs.getString("customer_name"));
                System.out.println("  Account:     " + rs.getString("account_number"));
                System.out.println("  IFSC:        " + rs.getString("ifsc_code"));
                System.out.println("  Balance:     " + rs.getDouble("account_balance"));
                System.out.println("  Aadhar:      " + rs.getString("aadhar_card_no"));
                System.out.println("  PAN:         " + rs.getString("pan_card_no"));
                System.out.println("  DOB:         " + rs.getString("dob"));
                System.out.println("  Email:       " + rs.getString("email"));
                System.out.println("  Address:     " + rs.getString("address"));
                System.out.println("  Contact:     " + rs.getString("contact_number"));
            }
            rs.close();

            // US006: simulate a credit of 5000 on first row, then show updated info
            System.out.println();
            System.out.println("US006 - Credit 5000 to first transaction row and show new balance:");
            st.execute("UPDATE transaction_demo SET account_balance = account_balance + 5000 " +
                "WHERE transaction_id = (SELECT MIN(transaction_id) FROM transaction_demo)");

            rs = st.executeQuery(
                "SELECT t.customer_id, c.first_name || ' ' || c.last_name AS name, " +
                "       c.email, t.account_balance " +
                "FROM transaction_demo t LEFT JOIN customer c " +
                "  ON t.customer_id = c.customer_ssn_id " +
                "WHERE transaction_id = (SELECT MIN(transaction_id) FROM transaction_demo)");
            if (rs.next()) {
                System.out.println("  Customer ID: " + rs.getString("customer_id"));
                System.out.println("  Name:        " + rs.getString("name"));
                System.out.println("  Email:       " + rs.getString("email"));
                System.out.println("  Balance:     " + rs.getDouble("account_balance"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            DBConnection.close(con);
        }
    }
}
