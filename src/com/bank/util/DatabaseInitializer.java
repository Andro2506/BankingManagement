package com.bank.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * DatabaseInitializer
 *
 * This class implements ServletContextListener so it runs ONE time when the
 * web app starts on Tomcat. It does two things:
 *
 *   1) Creates the four tables (employee, customer, customer_transactions, loan)
 *      if they do not already exist.
 *   2) Seeds at least 10 demo records into each table the first time the app runs.
 *
 * After tables exist, salary updates for Clerks (+1000) and Managers (+10%) are
 * applied so the UI shows the latest data.
 */
@WebListener
public class DatabaseInitializer implements ServletContextListener {

    /**
     * contextInitialized is automatically called by Tomcat when the app starts.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[DatabaseInitializer] Starting database setup...");

        Connection con = null;
        try {
            // Get a JDBC connection to banking.db
            con = DBConnection.getConnection();

            // Step 1: create all the tables if they are missing
            createEmployeeTable(con);
            createCustomerTable(con);
            createTransactionTable(con);
            createLoanTable(con);

            // Step 2: seed demo records (only if tables are empty)
            seedEmployees(con);
            seedCustomers(con);
            seedTransactions(con);
            seedLoans(con);

            // Step 3: run the salary updates (Clerk +1000, Manager +10%)
            applyClerkRaise(con);
            applyManagerRaise(con);

            System.out.println("[DatabaseInitializer] Database setup complete.");
        } catch (SQLException e) {
            System.err.println("[DatabaseInitializer] Error initializing DB:");
            e.printStackTrace();
        } finally {
            DBConnection.close(con);
        }
    }

    /**
     * contextDestroyed is called when Tomcat shuts the app down. Nothing to clean up here.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[DatabaseInitializer] App shutting down.");
    }

    /* -------------------- Table creation helpers -------------------- */

    /**
     * Creates the employee table with the schema defined in requirements.
     */
    private void createEmployeeTable(Connection con) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS employee (" +
            "  employee_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  first_name TEXT," +
            "  last_name TEXT," +
            "  email TEXT UNIQUE," +
            "  contact_number TEXT," +
            "  address TEXT," +
            "  password TEXT," +
            "  designation TEXT," +
            "  salary NUMERIC" +
            ")";
        Statement st = con.createStatement();
        st.execute(sql);
        st.close();
    }

    /**
     * Creates the customer table.
     */
    private void createCustomerTable(Connection con) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS customer (" +
            "  customer_ssn_id TEXT PRIMARY KEY," +
            "  first_name TEXT," +
            "  last_name TEXT," +
            "  email TEXT," +
            "  date_of_birth TEXT," +
            "  address TEXT," +
            "  contact_number TEXT," +
            "  aadhar_number TEXT," +
            "  pan_number TEXT," +
            "  account_number TEXT," +
            "  ifsc_code TEXT," +
            "  account_balance REAL," +
            "  account_type TEXT," +
            "  gender TEXT," +
            "  marital_status TEXT," +
            "  occupation TEXT," +
            "  employer_name TEXT," +
            "  employer_address TEXT," +
            "  password TEXT" +
            ")";
        Statement st = con.createStatement();
        st.execute(sql);
        st.close();
    }

    /**
     * Creates the customer_transactions table.
     */
    private void createTransactionTable(Connection con) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS customer_transactions (" +
            "  transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  customer_ssn_id TEXT," +
            "  customer_name TEXT," +
            "  account_number TEXT," +
            "  ifsc_code TEXT," +
            "  account_balance REAL," +
            "  aadhar_card_no TEXT," +
            "  pan_card_no TEXT," +
            "  date TEXT," +
            "  contact_number TEXT," +
            "  mode_of_transaction TEXT," +
            "  amount REAL," +
            "  credit_debit TEXT," +
            "  FOREIGN KEY(customer_ssn_id) REFERENCES customer(customer_ssn_id)" +
            ")";
        Statement st = con.createStatement();
        st.execute(sql);
        st.close();
    }

