package com.bank.model;

/**
 * POJO representing one row in the customer_transactions table.
 */
public class Transaction {

    private int transactionId;
    private String customerSsnId;
    private String customerName;
    private String accountNumber;
    private String ifscCode;
    private double accountBalance;
    private String aadharCardNo;
    private String panCardNo;
    private String date;
    private String contactNumber;
    private String modeOfTransaction;
    private double amount;
    private String creditDebit;       // "Credit" or "Debit"

    public Transaction() {
    }

    // ----- Getters and setters -----

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getCustomerSsnId() { return customerSsnId; }
    public void setCustomerSsnId(String customerSsnId) { this.customerSsnId = customerSsnId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public String getAadharCardNo() { return aadharCardNo; }
    public void setAadharCardNo(String aadharCardNo) { this.aadharCardNo = aadharCardNo; }

    public String getPanCardNo() { return panCardNo; }
    public void setPanCardNo(String panCardNo) { this.panCardNo = panCardNo; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getModeOfTransaction() { return modeOfTransaction; }
    public void setModeOfTransaction(String modeOfTransaction) { this.modeOfTransaction = modeOfTransaction; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCreditDebit() { return creditDebit; }
    public void setCreditDebit(String creditDebit) { this.creditDebit = creditDebit; }
}
