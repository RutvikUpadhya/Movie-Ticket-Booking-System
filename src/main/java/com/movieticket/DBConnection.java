package com.movieticket;

// ─────────────────────────────────────────────────────────────
//  DBConnection.java
//  Handles connecting to the MySQL database using JDBC.
//  Change DB_URL, USER, and PASSWORD to match your MySQL setup.
// ─────────────────────────────────────────────────────────────

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ── Change these three values to match your MySQL installation ──
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/movie_booking";
    private static final String USER    = "root";      // your MySQL username
    private static final String PASSWORD = "YOUR-PASSWORD";     // your MySQL password

    /**
     * Returns a live Connection to the movie_booking database.
     * Call this method whenever you need to talk to the database.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
