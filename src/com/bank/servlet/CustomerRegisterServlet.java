package com.bank.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

/**
 * US003: Employee registers a new customer.
 *
 * GET  -> show customer_register.jsp form
 * POST -> validate, save, then forward back with a success popup message.
 */
public class CustomerRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CustomerDAO dao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Require an employee to be logged in
        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher("/customer_register.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 1) Read all fields
        String ssn = request.getParameter("customerSsnId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String dob = request.getParameter("dateOfBirth");
        String address = request.getParameter("address");
        String contact = request.getParameter("contactNumber");
        String aadhar = request.getParameter("aadharNumber");
        String pan = request.getParameter("panNumber");
        String accountNumber = request.getParameter("accountNumber");
        String ifsc = request.getParameter("ifscCode");
        String balanceStr = request.getParameter("accountBalance");
        String accountType = request.getParameter("accountType");
        String gender = request.getParameter("gender");
        String marital = request.getParameter("maritalStatus");
        String occupation = request.getParameter("occupation");
        String employerName = request.getParameter("employerName");
        String employerAddress = request.getParameter("employerAddress");
        String password = request.getParameter("password");

        // 2) Validate
        String error = null;
        double balance = 0.0;

        if (ssn == null || ssn.length() != 7) {
            error = "Customer SSN must be exactly 7 digits.";
        } else if (firstName == null || firstName.trim().isEmpty()) {
            error = "First Name is required.";
        } else if (lastName == null || lastName.trim().isEmpty()) {
            error = "Last Name is required.";
        } else if (email == null || !email.contains("@")) {
            error = "A valid Email is required.";
        } else if (contact == null || contact.length() != 10) {
            error = "Contact Number must be 10 digits.";
        } else if (aadhar == null || aadhar.length() != 12) {
            error = "Aadhar Number must be 12 digits.";
        } else if (pan == null || pan.length() != 10) {
            error = "PAN Number must be 10 characters.";
        } else if (accountNumber == null || accountNumber.trim().isEmpty()) {
            error = "Account Number is required.";
        } else {
            try {
                balance = Double.parseDouble(balanceStr);
                if (balance < 0) {
                    error = "Account Balance cannot be negative.";
                }
            } catch (Exception e) {
                error = "Account Balance must be a valid number.";
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/customer_register.jsp").forward(request, response);
            return;
        }

        // 3) Build the Customer
        Customer c = new Customer();
        c.setCustomerSsnId(ssn);
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setEmail(email);
        c.setDateOfBirth(dob);
        c.setAddress(address);
        c.setContactNumber(contact);
        c.setAadharNumber(aadhar);
        c.setPanNumber(pan);
        c.setAccountNumber(accountNumber);
        c.setIfscCode(ifsc);
        c.setAccountBalance(balance);
        c.setAccountType(accountType);
        c.setGender(gender);
        c.setMaritalStatus(marital);
        c.setOccupation(occupation);
        c.setEmployerName(employerName);
        c.setEmployerAddress(employerAddress);
        // Default password if not provided
        if (password == null || password.trim().isEmpty()) {
            password = "cust123";
        }
        c.setPassword(password);

        // 4) Save
        boolean ok = dao.register(c);
        if (!ok) {
            request.setAttribute("error",
                "Failed to register customer. SSN may already exist.");
            request.getRequestDispatcher("/customer_register.jsp").forward(request, response);
            return;
        }

        // 5) Show acknowledgment popup
        request.setAttribute("success", "Customer Registration Successful");
        request.setAttribute("registeredCustomer", c);
        request.getRequestDispatcher("/customer_register.jsp").forward(request, response);
    }

    /**
     * Helper: returns true if an employee is in the session.
     */
    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInEmployee") != null;
    }
}
