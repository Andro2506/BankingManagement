package com.bank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;

/**
 * Shows every transaction in a list view.
 */
public class TransactionListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private TransactionDAO dao = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInEmployee") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Transaction> list = dao.findAll();
        request.setAttribute("transactions", list);
        request.getRequestDispatcher("/transaction_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
