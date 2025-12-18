package com.library.factory;

import com.library.model.User;
import com.library.model.Admin;
import com.library.model.RegularUser;

/**
 * UserFactory class implementing Factory Pattern.
 * 
 * Purpose: Creates different types of users without exposing the creation logic.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Hides object creation logic
 * - Polymorphism: Returns abstract User type
 * 
 * Design Pattern: Factory Pattern
 * Why Factory:
 * 1. Hides the complexity of object creation from the client
 * 2. Provides a single point for creating user objects
 * 3. Makes it easy to add new user types without modifying client code
 * 4. Returns abstract type (User) allowing polymorphic usage
 */
public class UserFactory {
    
    // User roles
    public static final String ADMIN = "Admin";
    public static final String REGULAR_USER = "RegularUser";
    
    /**
     * Factory method: Creates a user based on role
     * @param role Type of user to create (Admin or RegularUser)
     * @param userId Unique identifier
     * @param username Username for login
     * @param password Password for login
     * @param email User email
     * @return User object (polymorphic return type)
     */
    public static User createUser(String role, String userId, String username, String password, String email) {
        // Factory Pattern: Hide creation logic, return abstract type
        switch (role.toLowerCase()) {
            case "admin":
            case "administrator":
                return new Admin(userId, username, password, email);
                
            case "regularuser":
            case "regular_user":
            case "user":
                return new RegularUser(userId, username, password, email);
                
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }
    
    /**
     * Factory method: Creates an Admin user
     * @param userId Unique identifier
     * @param username Username for login
     * @param password Password for login
     * @param email User email
     * @return Admin object
     */
    public static User createAdmin(String userId, String username, String password, String email) {
        return new Admin(userId, username, password, email);
    }
    
    /**
     * Factory method: Creates a RegularUser
     * @param userId Unique identifier
     * @param username Username for login
     * @param password Password for login
     * @param email User email
     * @return RegularUser object
     */
    public static User createRegularUser(String userId, String username, String password, String email) {
        return new RegularUser(userId, username, password, email);
    }
}

