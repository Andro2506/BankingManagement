package com.bank.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.EmployeeDAO;
import com.bank.model.Employee;

/**
 * Handles employee login (US002).
 *
 * GET  -> show login.jsp
 * POST -> validate credentials, store Employee in session, redirect to home.jsp
 */
public class EmployeeLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EmployeeDAO dao = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Read inputs
        String idStr = request.getParameter("employeeId");
        String password = request.getParameter("password");

        // Basic validation
        if (idStr == null || idStr.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both Employee ID and Password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Employee ID must be a number.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Try to authenticate
        Employee emp = dao.login(employeeId, password);
        if (emp == null) {
            request.setAttribute("error", "Invalid Employee ID or Password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // Login success: store in session
        HttpSession session = request.getSession(true);
        session.setAttribute("loggedInEmployee", emp);
        session.setAttribute("loginSuccess", true);

        // Redirect to home page (PRG pattern)
        response.sendRedirect(request.getContextPath() + "/home.jsp");
    }
}
