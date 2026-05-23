package com.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bank.model.Customer;
import com.bank.util.DBConnection;

/**
 * Data Access Object for the customer table. Provides CRUD methods.
 */
public class CustomerDAO {

    /**
     * Insert a new customer. Returns true on success.
     */
    public boolean register(Customer c) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;

        try {
            con = DBConnection.getConnection();
            String sql =
                "INSERT INTO customer (customer_ssn_id, first_name, last_name, email, " +
                "date_of_birth, address, contact_number, aadhar_number, pan_number, " +
                "account_number, ifsc_code, account_balance, account_type, gender, " +
                "marital_status, occupation, employer_name, employer_address, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql);
            ps.setString(1, c.getCustomerSsnId());
            ps.setString(2, c.getFirstName());
            ps.setString(3, c.getLastName());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getDateOfBirth());
            ps.setString(6, c.getAddress());
            ps.setString(7, c.getContactNumber());
            ps.setString(8, c.getAadharNumber());
            ps.setString(9, c.getPanNumber());
            ps.setString(10, c.getAccountNumber());
            ps.setString(11, c.getIfscCode());
            ps.setDouble(12, c.getAccountBalance());
            ps.setString(13, c.getAccountType());
            ps.setString(14, c.getGender());
            ps.setString(15, c.getMaritalStatus());
            ps.setString(16, c.getOccupation());
            ps.setString(17, c.getEmployerName());
            ps.setString(18, c.getEmployerAddress());
            ps.setString(19, c.getPassword());

            int affected = ps.executeUpdate();
            ok = affected > 0;
        } catch (SQLException e) {
            System.err.println("CustomerDAO.register error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Update an existing customer. SSN is the key and is NOT changed here.
     */
    public boolean update(Customer c) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;

        try {
            con = DBConnection.getConnection();
            String sql =
                "UPDATE customer SET first_name=?, last_name=?, email=?, date_of_birth=?, " +
                "address=?, contact_number=?, aadhar_number=?, pan_number=?, account_number=?, " +
                "ifsc_code=?, account_balance=?, account_type=?, gender=?, marital_status=?, " +
                "occupation=?, employer_name=?, employer_address=? WHERE customer_ssn_id=?";

            ps = con.prepareStatement(sql);
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getDateOfBirth());
            ps.setString(5, c.getAddress());
            ps.setString(6, c.getContactNumber());
            ps.setString(7, c.getAadharNumber());
            ps.setString(8, c.getPanNumber());
            ps.setString(9, c.getAccountNumber());
            ps.setString(10, c.getIfscCode());
            ps.setDouble(11, c.getAccountBalance());
            ps.setString(12, c.getAccountType());
            ps.setString(13, c.getGender());
            ps.setString(14, c.getMaritalStatus());
            ps.setString(15, c.getOccupation());
            ps.setString(16, c.getEmployerName());
            ps.setString(17, c.getEmployerAddress());
            ps.setString(18, c.getCustomerSsnId());

            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CustomerDAO.update error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Delete a customer by SSN.
     */
    public boolean deleteBySsn(String ssn) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("DELETE FROM customer WHERE customer_ssn_id = ?");
            ps.setString(1, ssn);
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CustomerDAO.deleteBySsn error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Find a customer by SSN. Returns null if not found.
     */
    public Customer findBySsn(String ssn) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer c = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM customer WHERE customer_ssn_id = ?");
            ps.setString(1, ssn);
            rs = ps.executeQuery();
            if (rs.next()) {
                c = mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("CustomerDAO.findBySsn error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return c;
    }

    /**
     * Find a customer by account number.
     */
    public Customer findByAccountNumber(String accountNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer c = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM customer WHERE account_number = ?");
            ps.setString(1, accountNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                c = mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("CustomerDAO.findByAccountNumber error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return c;
    }

    /**
     * Validate customer login (SSN + password).
     */
    public Customer login(String ssn, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer c = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM customer WHERE customer_ssn_id = ? AND password = ?");
            ps.setString(1, ssn);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                c = mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("CustomerDAO.login error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return c;
    }

    /**
     * Returns all customers.
     */
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<Customer>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM customer ORDER BY customer_ssn_id");
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("CustomerDAO.findAll error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    /**
     * Update only the account balance for a given SSN.
     * Used after a transaction is recorded.
     */
    public boolean updateBalance(String ssn, double newBalance) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "UPDATE customer SET account_balance = ? WHERE customer_ssn_id = ?");
            ps.setDouble(1, newBalance);
            ps.setString(2, ssn);
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CustomerDAO.updateBalance error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Helper: map a ResultSet row to a Customer object.
     */
    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerSsnId(rs.getString("customer_ssn_id"));
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        c.setEmail(rs.getString("email"));
        c.setDateOfBirth(rs.getString("date_of_birth"));
        c.setAddress(rs.getString("address"));
        c.setContactNumber(rs.getString("contact_number"));
        c.setAadharNumber(rs.getString("aadhar_number"));
        c.setPanNumber(rs.getString("pan_number"));
        c.setAccountNumber(rs.getString("account_number"));
        c.setIfscCode(rs.getString("ifsc_code"));
        c.setAccountBalance(rs.getDouble("account_balance"));
        c.setAccountType(rs.getString("account_type"));
        c.setGender(rs.getString("gender"));
        c.setMaritalStatus(rs.getString("marital_status"));
        c.setOccupation(rs.getString("occupation"));
        c.setEmployerName(rs.getString("employer_name"));
        c.setEmployerAddress(rs.getString("employer_address"));
        c.setPassword(rs.getString("password"));
        return c;
    }
}
