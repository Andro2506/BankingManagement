package com.bank.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.util.DBConnection;

/**
 * Customer Registration - Servlet (US004) - simple MVC CRUD.
 *
 * Manages a separate "simple_customer" table with these columns:
 *   account_number, holder_name, gender, dob, state, city, account_type, balance.
 *
 * Routing is done with the "action" request parameter:
 *   action=list   -> show all rows
 *   action=new    -> show empty form
 *   action=edit   -> show form pre-filled by account_number
 *   action=save   -> insert or update (POST)
 *   action=delete -> remove a row by account_number
 */
public class SimpleCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Make sure the simple_customer table exists. Called from each request.
     */
    private void ensureTable() {
        Connection con = null;
        Statement st = null;
        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            st.execute(
                "CREATE TABLE IF NOT EXISTS simple_customer (" +
                "  account_number TEXT PRIMARY KEY," +
                "  holder_name TEXT," +
                "  gender TEXT," +
                "  dob TEXT," +
                "  state TEXT," +
                "  city TEXT," +
                "  account_type TEXT," +
                "  balance REAL" +
                ")");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureTable();
        // For minimal session demo, ensure session exists
        HttpSession session = request.getSession(true);
        session.setAttribute("simpleVisitedAt", System.currentTimeMillis());

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        if ("new".equals(action)) {
            request.getRequestDispatcher("/simple_customer_form.jsp").forward(request, response);
            return;
        }
        if ("edit".equals(action)) {
            String acc = request.getParameter("accountNumber");
            Map<String, Object> row = findOne(acc);
            if (row == null) {
                request.setAttribute("error", "Account not found: " + acc);
            }
            request.setAttribute("row", row);
            request.getRequestDispatcher("/simple_customer_form.jsp").forward(request, response);
            return;
        }
        if ("delete".equals(action)) {
            String acc = request.getParameter("accountNumber");
            boolean ok = deleteOne(acc);
            if (ok) {
                request.setAttribute("flash", "Deleted account " + acc);
            } else {
                request.setAttribute("error", "Failed to delete " + acc);
            }
            // fall through to list
        }

        // default: list
        List<Map<String, Object>> rows = findAll();
        request.setAttribute("rows", rows);
        request.getRequestDispatcher("/simple_customer_list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureTable();
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            String acc = request.getParameter("accountNumber");
            String name = request.getParameter("holderName");
            String gender = request.getParameter("gender");
            String dob = request.getParameter("dob");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String type = request.getParameter("accountType");
            double balance = 0.0;
            try {
                balance = Double.parseDouble(request.getParameter("balance"));
            } catch (Exception e) {
                request.setAttribute("error", "Balance must be a number.");
                request.getRequestDispatcher("/simple_customer_form.jsp").forward(request, response);
                return;
            }

            // upsert: if existing -> update, else -> insert
            Map<String, Object> existing = findOne(acc);
            boolean ok;
            if (existing == null) {
                ok = insertOne(acc, name, gender, dob, state, city, type, balance);
            } else {
                ok = updateOne(acc, name, gender, dob, state, city, type, balance);
            }
            if (ok) {
                request.setAttribute("flash", "Saved account " + acc);
            } else {
                request.setAttribute("error", "Failed to save account " + acc);
            }
            // Show the list afterwards
            request.setAttribute("rows", findAll());
            request.getRequestDispatcher("/simple_customer_list.jsp").forward(request, response);
            return;
        }

        // Anything else, redirect to list
        doGet(request, response);
    }

    /* -------------------- DB helpers -------------------- */

    private boolean insertOne(String acc, String name, String gender, String dob,
                              String state, String city, String type, double balance) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "INSERT INTO simple_customer (account_number, holder_name, gender, dob, state, " +
                "city, account_type, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, acc);
            ps.setString(2, name);
            ps.setString(3, gender);
            ps.setString(4, dob);
            ps.setString(5, state);
            ps.setString(6, city);
            ps.setString(7, type);
            ps.setDouble(8, balance);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
    }

    private boolean updateOne(String acc, String name, String gender, String dob,
                              String state, String city, String type, double balance) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "UPDATE simple_customer SET holder_name=?, gender=?, dob=?, state=?, city=?, " +
                "account_type=?, balance=? WHERE account_number=?");
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setString(3, dob);
            ps.setString(4, state);
            ps.setString(5, city);
            ps.setString(6, type);
            ps.setDouble(7, balance);
            ps.setString(8, acc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
    }

    private boolean deleteOne(String acc) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM simple_customer WHERE account_number = ?");
            ps.setString(1, acc);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
    }

    private Map<String, Object> findOne(String acc) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM simple_customer WHERE account_number = ?");
            ps.setString(1, acc);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rowToMap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return null;
    }

    private List<Map<String, Object>> findAll() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM simple_customer ORDER BY account_number");
            while (rs.next()) {
                list.add(rowToMap(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    private Map<String, Object> rowToMap(ResultSet rs) throws SQLException {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("accountNumber", rs.getString("account_number"));
        m.put("holderName", rs.getString("holder_name"));
        m.put("gender", rs.getString("gender"));
        m.put("dob", rs.getString("dob"));
        m.put("state", rs.getString("state"));
        m.put("city", rs.getString("city"));
        m.put("accountType", rs.getString("account_type"));
        m.put("balance", rs.getDouble("balance"));
        return m;
    }
}
