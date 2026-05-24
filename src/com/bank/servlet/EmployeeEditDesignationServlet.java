package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.EmployeeDAO;
import com.bank.model.Employee;

/**
 * Manager-only servlet that updates one employee's designation
 * (Clerk / Manager / Accountant). Form-posted from employee_list.jsp.
 *
 * Safety rules:
 *   - Only Managers can call this.
 *   - The new designation must be one of the three allowed values.
 *   - A Manager cannot demote themselves (would otherwise lock them out
 *     of this very page on the next click).
 */
public class EmployeeEditDesignationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EmployeeDAO dao = new EmployeeDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
                "Only Managers can change designations.");
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            return;
        }

        // Step 3: read form inputs
        String idStr = request.getParameter("employeeId");
        String newDesignation = request.getParameter("designation");

        // Step 4: validate inputs
        int targetId;
        try {
            targetId = Integer.parseInt(idStr);
        } catch (Exception e) {
            session.setAttribute("flashError", "Invalid employee id.");
            response.sendRedirect(request.getContextPath() + "/employees");
            return;
        }

        // Allowed values only - keeps the data clean.
        boolean validValue =
            "Clerk".equals(newDesignation) ||
            "Manager".equals(newDesignation) ||
            "Accountant".equals(newDesignation);
        if (!validValue) {
            session.setAttribute("flashError",
                "Designation must be Clerk, Manager, or Accountant.");
            response.sendRedirect(request.getContextPath() + "/employees");
            return;
        }

        // Step 5: prevent self-demotion - Manager would lose access otherwise.
        if (targetId == me.getEmployeeId() && !"Manager".equals(newDesignation)) {
            session.setAttribute("flashError",
                "You cannot change your own designation away from Manager.");
            response.sendRedirect(request.getContextPath() + "/employees");
            return;
        }

        // Step 6: do the update
        boolean ok = dao.updateDesignation(targetId, newDesignation);
        if (ok) {
            session.setAttribute("flashMessage",
                "Updated employee " + targetId + " designation to " + newDesignation + ".");
        } else {
            session.setAttribute("flashError",
                "Failed to update employee " + targetId + ".");
        }
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET should not be used for state changes; just bounce to the list.
        response.sendRedirect(request.getContextPath() + "/employees");
    }
}
