package com.bank.model;

/**
 * Plain Old Java Object (POJO) representing one row in the employee table.
 * Each field has a private member, a getter, and a setter.
 */
public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String address;
    private String password;
    private String designation;
    private double salary;

    // Default no-arg constructor (required by some frameworks)
    public Employee() {
    }

    // Convenience constructor used during registration
    public Employee(String firstName, String lastName, String email,
                    String contactNumber, String address, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.password = password;
    }

    // ----- Getters and setters below -----

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    // Convenience method: full name = first + last
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
