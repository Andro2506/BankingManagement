package com.bank.javadb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bank.util.DBConnection;

/**
 * SQLITE DB MODULES (Employee Sheet)
 *   US001: Create the employee table and insert 10 demo records.
 *   US002: After insertion, add Rs.1000 to each Clerk's salary.
 *   US003: After insertion, add 10% of salary to each Manager.
 *
 * The DatabaseInitializer (web app listener) does the same on web startup.
 * This class is a console runner that does it independently.
 *
 * Run:  java com.bank.javadb.EmployeeDBDemo
 */
public class EmployeeDBDemo {

    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();

            // Re-create employee table cleanly for the demo
            Statement st = con.createStatement();
            st.execute("DROP TABLE IF EXISTS employee_demo");
            st.execute(
                "CREATE TABLE employee_demo (" +
                "  employee_id INTEGER PRIMARY KEY," +
                "  first_name TEXT, last_name TEXT, email TEXT," +
                "  contact_number TEXT, designation TEXT, salary NUMERIC)");

            // Insert 10 records (US001)
            String[][] data = {
                {"1000001", "Aarav", "Sharma", "aarav@bank.com", "9876543210", "Clerk", "25000"},
                {"1000002", "Priya", "Patel", "priya@bank.com", "9876543211", "Manager", "75000"},
                {"1000003", "Rohan", "Kumar", "rohan@bank.com", "9876543212", "Accountant", "55000"},
                {"1000004", "Sneha", "Reddy", "sneha@bank.com", "9876543213", "Clerk", "26000"},
                {"1000005", "Vikram", "Singh", "vikram@bank.com", "9876543214", "Manager", "80000"},
                {"1000006", "Anita", "Joshi", "anita@bank.com", "9876543215", "Clerk", "24000"},
                {"1000007", "Karan", "Verma", "karan@bank.com", "9876543216", "Accountant", "58000"},
                {"1000008", "Pooja", "Gupta", "pooja@bank.com", "9876543217", "Manager", "78000"},
                {"1000009", "Arjun", "Nair", "arjun@bank.com", "9876543218", "Clerk", "27000"},
                {"1000010", "Divya", "Iyer", "divya@bank.com", "9876543219", "Accountant", "60000"}
            };
            for (int i = 0; i < data.length; i++) {
                String sql = "INSERT INTO employee_demo VALUES(" +
                    data[i][0] + ",'" + data[i][1] + "','" + data[i][2] + "','" + data[i][3] +
                    "','" + data[i][4] + "','" + data[i][5] + "'," + data[i][6] + ")";
                st.execute(sql);
            }
            System.out.println("Inserted 10 demo employees.");

            // US002: Add Rs.1000 to each Clerk
            int clerkRows = st.executeUpdate(
                "UPDATE employee_demo SET salary = salary + 1000 WHERE designation = 'Clerk'");
            System.out.println("Clerk raise applied to " + clerkRows + " rows.");

            // US003: Add 10% to each Manager
            int mgrRows = st.executeUpdate(
                "UPDATE employee_demo SET salary = salary + (salary * 0.10) " +
                "WHERE designation = 'Manager'");
            System.out.println("Manager raise applied to " + mgrRows + " rows.");

            // Print updated Clerks
            System.out.println();
            System.out.println("Updated Clerks:");
            ResultSet rs = st.executeQuery(
                "SELECT first_name || ' ' || last_name AS name, designation, salary " +
                "FROM employee_demo WHERE designation = 'Clerk'");
            while (rs.next()) {
                System.out.printf("  %-20s %-12s %.2f%n",
                    rs.getString("name"), rs.getString("designation"), rs.getDouble("salary"));
            }
            rs.close();

            // Print updated Managers
            System.out.println();
            System.out.println("Updated Managers:");
            rs = st.executeQuery(
                "SELECT first_name || ' ' || last_name AS name, designation, salary " +
                "FROM employee_demo WHERE designation = 'Manager'");
            while (rs.next()) {
                System.out.printf("  %-20s %-12s %.2f%n",
                    rs.getString("name"), rs.getString("designation"), rs.getDouble("salary"));
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
