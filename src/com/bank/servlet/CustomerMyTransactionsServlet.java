package com.bank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.TransactionDAO;
import com.bank.model.Customer;
import com.bank.model.Transaction;

/**
 * Shows the logged-in customer their own transaction history.
 */
public class CustomerMyTransactionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TransactionDAO dao = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInCustomer") == null) {
            response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
            return;
        }

        // Get the SSN from the session and fetch only this customer's transactions
        Customer cust = (Customer) session.getAttribute("loggedInCustomer");
        List<Transaction> list = dao.findByCustomer(cust.getCustomerSsnId());

        request.setAttribute("transactions", list);
        request.getRequestDispatcher("/customer_my_transactions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