    /**
     * Creates the loan table.
     */
    private void createLoanTable(Connection con) throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS loan (" +
            "  loan_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  customer_ssn_id TEXT," +
            "  customer_name TEXT," +
            "  loan_amount REAL," +
            "  length_of_loan INTEGER," +
            "  loan_type TEXT," +
            "  status TEXT," +
            "  FOREIGN KEY(customer_ssn_id) REFERENCES customer(customer_ssn_id)" +
            ")";
        Statement st = con.createStatement();
        st.execute(sql);
        st.close();
    }

    /* -------------------- Demo seeding helpers -------------------- */

    /**
     * Returns true if a table already has rows.
     */
    private boolean hasRows(Connection con, String tableName) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tableName);
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        rs.close();
        st.close();
        return count > 0;
    }

    /**
     * Inserts 10 demo employees if employee table is empty.
     * employee_id auto-increments from 1000001 (start) to keep them 7 digits.
     */
    private void seedEmployees(Connection con) throws SQLException {
        if (hasRows(con, "employee")) {
            return; // already seeded
        }

        // First force the autoincrement sequence to start at 1000000 so IDs are 7 digits
        Statement seqSt = con.createStatement();
        seqSt.execute(
            "INSERT OR REPLACE INTO sqlite_sequence(name, seq) VALUES('employee', 1000000)"
        );
        seqSt.close();

        // Employee data: {firstName, lastName, email, contact, address, password, designation, salary}
        String[][] data = {
            {"Aarav", "Sharma", "aarav.sharma@bank.com", "9876543210", "Pune, MH", "pass123", "Clerk", "25000"},
            {"Priya", "Patel", "priya.patel@bank.com", "9876543211", "Mumbai, MH", "pass123", "Manager", "75000"},
            {"Rohan", "Kumar", "rohan.kumar@bank.com", "9876543212", "Delhi", "pass123", "Accountant", "55000"},
            {"Sneha", "Reddy", "sneha.reddy@bank.com", "9876543213", "Hyderabad, TS", "pass123", "Clerk", "26000"},
            {"Vikram", "Singh", "vikram.singh@bank.com", "9876543214", "Bangalore, KA", "pass123", "Manager", "80000"},
            {"Anita", "Joshi", "anita.joshi@bank.com", "9876543215", "Pune, MH", "pass123", "Clerk", "24000"},
            {"Karan", "Verma", "karan.verma@bank.com", "9876543216", "Chennai, TN", "pass123", "Accountant", "58000"},
            {"Pooja", "Gupta", "pooja.gupta@bank.com", "9876543217", "Kolkata, WB", "pass123", "Manager", "78000"},
            {"Arjun", "Nair", "arjun.nair@bank.com", "9876543218", "Kochi, KL", "pass123", "Clerk", "27000"},
            {"Divya", "Iyer", "divya.iyer@bank.com", "9876543219", "Ahmedabad, GJ", "pass123", "Accountant", "60000"}
        };

        String sql =
            "INSERT INTO employee (first_name, last_name, email, contact_number, address, " +
            "password, designation, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            ps.setString(1, data[i][0]);
            ps.setString(2, data[i][1]);
            ps.setString(3, data[i][2]);
            ps.setString(4, data[i][3]);
            ps.setString(5, data[i][4]);
            ps.setString(6, data[i][5]);
            ps.setString(7, data[i][6]);
            ps.setDouble(8, Double.parseDouble(data[i][7]));
            ps.executeUpdate();
        }
        ps.close();
        System.out.println("[DatabaseInitializer] Inserted 10 demo employees.");
    }

    /**
     * Inserts 10 demo customers if customer table is empty.
     */
    private void seedCustomers(Connection con) throws SQLException {
        if (hasRows(con, "customer")) {
            return;
        }

        // Customer rows: 19 columns matching the customer table.
        String[][] data = {
            {"1800001", "Rahul", "Varma", "rahul.v@mail.com", "1990-05-12", "Pune, MH", "9000000001",
             "111122223331", "ABCDE1234A", "ACC1000000001", "BANK0001", "30000.0",
             "Savings", "M", "Single", "Software Engineer", "TCS", "Pune Tech Park", "cust123"},
            {"1800002", "Sita", "Raman", "sita.r@mail.com", "1985-09-22", "Mumbai, MH", "9000000002",
             "111122223332", "ABCDE1234B", "ACC1000000002", "BANK0001", "50000.0",
             "Joint", "F", "Married", "Doctor", "Apollo", "Mumbai Hospital", "cust123"},
            {"1900001", "Sheetal", "Patil", "sheetal.p@mail.com", "1992-07-20", "Delhi", "9000000003",
             "111122223333", "ABCDE1234C", "ACC1000000003", "BANK0002", "40000.0",
             "Current", "F", "Single", "Business Owner", "Self", "Delhi Office", "cust123"},
            {"1900002", "Pooja", "Patil", "pooja.p@mail.com", "1988-08-04", "Hyderabad, TS", "9000000004",
             "111122223334", "ABCDE1234D", "ACC1000000004", "BANK0002", "70000.0",
             "Salary", "F", "Married", "Manager", "Infosys", "Hyderabad Campus", "cust123"},
            {"2000001", "Rahul", "Sharma", "rahul.s@mail.com", "1995-08-20", "Bangalore, KA", "9000000005",
             "111122223335", "ABCDE1234E", "ACC1000000005", "BANK0003", "20000.0",
             "Savings", "M", "Single", "Designer", "Wipro", "Bangalore Tech", "cust123"},
            {"2100001", "Pooja", "Srikari", "pooja.sri@mail.com", "1993-08-20", "Chennai, TN", "9000000006",
             "111122223336", "ABCDE1234F", "ACC1000000006", "BANK0003", "25000.0",
             "Salary", "F", "Single", "Tester", "Cognizant", "Chennai DLF", "cust123"},
            {"2100002", "Pooja", "Rewa", "pooja.r@mail.com", "1994-08-20", "Kolkata, WB", "9000000007",
             "111122223337", "ABCDE1234G", "ACC1000000007", "BANK0004", "35000.0",
             "Joint", "F", "Married", "Teacher", "DAV School", "Kolkata Park", "cust123"},
            {"2100003", "Dan", "Stewart", "dan.s@mail.com", "1980-08-04", "Kochi, KL", "9000000008",
             "111122223338", "ABCDE1234H", "ACC1000000008", "BANK0004", "35000.0",
             "Current", "M", "Married", "Consultant", "Self", "Kochi Marine", "cust123"},
            {"2800001", "Sia", "R", "sia.r@mail.com", "2000-09-21", "Ahmedabad, GJ", "9000000009",
             "111122223339", "ABCDE1234I", "ACC1000000009", "BANK0005", "30000.0",
             "Savings", "F", "Single", "Student", "N/A", "N/A", "cust123"},
            {"2200001", "Sonali", "G", "sonali.g@mail.com", "1991-05-01", "Pune, MH", "9000000010",
             "111122223340", "ABCDE1234J", "ACC1000000010", "BANK0005", "15000.0",
             "Savings", "F", "Married", "HR Executive", "Capgemini", "Pune Hinjewadi", "cust123"}
        };

        String sql =
            "INSERT INTO customer (customer_ssn_id, first_name, last_name, email, date_of_birth, " +
            "address, contact_number, aadhar_number, pan_number, account_number, ifsc_code, " +
            "account_balance, account_type, gender, marital_status, occupation, employer_name, " +
            "employer_address, password) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            ps.setString(1, data[i][0]);
            ps.setString(2, data[i][1]);
            ps.setString(3, data[i][2]);
            ps.setString(4, data[i][3]);
            ps.setString(5, data[i][4]);
            ps.setString(6, data[i][5]);
            ps.setString(7, data[i][6]);
            ps.setString(8, data[i][7]);
            ps.setString(9, data[i][8]);
            ps.setString(10, data[i][9]);
            ps.setString(11, data[i][10]);
            ps.setDouble(12, Double.parseDouble(data[i][11]));
            ps.setString(13, data[i][12]);
            ps.setString(14, data[i][13]);
            ps.setString(15, data[i][14]);
            ps.setString(16, data[i][15]);
            ps.setString(17, data[i][16]);
            ps.setString(18, data[i][17]);
            ps.setString(19, data[i][18]);
            ps.executeUpdate();
        }
        ps.close();
        System.out.println("[DatabaseInitializer] Inserted 10 demo customers.");
    }

    /**
     * Inserts 10 demo transactions if customer_transactions table is empty.
     */
    private void seedTransactions(Connection con) throws SQLException {
        if (hasRows(con, "customer_transactions")) {
            return;
        }

        // {ssn, name, accountNumber, ifsc, balance, aadhar, pan, date, contact, mode, amount, credit_debit}
        String[][] data = {
            {"1800001", "Rahul Varma", "ACC1000000001", "BANK0001", "30000.0", "111122223331", "ABCDE1234A", "2024-01-15", "9000000001", "ONLINE", "5000", "Credit"},
            {"1800002", "Sita Raman", "ACC1000000002", "BANK0001", "50000.0", "111122223332", "ABCDE1234B", "2024-01-16", "9000000002", "CHEQUE", "2000", "Debit"},
            {"1900001", "Sheetal Patil", "ACC1000000003", "BANK0002", "40000.0", "111122223333", "ABCDE1234C", "2024-01-17", "9000000003", "ATM", "1500", "Debit"},
            {"1900002", "Pooja Patil", "ACC1000000004", "BANK0002", "70000.0", "111122223334", "ABCDE1234D", "2024-01-18", "9000000004", "ONLINE", "10000", "Credit"},
            {"2000001", "Rahul Sharma", "ACC1000000005", "BANK0003", "20000.0", "111122223335", "ABCDE1234E", "2024-01-19", "9000000005", "CASH", "3000", "Credit"},
            {"2100001", "Pooja Srikari", "ACC1000000006", "BANK0003", "25000.0", "111122223336", "ABCDE1234F", "2024-01-20", "9000000006", "ONLINE", "500", "Debit"},
            {"2100002", "Pooja Rewa", "ACC1000000007", "BANK0004", "35000.0", "111122223337", "ABCDE1234G", "2024-01-21", "9000000007", "CHEQUE", "7500", "Credit"},
            {"2100003", "Dan Stewart", "ACC1000000008", "BANK0004", "35000.0", "111122223338", "ABCDE1234H", "2024-01-22", "9000000008", "ATM", "2500", "Debit"},
            {"2800001", "Sia R", "ACC1000000009", "BANK0005", "30000.0", "111122223339", "ABCDE1234I", "2024-01-23", "9000000009", "ONLINE", "1000", "Debit"},
            {"2200001", "Sonali G", "ACC1000000010", "BANK0005", "15000.0", "111122223340", "ABCDE1234J", "2024-01-24", "9000000010", "CASH", "5000", "Credit"}
        };

        String sql =
            "INSERT INTO customer_transactions (customer_ssn_id, customer_name, account_number, " +
            "ifsc_code, account_balance, aadhar_card_no, pan_card_no, date, contact_number, " +
            "mode_of_transaction, amount, credit_debit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            ps.setString(1, data[i][0]);
            ps.setString(2, data[i][1]);
            ps.setString(3, data[i][2]);
            ps.setString(4, data[i][3]);
            ps.setDouble(5, Double.parseDouble(data[i][4]));
            ps.setString(6, data[i][5]);
            ps.setString(7, data[i][6]);
            ps.setString(8, data[i][7]);
            ps.setString(9, data[i][8]);
            ps.setString(10, data[i][9]);
            ps.setDouble(11, Double.parseDouble(data[i][10]));
            ps.setString(12, data[i][11]);
            ps.executeUpdate();
        }
        ps.close();
        System.out.println("[DatabaseInitializer] Inserted 10 demo transactions.");
    }

    /**
     * Inserts 10 demo loan records if loan table is empty.
     */
    private void seedLoans(Connection con) throws SQLException {
        if (hasRows(con, "loan")) {
            return;
        }

        // {ssn, name, amount, lengthMonths, type, status}
        String[][] data = {
            {"1800001", "Rahul Varma", "200000", "24", "Personal", "Pending"},
            {"1800002", "Sita Raman", "500000", "60", "Home", "Approved"},
            {"1900001", "Sheetal Patil", "150000", "12", "Education", "Approved"},
            {"1900002", "Pooja Patil", "300000", "36", "Car", "Pending"},
            {"2000001", "Rahul Sharma", "100000", "12", "Personal", "Rejected"},
            {"2100001", "Pooja Srikari", "250000", "24", "Personal", "Approved"},
            {"2100002", "Pooja Rewa", "800000", "120", "Home", "Pending"},
            {"2100003", "Dan Stewart", "400000", "48", "Car", "Approved"},
            {"2800001", "Sia R", "75000", "12", "Education", "Pending"},
            {"2200001", "Sonali G", "120000", "18", "Personal", "Rejected"}
        };

        String sql =
            "INSERT INTO loan (customer_ssn_id, customer_name, loan_amount, length_of_loan, " +
            "loan_type, status) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < data.length; i++) {
            ps.setString(1, data[i][0]);
            ps.setString(2, data[i][1]);
            ps.setDouble(3, Double.parseDouble(data[i][2]));
            ps.setInt(4, Integer.parseInt(data[i][3]));
            ps.setString(5, data[i][4]);
            ps.setString(6, data[i][5]);
            ps.executeUpdate();
        }
        ps.close();
        System.out.println("[DatabaseInitializer] Inserted 10 demo loans.");
    }

    /* -------------------- Salary update helpers (DB modules US002 / US003) -------------------- */

    /**
     * Adds Rs.1000 to every Clerk's salary (idempotent guard: only run once via a marker).
     */
    private void applyClerkRaise(Connection con) throws SQLException {
        // Guard with a marker table so this only runs once per fresh DB.
        Statement st = con.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS init_flags (flag_name TEXT PRIMARY KEY)");
        ResultSet rs = st.executeQuery("SELECT flag_name FROM init_flags WHERE flag_name='clerk_raise'");
        boolean alreadyRan = rs.next();
        rs.close();

        if (!alreadyRan) {
            // Add 1000 to each Clerk
            st.execute("UPDATE employee SET salary = salary + 1000 WHERE designation = 'Clerk'");
            st.execute("INSERT INTO init_flags(flag_name) VALUES('clerk_raise')");
            System.out.println("[DatabaseInitializer] Applied +1000 raise to all Clerks.");
        }
        st.close();
    }

    /**
     * Adds 10% of salary to every Manager (idempotent via marker).
     */
    private void applyManagerRaise(Connection con) throws SQLException {
        Statement st = con.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS init_flags (flag_name TEXT PRIMARY KEY)");
        ResultSet rs = st.executeQuery("SELECT flag_name FROM init_flags WHERE flag_name='manager_raise'");
        boolean alreadyRan = rs.next();
        rs.close();

        if (!alreadyRan) {
            // Add 10% of salary to each Manager
            st.execute("UPDATE employee SET salary = salary + (salary * 0.10) WHERE designation = 'Manager'");
            st.execute("INSERT INTO init_flags(flag_name) VALUES('manager_raise')");
            System.out.println("[DatabaseInitializer] Applied +10% raise to all Managers.");
        }
        st.close();
    }
}
