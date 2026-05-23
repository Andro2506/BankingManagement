package com.bank.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.dao.LoanDAO;

/**
 * Deletes a loan request by id.
 */
public class LoanDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LoanDAO dao = new LoanDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInEmployee") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        boolean ok = false;
        try {
            int id = Integer.parseInt(idStr);
            ok = dao.deleteById(id);
        } catch (Exception e) {
            // ignore
        }

        if (ok) {
            session.setAttribute("flashMessage", "Loan deleted successfully.");
        } else {
            session.setAttribute("flashError", "Failed to delete loan.");
        }
        response.sendRedirect(request.getContextPath() + "/loans");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doGet(request, response);
    }
}
