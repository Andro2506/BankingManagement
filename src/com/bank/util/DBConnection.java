package com.bank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to get a JDBC Connection to the SQLite database.
 * The database file is created at "./banking.db" (relative to where Tomcat runs).
 *
 * Usage:
 *   Connection con = DBConnection.getConnection();
 *   ...
 *   con.close();
 */
public class DBConnection {

    // JDBC URL for SQLite. The file "banking.db" sits in the current working directory.
    private static final String DB_URL = "jdbc:sqlite:banking.db";

    // Static block: load the SQLite JDBC driver one time when this class is first used.
    static {
        try {
            // Class.forName loads the driver class so DriverManager knows about it.
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            // If the JAR is missing this will throw. Print a clear message to the server log.
            System.err.println("SQLite JDBC driver not found. Make sure sqlite-jdbc-3.7.2.jar is in WEB-INF/lib/");
            e.printStackTrace();
        }
    }

    /**
     * Open and return a new SQLite database connection.
     * The caller is responsible for closing it.
     */
    public static Connection getConnection() throws SQLException {
        // DriverManager creates a Connection using the URL above.
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Helper to close a Connection quietly. Used in finally blocks.
     */
    public static void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // We just log; nothing more we can do here.
                e.printStackTrace();
            }
        }
    }
}
