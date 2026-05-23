package com.bank.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.CustomerDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.Customer;
import com.bank.model.Transaction;

/**
 * US006: Employee captures a transaction for a customer.
 *
 * GET  -> show transaction.jsp (employee picks customer SSN, fills info)
 * POST -> validate, insert into customer_transactions, also adjust the customer's
 *         account_balance (Credit adds, Debit subtracts) - block if insufficient funds.
 */
public class TransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TransactionDAO txDao = new TransactionDAO();
    private CustomerDAO custDao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/transaction.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Read parameters
        String ssn = request.getParameter("customerSsnId");
        String mode = request.getParameter("modeOfTransaction");
        String creditDebit = request.getParameter("creditDebit");
        String amountStr = request.getParameter("amount");

        // Find the customer first - we need their account info and balance
        Customer cust = custDao.findBySsn(ssn);
        if (cust == null) {
            request.setAttribute("error", "No customer found with SSN: " + ssn);
            request.getRequestDispatcher("/transaction.jsp").forward(request, response);
            return;
        }

        // Parse amount
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than zero.");
                request.getRequestDispatcher("/transaction.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Amount must be a valid number.");
            request.getRequestDispatcher("/transaction.jsp").forward(request, response);
            return;
        }

        // Compute new balance based on Credit/Debit
        double newBalance = cust.getAccountBalance();
        if ("Credit".equalsIgnoreCase(creditDebit)) {
            newBalance = newBalance + amount;
        } else if ("Debit".equalsIgnoreCase(creditDebit)) {
            // Block insufficient funds
            if (amount > cust.getAccountBalance()) {
                request.setAttribute("error",
                    "Insufficient funds. Current balance is Rs. " + cust.getAccountBalance());
                request.getRequestDispatcher("/transaction.jsp").forward(request, response);
                return;
            }
            newBalance = newBalance - amount;
        } else {
            request.setAttribute("error", "Please choose Credit or Debit.");
            request.getRequestDispatcher("/transaction.jsp").forward(request, response);
            return;
        }

        // Build Transaction record
        Transaction t = new Transaction();
        t.setCustomerSsnId(cust.getCustomerSsnId());
        t.setCustomerName(cust.getFullName());
        t.setAccountNumber(cust.getAccountNumber());
        t.setIfscCode(cust.getIfscCode());
        t.setAccountBalance(newBalance);
        t.setAadharCardNo(cust.getAadharNumber());
        t.setPanCardNo(cust.getPanNumber());
        t.setContactNumber(cust.getContactNumber());
        t.setModeOfTransaction(mode);
        t.setAmount(amount);
        t.setCreditDebit(creditDebit);

        // Use today's date if user did not type one
        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.trim().isEmpty()) {
            dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        t.setDate(dateStr);

        // Save the transaction and update the customer's balance
        int newTxId = txDao.insert(t);
        if (newTxId == -1) {
            request.setAttribute("error", "Failed to record transaction.");
            request.getRequestDispatcher("/transaction.jsp").forward(request, response);
            return;
        }

        custDao.updateBalance(cust.getCustomerSsnId(), newBalance);

        // Show success
        t.setTransactionId(newTxId);
        request.setAttribute("success",
            "Transaction recorded successfully. New balance: Rs. " + newBalance);
        request.setAttribute("savedTransaction", t);
        request.getRequestDispatcher("/transaction.jsp").forward(request, response);
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInEmployee") != null;
    }
}
