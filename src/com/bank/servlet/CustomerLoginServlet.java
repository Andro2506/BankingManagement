package com.bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
 * Validates a Username (Customer SSN) + Password against the database.
 * Prints "Login Successful" or "Login Unsuccessful" in the browser.
 */
public class CustomerLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CustomerDAO dao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the JSP form
        request.getRequestDispatcher("/customer_login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username"); // SSN id
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Customer Login Result</title>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'>");
        out.println("</head><body class='p-4'>");

        Customer c = dao.login(username, password);
        if (c != null) {
            // Save in session for any follow-up pages
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedInCustomer", c);

            out.println("<div class='alert alert-success'><h2>Login Successful</h2>");
            out.println("Welcome, " + c.getFullName() + " (SSN: " + c.getCustomerSsnId() + ")");
            out.println("</div>");
        } else {
            out.println("<div class='alert alert-danger'><h2>Login Unsuccessful</h2>");
            out.println("Invalid username or password.");
            out.println("</div>");
        }
        out.println("<a href='customer_login.jsp' class='btn btn-primary'>Back</a>");
        out.println("</body></html>");
    }
}
