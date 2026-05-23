package com.bank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

/**
 * US005: Employee deletes a customer.
 *
 * GET ?ssn=XXXXXXX  -> show confirmation page with full details.
 * POST              -> actually delete and redirect to list.
 */
public class CustomerDeleteServlet extends HttpServlet {
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
            request.setAttribute("error", "Missing SSN.");
            forwardToList(request, response);
            return;
        }

        Customer c = dao.findBySsn(ssn);
        if (c == null) {
            request.setAttribute("error", "Customer not found.");
            forwardToList(request, response);
            return;
        }

        request.setAttribute("customer", c);
        request.getRequestDispatcher("/customer_delete.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String ssn = request.getParameter("customerSsnId");
        boolean ok = dao.deleteBySsn(ssn);

        HttpSession session = request.getSession();
        if (ok) {
            session.setAttribute("flashMessage", "Customer " + ssn + " deleted successfully.");
        } else {
            session.setAttribute("flashError", "Failed to delete customer " + ssn + ".");
        }
        response.sendRedirect(request.getContextPath() + "/customers");
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInEmployee") != null;
    }

    private void forwardToList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> list = dao.findAll();
        request.setAttribute("customers", list);
        request.getRequestDispatcher("/customer_list.jsp").forward(request, response);
    }
}
