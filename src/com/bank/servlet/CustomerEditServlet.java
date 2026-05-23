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
 * US004: Employee edits customer info.
 *
 * GET ?ssn=XXXXXXX -> load existing customer, show form pre-filled, SSN read-only.
 * POST              -> save updates, show success/error.
 */
public class CustomerEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CustomerDAO dao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String ssn = request.getParameter("ssn");
        if (ssn == null || ssn.trim().isEmpty()) {
            request.setAttribute("error", "Missing customer SSN.");
            request.getRequestDispatcher("/customer_list.jsp").forward(request, response);
            return;
        }

        Customer c = dao.findBySsn(ssn);
        if (c == null) {
            request.setAttribute("error", "Customer not found for SSN: " + ssn);
            request.getRequestDispatcher("/customer_list.jsp").forward(request, response);
            return;
        }

        request.setAttribute("customer", c);
        request.getRequestDispatcher("/customer_edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // SSN comes back from a hidden/disabled field; we re-fetch and update other columns
        String ssn = request.getParameter("customerSsnId");
        Customer c = dao.findBySsn(ssn);
        if (c == null) {
            request.setAttribute("error", "Customer not found for SSN: " + ssn);
            request.getRequestDispatcher("/customer_list.jsp").forward(request, response);
            return;
        }

        // Apply form changes (SSN itself is NOT editable)
        c.setFirstName(request.getParameter("firstName"));
        c.setLastName(request.getParameter("lastName"));
        c.setEmail(request.getParameter("email"));
        c.setDateOfBirth(request.getParameter("dateOfBirth"));
        c.setAddress(request.getParameter("address"));
        c.setContactNumber(request.getParameter("contactNumber"));
        c.setAadharNumber(request.getParameter("aadharNumber"));
        c.setPanNumber(request.getParameter("panNumber"));
        c.setAccountNumber(request.getParameter("accountNumber"));
        c.setIfscCode(request.getParameter("ifscCode"));

        // Parse balance safely
        String balStr = request.getParameter("accountBalance");
        try {
            c.setAccountBalance(Double.parseDouble(balStr));
        } catch (Exception e) {
            request.setAttribute("error", "Account Balance must be a valid number.");
            request.setAttribute("customer", c);
            request.getRequestDispatcher("/customer_edit.jsp").forward(request, response);
            return;
        }

        c.setAccountType(request.getParameter("accountType"));
        c.setGender(request.getParameter("gender"));
        c.setMaritalStatus(request.getParameter("maritalStatus"));
        c.setOccupation(request.getParameter("occupation"));
        c.setEmployerName(request.getParameter("employerName"));
        c.setEmployerAddress(request.getParameter("employerAddress"));

        boolean ok = dao.update(c);
        if (ok) {
            request.setAttribute("success", "Customer updated successfully.");
        } else {
            request.setAttribute("error", "Failed to update customer.");
        }
        request.setAttribute("customer", c);
        request.getRequestDispatcher("/customer_edit.jsp").forward(request, response);
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInEmployee") != null;
    }
}
