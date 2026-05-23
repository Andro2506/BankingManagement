package com.bank.javadb;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * JAVA / DB MODULE - US001 (Customer Data Management - Collection).
 *
 * Manages customer accounts in memory using an ArrayList. Provides:
 *   - add a customer
 *   - remove a customer
 *   - retrieve a customer by account number
 *   - count total customers
 *   - view all customers
 *
 * Run as a console program:  java com.bank.javadb.CustomerCollectionManager
 */
public class CustomerCollectionManager {

    /**
     * Simple inner class holding the fields required by US001.
     * Kept inside the same file to make it easy for a beginner to follow.
     */
    public static class CustomerRecord {
        public String ssn;            // 7 digits
        public String name;           // up to 50
        public String email;
        public String address;        // up to 100
        public String contactNumber;  // 10 digits
        public String aadharNumber;   // 12 digits
        public String panNumber;      // 10 chars
        public String accountNumber;  // up to 20
        public double initialDeposit;

        public CustomerRecord(String ssn, String name, String email, String address,
                              String contactNumber, String aadharNumber, String panNumber,
                              String accountNumber, double initialDeposit) {
            this.ssn = ssn;
            this.name = name;
            this.email = email;
            this.address = address;
            this.contactNumber = contactNumber;
            this.aadharNumber = aadharNumber;
            this.panNumber = panNumber;
            this.accountNumber = accountNumber;
            this.initialDeposit = initialDeposit;
        }

        @Override
        public String toString() {
            return "[SSN=" + ssn + ", Name=" + name + ", Email=" + email +
                   ", Account=" + accountNumber + ", Deposit=" + initialDeposit + "]";
        }
    }

    // The collection that holds every customer record in memory
    private List<CustomerRecord> customers = new ArrayList<CustomerRecord>();

    /** Add a new customer to the list. */
    public void addCustomer(CustomerRecord c) {
        customers.add(c);
        System.out.println("Customer added: " + c);
    }

    /** Remove a customer by account number. Returns true if removed. */
    public boolean removeCustomer(String accountNumber) {
        // Plain for-loop and if-else as requested - no streams.
        for (int i = 0; i < customers.size(); i++) {
            CustomerRecord c = customers.get(i);
            if (c.accountNumber.equals(accountNumber)) {
                customers.remove(i);
                System.out.println("Removed customer with account: " + accountNumber);
                return true;
            }
        }
        System.out.println("No customer found with account: " + accountNumber);
        return false;
    }

    /** Find and return a customer by account number, or null if missing. */
    public CustomerRecord retrieveByAccount(String accountNumber) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).accountNumber.equals(accountNumber)) {
                return customers.get(i);
            }
        }
        return null;
    }

    /** Total number of customers stored. */
    public int countTotal() {
        return customers.size();
    }

    /** Print every customer to the console. */
    public void viewAll() {
        if (customers.isEmpty()) {
            System.out.println("No customers in the list.");
            return;
        }
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i));
        }
    }

    /**
     * Console driver: lets the user pick an option in a loop.
     */
    public static void main(String[] args) {
        CustomerCollectionManager mgr = new CustomerCollectionManager();
        Scanner sc = new Scanner(System.in);

        // Pre-load a couple of records so menu is useful immediately
        mgr.addCustomer(new CustomerRecord("1234567", "Demo One", "one@mail.com",
            "Pune", "9000000001", "111122223331", "ABCDE1234A", "ACC0000001", 5000));
        mgr.addCustomer(new CustomerRecord("7654321", "Demo Two", "two@mail.com",
            "Mumbai", "9000000002", "111122223332", "ABCDE1234B", "ACC0000002", 8000));

        while (true) {
            System.out.println();
            System.out.println("---- Customer Collection Manager ----");
            System.out.println("1. Add customer");
            System.out.println("2. Remove customer (by account number)");
            System.out.println("3. Retrieve customer (by account number)");
            System.out.println("4. Count total customers");
            System.out.println("5. View all");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            String choice = sc.nextLine();
            if ("1".equals(choice)) {
                System.out.print("SSN (7 digits): "); String ssn = sc.nextLine();
                System.out.print("Name: "); String name = sc.nextLine();
                System.out.print("Email: "); String email = sc.nextLine();
                System.out.print("Address: "); String addr = sc.nextLine();
                System.out.print("Contact (10 digits): "); String ph = sc.nextLine();
                System.out.print("Aadhar (12 digits): "); String adh = sc.nextLine();
                System.out.print("PAN (10 chars): "); String pan = sc.nextLine();
                System.out.print("Account Number: "); String acc = sc.nextLine();
                System.out.print("Initial Deposit: "); String dep = sc.nextLine();
                double deposit = 0.0;
                try { deposit = Double.parseDouble(dep); } catch (Exception ex) {
                    System.out.println("Invalid number, using 0");
                }
                mgr.addCustomer(new CustomerRecord(ssn, name, email, addr, ph, adh, pan, acc, deposit));
            } else if ("2".equals(choice)) {
                System.out.print("Account number: "); String acc = sc.nextLine();
                mgr.removeCustomer(acc);
            } else if ("3".equals(choice)) {
                System.out.print("Account number: "); String acc = sc.nextLine();
                CustomerRecord c = mgr.retrieveByAccount(acc);
                System.out.println(c == null ? "Not found." : c);
            } else if ("4".equals(choice)) {
                System.out.println("Total customers: " + mgr.countTotal());
            } else if ("5".equals(choice)) {
                mgr.viewAll();
            } else if ("6".equals(choice)) {
                System.out.println("Bye.");
                sc.close();
                return;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
}
