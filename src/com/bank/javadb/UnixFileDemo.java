package com.bank.javadb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bank.util.DBConnection;

/**
 * UNIX / FILE OPERATIONS module.
 *
 * Reads customers.txt (same folder as this class), captures it into a SQLite
 * table 'unix_customer', and runs the four required queries:
 *   1. customers with balance >= 30000
 *   2. total balance for Joint accounts
 *   3. search for a specific account number (here: 222635 and 180607)
 *   4. average balance excluding Joint accounts
 *
 * File row format: customer_id, first_last_name, dd.mm.yy, AccountType, Balance
 *
 * Run:  java com.bank.javadb.UnixFileDemo path/to/customers.txt
 *       (defaults to "src/com/bank/javadb/customers.txt")
 *
 * UNIX command equivalents (handy reference):
 *   1. awk -F', *' '$5 >= 30000 {print}' customers.txt
 *   2. awk -F', *' '$4 ~ /^[Jj][Oo][Ii][Nn][Tt]$/ {sum+=$5} END {print sum}' customers.txt
 *   3. grep -E "(^222635,|^180607,)" customers.txt
 *   4. awk -F', *' '$4 !~ /^[Jj][Oo][Ii][Nn][Tt]$/ {sum+=$5; n++} END {print sum/n}' customers.txt
 */
public class UnixFileDemo {

    public static void main(String[] args) {
        String path = "src/com/bank/javadb/customers.txt";
        if (args.length > 0) {
            path = args[0];
        }

        // Step 1: load file lines
        List<String[]> rows = readFile(path);
        if (rows == null || rows.isEmpty()) {
            System.out.println("No data read from: " + path);
            return;
        }
        System.out.println("Loaded " + rows.size() + " rows from " + path);

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            Statement st = con.createStatement();

            // Step 2: rebuild unix_customer table
            st.execute("DROP TABLE IF EXISTS unix_customer");
            st.execute(
                "CREATE TABLE unix_customer (" +
                "  customer_id TEXT," +
                "  full_name TEXT," +
                "  date_of_opening TEXT," +
                "  account_type TEXT," +
                "  balance REAL)");

            // Step 3: insert each row
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO unix_customer VALUES (?, ?, ?, ?, ?)");
            for (int i = 0; i < rows.size(); i++) {
                String[] r = rows.get(i);
                ps.setString(1, r[0]);
                ps.setString(2, r[1]);
                ps.setString(3, r[2]);
                ps.setString(4, r[3]);
                try {
                    ps.setDouble(5, Double.parseDouble(r[4]));
                } catch (Exception e) {
                    ps.setDouble(5, 0.0);
                }
                ps.executeUpdate();
            }
            ps.close();

            // -------- Query 1: balance >= 30000 --------
            System.out.println();
            System.out.println("1) Customers with balance >= 30000:");
            ResultSet rs = st.executeQuery("SELECT * FROM unix_customer WHERE balance >= 30000");
            while (rs.next()) {
                System.out.println("   " + rs.getString("customer_id") +
                    " | " + rs.getString("full_name") +
                    " | " + rs.getString("account_type") +
                    " | " + rs.getDouble("balance"));
            }
            rs.close();

            // -------- Query 2: total Joint balance --------
            System.out.println();
            rs = st.executeQuery(
                "SELECT SUM(balance) AS total FROM unix_customer " +
                "WHERE LOWER(account_type) = 'joint'");
            if (rs.next()) {
                System.out.println("2) Total balance of Joint account holders: " + rs.getDouble("total"));
            }
            rs.close();

            // -------- Query 3: search specific account ids --------
            System.out.println();
            System.out.println("3) Customers with id 222635 or 180607:");
            ps = con.prepareStatement(
                "SELECT * FROM unix_customer WHERE customer_id IN (?, ?)");
            ps.setString(1, "222635");
            ps.setString(2, "180607");
            rs = ps.executeQuery();
            int hits = 0;
            while (rs.next()) {
                hits++;
                System.out.println("   " + rs.getString("customer_id") +
                    " | " + rs.getString("full_name") +
                    " | " + rs.getString("account_type") +
                    " | " + rs.getDouble("balance"));
            }
            if (hits == 0) System.out.println("   (no matches)");
            rs.close();
            ps.close();

            // -------- Query 4: average balance excluding Joint --------
            System.out.println();
            rs = st.executeQuery(
                "SELECT AVG(balance) AS avg_bal FROM unix_customer " +
                "WHERE LOWER(account_type) <> 'joint'");
            if (rs.next()) {
                System.out.println("4) Average balance EXCLUDING Joint: " + rs.getDouble("avg_bal"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } finally {
            DBConnection.close(con);
        }
    }

    /**
     * Reads the CSV-like file into a list of String arrays.
     * Each line becomes: [customer_id, full_name, date_of_opening, account_type, balance]
     */
    private static List<String[]> readFile(String path) {
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // split by comma but trim each piece
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                // The original test data uses a single name field ("Rahul Varma") so
                // we map: 0=id, 1=name, 2=date, 3=type, 4=balance.
                String[] r = new String[5];
                r[0] = parts[0].trim();
                r[1] = parts[1].trim();
                r[2] = parts[2].trim();
                r[3] = parts[3].trim();
                r[4] = parts[4].trim();
                rows.add(r);
            }
        } catch (Exception e) {
            System.out.println("Failed to read file: " + e.getMessage());
        } finally {
            try { if (br != null) br.close(); } catch (Exception ignore) {}
        }
        return rows;
    }
}
