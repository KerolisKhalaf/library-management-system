package com.library.model;

/**
 * RegularUser class demonstrating Inheritance.
 * 
 * Purpose: Represents a regular (non-admin) user in the library system.
 * 
 * OOP Concepts Used:
 * - Inheritance: Extends abstract User class
 * - Polymorphism: Implements abstract getRole() and isAdmin() methods
 * 
 * Design Pattern: None (used by UserFactory)
 */
public class RegularUser extends User {
    
    /**
     * Constructor for RegularUser
     * @param userId Unique identifier
     * @param username Username for login
     * @param password Password for login
     * @param email User email
     */
    public RegularUser(String userId, String username, String password, String email) {
        super(userId, username, password, email);
    }
    
    /**
     * Polymorphism: Overrides abstract method from User class
     * @return Role of the user
     */
    @Override
    public String getRole() {
        return "Regular User";
    }
    
    /**
     * Polymorphism: Overrides abstract method from User class
     * @return false since this is not an admin user
     */
    @Override
    public boolean isAdmin() {
        return false;
    }
}

