package com.bank.model;

/**
 * POJO representing one row in the customer table.
 */
public class Customer {

    private String customerSsnId;     // 7-digit primary key
    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;       // YYYY-MM-DD
    private String address;
    private String contactNumber;
    private String aadharNumber;
    private String panNumber;
    private String accountNumber;
    private String ifscCode;
    private double accountBalance;
    private String accountType;       // Current/Savings/Salary/Joint
    private String gender;            // M/F
    private String maritalStatus;
    private String occupation;
    private String employerName;
    private String employerAddress;
    private String password;

    public Customer() {
    }

    // ----- Getters and setters -----

    public String getCustomerSsnId() { return customerSsnId; }
    public void setCustomerSsnId(String customerSsnId) { this.customerSsnId = customerSsnId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public String getEmployerAddress() { return employerAddress; }
    public void setEmployerAddress(String employerAddress) { this.employerAddress = employerAddress; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Helper: full name = first + last
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
