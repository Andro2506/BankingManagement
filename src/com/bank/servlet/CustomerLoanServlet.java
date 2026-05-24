package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.LoanDAO;
import com.bank.model.Customer;
import com.bank.model.Loan;

/**
 * Allows a logged-in customer to apply for a loan.
 *
 * GET  -> show the loan-application form (customer_loan.jsp).
 * POST -> validate inputs and insert into the loan table with status = "Pending".
 */
public class CustomerLoanServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LoanDAO loanDao = new LoanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Auth check
        if (!isCustomerLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
            return;
        }
        request.getRequestDispatcher("/customer_loan.jsp").forward(request, response);
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
        Customer cust = (Customer) session.getAttribute("loggedInCustomer");

        // Read form inputs
        String loanType = request.getParameter("loanType");
        String amountStr = request.getParameter("loanAmount");
        String lengthStr = request.getParameter("lengthOfLoan");

        // Validate
        if (loanType == null || loanType.trim().isEmpty()) {
            request.setAttribute("error", "Please pick a loan type.");
            request.getRequestDispatcher("/customer_loan.jsp").forward(request, response);
            return;
        }

        double amount;
        int length;
        try {
            amount = Double.parseDouble(amountStr);
            length = Integer.parseInt(lengthStr);
            if (amount <= 0 || length <= 0) {
                request.setAttribute("error",
                    "Loan amount and length must both be greater than zero.");
                request.getRequestDispatcher("/customer_loan.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Amount and length must be numeric.");
            request.getRequestDispatcher("/customer_loan.jsp").forward(request, response);
            return;
        }

        // Build the Loan record
        Loan loan = new Loan();
        loan.setCustomerSsnId(cust.getCustomerSsnId());
        loan.setCustomerName(cust.getFullName());
        loan.setLoanType(loanType);
        loan.setLoanAmount(amount);
        loan.setLengthOfLoan(length);
        loan.setStatus("Pending"); // every new application starts as Pending

        // Save it
        int newId = loanDao.insert(loan);
        if (newId == -1) {
            request.setAttribute("error", "Failed to submit loan application. Please try again.");
        } else {
            loan.setLoanId(newId);
            request.setAttribute("savedLoan", loan);
            request.setAttribute("success",
                "Loan application submitted (ID: " + newId + "). Status: Pending.");
        }
        request.getRequestDispatcher("/customer_loan.jsp").forward(request, response);
    }

    private boolean isCustomerLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInCustomer") != null;
    }
}
