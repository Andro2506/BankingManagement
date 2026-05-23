package com.bank.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.LoanDAO;
import com.bank.model.Loan;

/**
 * Lists every loan request (US007 part).
 */
public class LoanListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LoanDAO dao = new LoanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInEmployee") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Loan> loans = dao.findAll();
        request.setAttribute("loans", loans);
        request.getRequestDispatcher("/loan_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
