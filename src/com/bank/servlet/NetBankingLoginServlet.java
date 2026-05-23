package com.bank.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * NetBanking Login - Servlet (US001 from Servlet sheet).
 *
 * Reads a single password text-box from an HTML form and compares it against
 * a hardcoded predefined string. Prints "Login Successful" or "Login Unsuccessful".
 */
public class NetBankingLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Predefined password (NOT stored using the servlet - hardcoded as required).
    private static final String PREDEFINED_PASSWORD = "netbank123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the simple HTML form
        request.getRequestDispatcher("/netbanking_login.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String entered = request.getParameter("password");

        // Print plain HTML using the servlet's PrintWriter
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>NetBanking Login Result</title>");
        out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css'>");
        out.println("</head><body class='p-4'>");

        // Compare entered password to the predefined string
        if (PREDEFINED_PASSWORD.equals(entered)) {
            out.println("<div class='alert alert-success'><h2>Login Successful</h2></div>");
        } else {
            out.println("<div class='alert alert-danger'><h2>Login Unsuccessful</h2></div>");
        }
        out.println("<a href='netbanking_login.html' class='btn btn-primary'>Back</a>");
        out.println("</body></html>");
    }
}
