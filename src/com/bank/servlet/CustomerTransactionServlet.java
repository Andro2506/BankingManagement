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
 * Allows a logged-in customer to make a transaction (deposit / withdraw)
 * on their own account.
 *
 * Compared to the employee TransactionServlet, this version:
 *   - Does NOT ask for a Customer SSN (uses the one in the session).
 *   - Only the logged-in customer's own balance can be changed.
 *   - Block insufficient funds for Debit operations.
 */
public class CustomerTransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TransactionDAO txDao = new TransactionDAO();
    private CustomerDAO custDao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Make sure a customer is logged in
        if (!isCustomerLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
            return;
        }
        // Just render the transaction form
        request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Auth check
        if (!isCustomerLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
            return;
        }

        // Pull the logged-in customer from the session
        HttpSession session = request.getSession(false);
        Customer sessionCust = (Customer) session.getAttribute("loggedInCustomer");

        // Re-load fresh data from DB so we always work with the current balance
        Customer cust = custDao.findBySsn(sessionCust.getCustomerSsnId());
        if (cust == null) {
            request.setAttribute("error", "Your customer record could not be found.");
            request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
            return;
        }

        // Read form inputs
        String mode = request.getParameter("modeOfTransaction");
        String creditDebit = request.getParameter("creditDebit");
        String amountStr = request.getParameter("amount");

        // Validate amount
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than zero.");
                request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Amount must be a valid number.");
            request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
            return;
        }

        // Compute new balance based on Credit (deposit) or Debit (withdraw)
        double newBalance = cust.getAccountBalance();
        if ("Credit".equalsIgnoreCase(creditDebit)) {
            // Deposit
            newBalance = newBalance + amount;
        } else if ("Debit".equalsIgnoreCase(creditDebit)) {
            // Withdraw - block if insufficient funds
            if (amount > cust.getAccountBalance()) {
                request.setAttribute("error",
                    "Insufficient funds. Your current balance is Rs. " + cust.getAccountBalance());
                request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
                return;
            }
            newBalance = newBalance - amount;
        } else {
            request.setAttribute("error", "Please choose Credit (deposit) or Debit (withdraw).");
            request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
            return;
        }

        // Build the Transaction record
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

        // Save the transaction
        int newTxId = txDao.insert(t);
        if (newTxId == -1) {
            request.setAttribute("error", "Failed to record transaction. Please try again.");
            request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
            return;
        }

        // Update the customer's balance in the customer table
        custDao.updateBalance(cust.getCustomerSsnId(), newBalance);

        // Refresh the session copy so dashboard shows new balance
        cust.setAccountBalance(newBalance);
        session.setAttribute("loggedInCustomer", cust);

        // Show success
        t.setTransactionId(newTxId);
        request.setAttribute("savedTransaction", t);
        request.setAttribute("success",
            "Transaction recorded successfully. New balance: Rs. " + newBalance);
        request.getRequestDispatcher("/customer_transaction.jsp").forward(request, response);
    }

    /**
     * Returns true if a Customer object is in the session.
     */
    private boolean isCustomerLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInCustomer") != null;
    }
}
