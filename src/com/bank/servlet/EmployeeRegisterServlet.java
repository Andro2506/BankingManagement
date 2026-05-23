package com.bank.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.dao.EmployeeDAO;
import com.bank.model.Employee;

/**
 * Handles employee registration (US001).
 *
 * GET  -> show register.jsp
 * POST -> validate inputs, save via DAO, then forward to a success page that
 *         shows the auto-generated 7-digit Employee ID, name and email.
 */
public class EmployeeRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private EmployeeDAO dao = new EmployeeDAO();

    /**
     * GET: just show the registration form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/register.jsp");
        rd.forward(request, response);
    }

    /**
     * POST: read form data, validate, insert, show acknowledgment.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1) Read every field the form sends
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String contactNumber = request.getParameter("contactNumber");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 2) Server-side validation - keep simple if/else
        String errorMessage = null;

        if (firstName == null || firstName.trim().isEmpty()) {
            errorMessage = "First Name is required.";
        } else if (lastName == null || lastName.trim().isEmpty()) {
            errorMessage = "Last Name is required.";
        } else if (email == null || !email.contains("@")) {
            errorMessage = "A valid Email is required.";
        } else if (contactNumber == null || contactNumber.length() != 10) {
            errorMessage = "Contact Number must be exactly 10 digits.";
        } else if (address == null || address.trim().isEmpty()) {
            errorMessage = "Address is required.";
        } else if (password == null || password.length() < 4) {
            errorMessage = "Password must be at least 4 characters.";
        } else if (!password.equals(confirmPassword)) {
            errorMessage = "Password and Confirm Password do not match.";
        }

        if (errorMessage != null) {
            // Send the user back to the form with the error message
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 3) Build an Employee object from the inputs
        Employee emp = new Employee();
        emp.setFirstName(firstName.trim());
        emp.setLastName(lastName.trim());
        emp.setEmail(email.trim());
        emp.setContactNumber(contactNumber.trim());
        emp.setAddress(address.trim());
        emp.setPassword(password);
        // Default designation/salary applied inside DAO

        // 4) Save
        int newId = dao.register(emp);

        if (newId == -1) {
            request.setAttribute("error",
                "Registration failed. Email may already be in use. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 5) Show acknowledgment page
        emp.setEmployeeId(newId);
        request.setAttribute("registeredEmployee", emp);
        request.getRequestDispatcher("/register_success.jsp").forward(request, response);
    }
}
