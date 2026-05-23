package com.bank.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.util.DBConnection;

/**
 * CUSTOMER DB MODULES
 *   US001: Create customer_demo table (subset of fields) and insert 10 records.
 *   US002: Retrieve customer details by SSN.
 *   US003: Update personal contact details and display the result.
 *
 * Run:  java com.bank.javadb.CustomerDBDemo
 */
public class CustomerDBDemo {

    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            Statement st = con.createStatement();

            // US001: create + insert
            st.execute("DROP TABLE IF EXISTS customer_demo");
            st.execute(
                "CREATE TABLE customer_demo (" +
                "  customer_ssn_id TEXT PRIMARY KEY," +
                "  first_name TEXT, last_name TEXT, email TEXT," +
                "  date_of_birth TEXT, address TEXT, contact_number TEXT)");

            String[][] data = {
                {"1800001","Rahul","Varma","rahul.v@mail.com","1990-05-12","Pune","9000000001"},
                {"1800002","Sita","Raman","sita.r@mail.com","1985-09-22","Mumbai","9000000002"},
                {"1900001","Sheetal","Patil","sheetal.p@mail.com","1992-07-20","Delhi","9000000003"},
                {"1900002","Pooja","Patil","pooja.p@mail.com","1988-08-04","Hyderabad","9000000004"},
                {"2000001","Rahul","Sharma","rahul.s@mail.com","1995-08-20","Bangalore","9000000005"},
                {"2100001","Pooja","Srikari","pooja.sri@mail.com","1993-08-20","Chennai","9000000006"},
                {"2100002","Pooja","Rewa","pooja.r@mail.com","1994-08-20","Kolkata","9000000007"},
                {"2100003","Dan","Stewart","dan.s@mail.com","1980-08-04","Kochi","9000000008"},
                {"2800001","Sia","R","sia.r@mail.com","2000-09-21","Ahmedabad","9000000009"},
                {"2200001","Sonali","G","sonali.g@mail.com","1991-05-01","Pune","9000000010"}
            };

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO customer_demo VALUES (?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    ps.setString(j + 1, data[i][j]);
                }
                ps.executeUpdate();
            }
            ps.close();
            System.out.println("Inserted 10 customer_demo rows.");

            // US002: retrieve by SSN
            String ssn = "1900001";
            System.out.println();
            System.out.println("US002 - Retrieve customer by SSN " + ssn + ":");
            ps = con.prepareStatement(
                "SELECT customer_ssn_id, first_name || ' ' || last_name AS name, " +
                "email, contact_number, address FROM customer_demo WHERE customer_ssn_id = ?");
            ps.setString(1, ssn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("  SSN:     " + rs.getString("customer_ssn_id"));
                System.out.println("  Name:    " + rs.getString("name"));
                System.out.println("  Email:   " + rs.getString("email"));
                System.out.println("  Phone:   " + rs.getString("contact_number"));
                System.out.println("  Address: " + rs.getString("address"));
            } else {
                System.out.println("  No row found.");
            }
            rs.close();
            ps.close();

            // US003: update phone+address, then display
            System.out.println();
            System.out.println("US003 - Update contact details for SSN " + ssn + ":");
            ps = con.prepareStatement(
                "UPDATE customer_demo SET contact_number = ?, address = ? " +
                "WHERE customer_ssn_id = ?");
            ps.setString(1, "9999999999");
            ps.setString(2, "New Delhi (Updated)");
            ps.setString(3, ssn);
            int n = ps.executeUpdate();
            ps.close();
            System.out.println("  Rows updated: " + n);

            ps = con.prepareStatement(
                "SELECT customer_ssn_id, first_name || ' ' || last_name AS name, " +
                "email, contact_number, address FROM customer_demo WHERE customer_ssn_id = ?");
            ps.setString(1, ssn);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("  After update:");
                System.out.println("  SSN:     " + rs.getString("customer_ssn_id"));
                System.out.println("  Name:    " + rs.getString("name"));
                System.out.println("  Email:   " + rs.getString("email"));
                System.out.println("  Phone:   " + rs.getString("contact_number"));
                System.out.println("  Address: " + rs.getString("address"));
            }
            rs.close();
            ps.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            DBConnection.close(con);
        }
    }
}
