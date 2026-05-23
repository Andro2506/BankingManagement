package com.bank.model;

/**
 * POJO representing one row in the loan table.
 */
public class Loan {

    private int loanId;
    private String customerSsnId;
    private String customerName;
    private double loanAmount;
    private int lengthOfLoan;     // duration in months
    private String loanType;      // Personal, Home, Car, Education, etc.
    private String status;        // Pending, Approved, Rejected

    public Loan() {
    }

    // ----- Getters and setters -----

    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public String getCustomerSsnId() { return customerSsnId; }
    public void setCustomerSsnId(String customerSsnId) { this.customerSsnId = customerSsnId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public double getLoanAmount() { return loanAmount; }
    public void setLoanAmount(double loanAmount) { this.loanAmount = loanAmount; }

    public int getLengthOfLoan() { return lengthOfLoan; }
    public void setLengthOfLoan(int lengthOfLoan) { this.lengthOfLoan = lengthOfLoan; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
