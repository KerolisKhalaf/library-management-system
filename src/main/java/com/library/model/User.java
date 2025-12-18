package com.library.model;

/**
 * Abstract User class demonstrating Abstraction and Inheritance.
 * 
 * Purpose: Base class for all user types in the library system.
 * 
 * OOP Concepts Used:
 * - Abstraction: Defines common interface for all users without implementation details
 * - Encapsulation: Private fields with public getters/setters
 * - Inheritance: Base class for specialized user types
 * 
 * Design Pattern: None (base class for Factory pattern)
 */
public abstract class User {
    // Encapsulation: Private fields
    private String userId;
    private String username;
    private String password;
    private String email;
    
    /**
     * Constructor for User
     * @param userId Unique identifier
     * @param username Username for login
     * @param password Password for login
     * @param email User email
     */
    public User(String userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    
    // Encapsulation: Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Abstract method demonstrating Polymorphism.
     * Each user type will provide its own implementation.
     * @return User role/type
     */
    public abstract String getRole();
    
    /**
     * Abstract method to check if user has admin privileges.
     * Demonstrates Polymorphism.
     * @return true if user is admin, false otherwise
     */
    public abstract boolean isAdmin();
    
    @Override
    public String toString() {
        return String.format("UserID: %s, Username: %s, Email: %s, Role: %s",
                userId, username, email, getRole());
    }
}

