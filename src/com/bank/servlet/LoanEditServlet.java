package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.LoanDAO;
import com.bank.model.Loan;

/**
 * US007: Employee views/updates a loan request.
 *
 * GET ?id=<loanId>  -> load and show form (SSN read-only).
 * POST              -> save changes to status / amount / length / type.
 */
public class LoanEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LoanDAO dao = new LoanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        Loan loan = null;
        try {
            int id = Integer.parseInt(idStr);
            loan = dao.findById(id);
        } catch (Exception e) {
            // fall through, loan stays null
        }

        if (loan == null) {
            request.setAttribute("error", "Loan not found.");
            response.sendRedirect(request.getContextPath() + "/loans");
            return;
        }
        request.setAttribute("loan", loan);
        request.getRequestDispatcher("/loan_edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("loanId");
        Loan loan = null;
        try {
            int id = Integer.parseInt(idStr);
            loan = dao.findById(id);
        } catch (Exception e) {
            // ignore
        }
        if (loan == null) {
            request.setAttribute("error", "Loan not found.");
            response.sendRedirect(request.getContextPath() + "/loans");
            return;
        }

        // SSN is NOT editable: keep what's in DB.
        loan.setCustomerName(request.getParameter("customerName"));
        loan.setLoanType(request.getParameter("loanType"));
        loan.setStatus(request.getParameter("status"));

        try {
            loan.setLoanAmount(Double.parseDouble(request.getParameter("loanAmount")));
            loan.setLengthOfLoan(Integer.parseInt(request.getParameter("lengthOfLoan")));
        } catch (Exception e) {
            request.setAttribute("error", "Loan amount and length must be numeric.");
            request.setAttribute("loan", loan);
            request.getRequestDispatcher("/loan_edit.jsp").forward(request, response);
            return;
        }

        boolean ok = dao.update(loan);
        if (ok) {
            request.setAttribute("success", "Loan updated successfully.");
        } else {
            request.setAttribute("error", "Failed to update loan.");
        }
        request.setAttribute("loan", loan);
        request.getRequestDispatcher("/loan_edit.jsp").forward(request, response);
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loggedInEmployee") != null;
    }
}
