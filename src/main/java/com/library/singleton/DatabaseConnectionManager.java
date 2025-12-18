package com.library.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnectionManager class implementing Singleton Pattern.
 * 
 * Purpose: Manages database connection for the library system.
 * Ensures only one database connection instance exists throughout the application.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private constructor and instance variable
 * 
 * Design Pattern: Singleton Pattern
 * Why Singleton: Database connections are expensive resources. 
 * Having a single connection instance ensures:
 * 1. Resource efficiency (one connection instead of multiple)
 * 2. Consistency (all parts of application use same connection)
 * 3. Centralized connection management
 */
public class DatabaseConnectionManager {
    // Singleton: Private static instance
    private static DatabaseConnectionManager instance;
    
    // Encapsulation: Private connection
    private Connection connection;
    private String url = "jdbc:sqlite:library.db";
    
    // Singleton: Private constructor to prevent instantiation
    private DatabaseConnectionManager() {
        try {
            // Initialize SQLite database connection
            connection = DriverManager.getConnection(url);
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("Error creating database connection: " + e.getMessage());
        }
    }
    
    /**
     * Singleton: Global access point to get the instance
     * @return The single instance of DatabaseConnectionManager
     */
    public static synchronized DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }
    
    /**
     * Get the database connection
     * @return Connection object
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * Initialize database tables
     */
    private void initializeDatabase() {
        try {
            // Create books table
            connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS books (" +
                "isbn TEXT PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "author TEXT NOT NULL, " +
                "year INTEGER, " +
                "category TEXT NOT NULL, " +
                "isAvailable INTEGER DEFAULT 1)"
            );
            
            // Create users table
            connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "userId TEXT PRIMARY KEY, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "role TEXT NOT NULL)"
            );
            
            // Create borrow_records table
            connection.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS borrow_records (" +
                "recordId TEXT PRIMARY KEY, " +
                "userId TEXT NOT NULL, " +
                "bookIsbn TEXT NOT NULL, " +
                "borrowDate TEXT NOT NULL, " +
                "returnDate TEXT, " +
                "isReturned INTEGER DEFAULT 0, " +
                "FOREIGN KEY (userId) REFERENCES users(userId), " +
                "FOREIGN KEY (bookIsbn) REFERENCES books(isbn))"
            );
            
            // Create default admin user if not exists
            connection.createStatement().execute(
                "INSERT OR IGNORE INTO users (userId, username, password, email, role) " +
                "VALUES ('admin001', 'admin', 'admin123', 'admin@library.com', 'Admin')"
            );
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
    
    /**
     * Close the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}

