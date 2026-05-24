package com.bank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.EmployeeDAO;
import com.bank.model.Employee;

/**
 * Manager-only page that lists every employee plus a total count.
 *
 * Access rules:
 *   - Must have a logged-in Employee in the session.
 *   - That Employee's designation must be "Manager".
 *   - Anyone else gets redirected to /home.jsp with a flash error.
 */
public class EmployeeListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EmployeeDAO dao = new EmployeeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: must be logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInEmployee") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Step 2: must be a Manager
        Employee me = (Employee) session.getAttribute("loggedInEmployee");
        if (!"Manager".equalsIgnoreCase(me.getDesignation())) {
            session.setAttribute("flashError",
                "Only Managers can access the Employees page.");
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            return;
        }

        // Step 3: load the list and the total count
        List<Employee> employees = dao.findAll();
        int total = dao.count();

        request.setAttribute("employees", employees);
        request.setAttribute("totalEmployees", total);
        request.getRequestDispatcher("/employee_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
