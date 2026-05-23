package com.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bank.model.Transaction;
import com.bank.util.DBConnection;

/**
 * Data Access Object for the customer_transactions table.
 */
public class TransactionDAO {

    /**
     * Insert a new transaction. Returns the auto-generated transaction_id, or -1 on failure.
     */
    public int insert(Transaction t) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet keys = null;
        int newId = -1;

        try {
            con = DBConnection.getConnection();
            String sql =
                "INSERT INTO customer_transactions (customer_ssn_id, customer_name, account_number, " +
                "ifsc_code, account_balance, aadhar_card_no, pan_card_no, date, contact_number, " +
                "mode_of_transaction, amount, credit_debit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getCustomerSsnId());
            ps.setString(2, t.getCustomerName());
            ps.setString(3, t.getAccountNumber());
            ps.setString(4, t.getIfscCode());
            ps.setDouble(5, t.getAccountBalance());
            ps.setString(6, t.getAadharCardNo());
            ps.setString(7, t.getPanCardNo());
            ps.setString(8, t.getDate());
            ps.setString(9, t.getContactNumber());
            ps.setString(10, t.getModeOfTransaction());
            ps.setDouble(11, t.getAmount());
            ps.setString(12, t.getCreditDebit());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    newId = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("TransactionDAO.insert error:");
            e.printStackTrace();
        } finally {
            try { if (keys != null) keys.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return newId;
    }

    /**
     * Get all transactions, newest first (by transaction_id descending).
     */
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<Transaction>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(
                "SELECT * FROM customer_transactions ORDER BY transaction_id DESC");
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("TransactionDAO.findAll error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    /**
     * Get all transactions for a specific customer.
     */
    public List<Transaction> findByCustomer(String ssn) {
        List<Transaction> list = new ArrayList<Transaction>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM customer_transactions WHERE customer_ssn_id = ? " +
                "ORDER BY transaction_id DESC");
            ps.setString(1, ssn);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("TransactionDAO.findByCustomer error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    /**
     * Helper: map one row to a Transaction object.
     */
    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("transaction_id"));
        t.setCustomerSsnId(rs.getString("customer_ssn_id"));
        t.setCustomerName(rs.getString("customer_name"));
        t.setAccountNumber(rs.getString("account_number"));
        t.setIfscCode(rs.getString("ifsc_code"));
        t.setAccountBalance(rs.getDouble("account_balance"));
        t.setAadharCardNo(rs.getString("aadhar_card_no"));
        t.setPanCardNo(rs.getString("pan_card_no"));
        t.setDate(rs.getString("date"));
        t.setContactNumber(rs.getString("contact_number"));
        t.setModeOfTransaction(rs.getString("mode_of_transaction"));
        t.setAmount(rs.getDouble("amount"));
        t.setCreditDebit(rs.getString("credit_debit"));
        return t;
    }
}
