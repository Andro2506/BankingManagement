package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logs the customer out by destroying the session, then redirects
 * back to the customer login page.
 */
public class CustomerLogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the existing session (do NOT create a new one)
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // wipes loggedInCustomer attribute
        }
        response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
