package com.parking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralised JDBC connection factory.
 * Update URL / USER / PASSWORD to match your local MySQL setup.
 */
public class DBConnection {

    private static final String URL  =
            "jdbc:mysql://localhost:3306/smart_parking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "Purush@2628";   // <-- change to your MySQL password

    static {
        try {
            // Load MySQL JDBC driver (mysql-connector-j-x.x.x.jar must be in WEB-INF/lib)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. "
                    + "Add mysql-connector-j-*.jar to WEB-INF/lib", e);
        }
    }

    /** Returns a brand-new connection. Caller is responsible for closing it. */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
