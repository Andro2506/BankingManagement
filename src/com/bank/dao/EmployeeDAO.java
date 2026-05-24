package com.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bank.model.Employee;
import com.bank.util.DBConnection;

/**
 * Data Access Object for the employee table.
 * All methods open a Connection, run their SQL, then close the Connection.
 */
public class EmployeeDAO {

    /**
     * Insert a new employee row.
     * Returns the auto-generated 7-digit employee_id, or -1 on failure.
     */
    public int register(Employee emp) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet keys = null;
        int newId = -1;

        try {
            con = DBConnection.getConnection();

            // Default values for designation and salary if user did not supply them
            if (emp.getDesignation() == null || emp.getDesignation().trim().isEmpty()) {
                emp.setDesignation("Clerk");
            }
            if (emp.getSalary() == 0) {
                emp.setSalary(25000);
            }

            String sql =
                "INSERT INTO employee (first_name, last_name, email, contact_number, address, " +
                "password, designation, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getEmail());
            ps.setString(4, emp.getContactNumber());
            ps.setString(5, emp.getAddress());
            ps.setString(6, emp.getPassword());
            ps.setString(7, emp.getDesignation());
            ps.setDouble(8, emp.getSalary());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    newId = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.register error:");
            e.printStackTrace();
        } finally {
            try { if (keys != null) keys.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return newId;
    }

    /**
     * Validate login: returns Employee object if id+password match, else null.
     */
    public Employee login(int employeeId, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Employee emp = null;

        try {
            con = DBConnection.getConnection();
            String sql = "SELECT * FROM employee WHERE employee_id = ? AND password = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, employeeId);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                emp = mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.login error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return emp;
    }

    /**
     * Find an employee by primary key.
     */
    public Employee findById(int employeeId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Employee emp = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM employee WHERE employee_id = ?");
            ps.setInt(1, employeeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                emp = mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.findById error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return emp;
    }

    /**
     * Returns the total number of rows in the employee table.
     * Used by the Manager dashboard to show "how many employees we have".
     */
    public int count() {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        int total = 0;
        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM employee");
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.count error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return total;
    }

    /**
     * Update only the designation column for one employee.
     * Returns true on success. Used by the Manager-only edit screen.
     */
    public boolean updateDesignation(int employeeId, String newDesignation) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean ok = false;
        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(
                "UPDATE employee SET designation = ? WHERE employee_id = ?");
            ps.setString(1, newDesignation);
            ps.setInt(2, employeeId);
            ok = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.updateDesignation error:");
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return ok;
    }

    /**
     * Get all employees.
     */
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<Employee>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM employee ORDER BY employee_id");
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("EmployeeDAO.findAll error:");
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignore) {}
            try { if (st != null) st.close(); } catch (SQLException ignore) {}
            DBConnection.close(con);
        }
        return list;
    }

    /**
     * Helper to read one ResultSet row into an Employee object.
     */
    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setEmployeeId(rs.getInt("employee_id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setEmail(rs.getString("email"));
        e.setContactNumber(rs.getString("contact_number"));
        e.setAddress(rs.getString("address"));
        e.setPassword(rs.getString("password"));
        e.setDesignation(rs.getString("designation"));
        e.setSalary(rs.getDouble("salary"));
        return e;
    }
}
