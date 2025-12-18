package com.library.model;

/**
 * Admin class demonstrating Inheritance.
 * 
 * Purpose: Represents an administrator user in the library system.
 * 
 * OOP Concepts Used:
 * - Inheritance: Extends abstract User class
 * - Polymorphism: Implements abstract getRole() and isAdmin() methods
 * 
 * Design Pattern: None (used by UserFactory)
 */
public class Admin extends User {
    
    /**
     * Constructor for Admin
     * @param userId Unique identifier
     * @param username Username for login
     * @param password Password for login
     * @param email User email
     */
    public Admin(String userId, String username, String password, String email) {
        super(userId, username, password, email);
    }
    
    /**
     * Polymorphism: Overrides abstract method from User class
     * @return Role of the user
     */
    @Override
    public String getRole() {
        return "Admin";
    }
    
    /**
     * Polymorphism: Overrides abstract method from User class
     * @return true since this is an admin user
     */
    @Override
    public boolean isAdmin() {
        return true;
    }
}

