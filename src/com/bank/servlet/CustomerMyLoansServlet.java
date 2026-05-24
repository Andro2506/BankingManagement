package com.bank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.LoanDAO;
import com.bank.model.Customer;
import com.bank.model.Loan;

/**
 * Shows the logged-in customer their own loan applications and their status.
 */
public class CustomerMyLoansServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LoanDAO dao = new LoanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInCustomer") == null) {
            response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
            return;
        }

        Customer cust = (Customer) session.getAttribute("loggedInCustomer");
        List<Loan> list = dao.findByCustomer(cust.getCustomerSsnId());

        request.setAttribute("loans", list);
        request.getRequestDispatcher("/customer_my_loans.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
