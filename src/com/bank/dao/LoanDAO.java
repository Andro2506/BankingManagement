package com.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bank.model.Loan;
import com.bank.util.DBConnection;

/**
 * Data Access Object for the loan table.
 */
public class LoanDAO {

    /**
     * Create a new loan request. Returns auto-generated loan_id or -1 on failure.
     */
    public int insert(Loan loan) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet keys = null;
        int newId = -1;

        // Default status is "Pending" if caller did not set it
        if (loan.getStatus() == null || loan.getStatus().trim().isEmpty()) {
            loan.setStatus("Pending");
        }

        try {
            con = DBConnection.getConnection();
            String sql =
                "INSERT INTO loan (customer_ssn_id, customer_name, loan_amount, length_of_loan, " +
                "loan_type, status) VALUES (?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loan.getCustomerSsnId());
            ps.setString(2, loan.getCustomerName());
            ps.setDouble(3, loan.getLoanAmount());
            ps.setInt(4, loan.getLengthOfLoan());
            ps.setString(5, loan.getLoanType());
            ps.setString(6, loan.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    newId = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("LoanDAO.insert error:");
            e.printStackTrace();
        } finally {
            try { if (keys != null) keys.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return newId;
    }

    /**
     * Update an existing loan record. SSN is NOT changed.
     */
    public boolean update(Loan loan) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;

        try {
            con = DBConnection.getConnection();
            String sql =
                "UPDATE loan SET customer_name=?, loan_amount=?, length_of_loan=?, loan_type=?, " +
                "status=? WHERE loan_id=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, loan.getCustomerName());
            ps.setDouble(2, loan.getLoanAmount());
            ps.setInt(3, loan.getLengthOfLoan());
            ps.setString(4, loan.getLoanType());
            ps.setString(5, loan.getStatus());
            ps.setInt(6, loan.getLoanId());
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("LoanDAO.update error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Delete a loan by its loan_id.
     */
    public boolean deleteById(int loanId) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM loan WHERE loan_id = ?");
            ps.setInt(1, loanId);
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("LoanDAO.deleteById error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Find a single loan by id, or null if missing.
     */
    public Loan findById(int loanId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Loan loan = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM loan WHERE loan_id = ?");
            ps.setInt(1, loanId);
            rs = ps.executeQuery();
            if (rs.next()) {
                loan = mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("LoanDAO.findById error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return loan;
    }

    /**
     * Get all loans, ordered by loan_id descending (newest first).
     */
    public List<Loan> findAll() {
        List<Loan> list = new ArrayList<Loan>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM loan ORDER BY loan_id DESC");
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("LoanDAO.findAll error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    /**
     * Get all loans belonging to a specific customer (by SSN).
     * Newest first.
     */
    public List<Loan> findByCustomer(String ssn) {
        List<Loan> list = new ArrayList<Loan>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM loan WHERE customer_ssn_id = ? ORDER BY loan_id DESC");
            ps.setString(1, ssn);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("LoanDAO.findByCustomer error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    /**
     * Helper: map one ResultSet row to a Loan object.
     */
    private Loan mapRow(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setLoanId(rs.getInt("loan_id"));
        l.setCustomerSsnId(rs.getString("customer_ssn_id"));
        l.setCustomerName(rs.getString("customer_name"));
        l.setLoanAmount(rs.getDouble("loan_amount"));
        l.setLengthOfLoan(rs.getInt("length_of_loan"));
        l.setLoanType(rs.getString("loan_type"));
        l.setStatus(rs.getString("status"));
        return l;
    }
}
