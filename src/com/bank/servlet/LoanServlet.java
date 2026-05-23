package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.CustomerDAO;
import com.bank.dao.LoanDAO;
import com.bank.model.Customer;
import com.bank.model.Loan;

/**
 * US006 (loan part): Employee initiates a new loan request for a customer.
 *
 * GET  -> show loan.jsp form
 * POST -> validate input, save loan record (status default 'Pending').
 */
public class LoanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LoanDAO loanDao = new LoanDAO();
    private CustomerDAO custDao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        request.getRequestDispatcher("/loan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String ssn = request.getParameter("customerSsnId");
        String customerName = request.getParameter("customerName");
        String amountStr = request.getParameter("loanAmount");
        String lengthStr = request.getParameter("lengthOfLoan");
        String loanType = request.getParameter("loanType");

        // Validate
        Customer cust = custDao.findBySsn(ssn);
        if (cust == null) {
            request.setAttribute("error", "No customer found with SSN: " + ssn);
            request.getRequestDispatcher("/loan.jsp").forward(request, response);
            return;
        }

        double amount;
        int length;
        try {
            amount = Double.parseDouble(amountStr);
            length = Integer.parseInt(lengthStr);
            if (amount <= 0 || length <= 0) {
                request.setAttribute("error",
                    "Loan amount and duration must be greater than zero.");
                request.getRequestDispatcher("/loan.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Amount and duration must be numeric.");
            request.getRequestDispatcher("/loan.jsp").forward(request, response);
            return;
        }

        // If the form did not include a name, use the customer's full name
        if (customerName == null || customerName.trim().isEmpty()) {
            customerName = cust.getFullName();
        }

        // Build and save the loan
        Loan loan = new Loan();
        loan.setCustomerSsnId(ssn);
        loan.setCustomerName(customerName);
        loan.setLoanAmount(amount);
        loan.setLengthOfLoan(length);
        loan.setLoanType(loanType);
        loan.setStatus("Pending");

        int newId = loanDao.insert(loan);
        if (newId == -1) {
            request.setAttribute("error", "Failed to save loan request.");
        } else {
            loan.setLoanId(newId);
            request.setAttribute("savedLoan", loan);
            request.setAttribute("success", "Loan request submitted (ID: " + newId + ").");
        }
        request.getRequestDispatcher("/loan.jsp").forward(request, response);
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInEmployee") != null;
    }
}
