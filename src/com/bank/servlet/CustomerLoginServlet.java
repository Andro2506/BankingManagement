package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

/**
 * Customer Login - Servlet (US002 from Servlet sheet).
 *
 * Validates a Username (Customer SSN) + Password against the customer table.
 * On success, stores the Customer in the session and redirects to the
 * customer dashboard page (customer_home.jsp) where the customer can
 * make transactions, apply for loans, and view their history.
 */
public class CustomerLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CustomerDAO dao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the JSP login form
        request.getRequestDispatcher("/customer_login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Read input fields
        String username = request.getParameter("username"); // SSN id
        String password = request.getParameter("password");

        // Basic server-side validation
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both Username and Password.");
            request.getRequestDispatcher("/customer_login.jsp").forward(request, response);
            return;
        }

        // Validate against the customer table
        Customer c = dao.login(username.trim(), password);

        if (c == null) {
            // Login failed -> show the form again with an error message
            request.setAttribute("error", "Login Unsuccessful. Invalid SSN or password.");
            request.getRequestDispatcher("/customer_login.jsp").forward(request, response);
            return;
        }

        // Login OK -> save the customer in the session and redirect to dashboard
        HttpSession session = request.getSession(true);
        session.setAttribute("loggedInCustomer", c);
        session.setAttribute("customerLoginSuccess", true);

        // PRG (Post-Redirect-Get) - prevents form re-submission on refresh
        response.sendRedirect(request.getContextPath() + "/customer_home.jsp");
    }
}
