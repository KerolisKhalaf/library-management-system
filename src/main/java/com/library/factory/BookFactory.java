package com.library.factory;

import com.library.model.Book;
import com.library.model.SoftwareEngineeringBook;
import com.library.model.ManagementBook;
import com.library.model.AIBook;

/**
 * BookFactory class implementing Factory Pattern.
 * 
 * Purpose: Creates different types of books without exposing the creation logic.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Hides object creation logic
 * - Polymorphism: Returns abstract Book type
 * 
 * Design Pattern: Factory Pattern
 * Why Factory: 
 * 1. Hides the complexity of object creation from the client
 * 2. Provides a single point for creating book objects
 * 3. Makes it easy to add new book types without modifying client code
 * 4. Returns abstract type (Book) allowing polymorphic usage
 */
public class BookFactory {
    
    // Book categories
    public static final String SOFTWARE_ENGINEERING = "SoftwareEngineering";
    public static final String MANAGEMENT = "Management";
    public static final String AI = "AI";
    
    /**
     * Factory method: Creates a book based on category
     * @param category Type of book to create
     * @param isbn Unique identifier
     * @param title Book title
     * @param author Book author
     * @param year Publication year
     * @return Book object (polymorphic return type)
     */
    public static Book createBook(String category, String isbn, String title, String author, int year) {
        // Factory Pattern: Hide creation logic, return abstract type
        switch (category.toLowerCase()) {
            case "softwareengineering":
            case "software_engineering":
            case "se":
                return new SoftwareEngineeringBook(isbn, title, author, year);
                
            case "management":
            case "mgmt":
                return new ManagementBook(isbn, title, author, year);
                
            case "ai":
            case "artificial_intelligence":
            case "artificialintelligence":
                return new AIBook(isbn, title, author, year);
                
            default:
                throw new IllegalArgumentException("Unknown book category: " + category);
        }
    }
    
    /**
     * Factory method: Creates a SoftwareEngineeringBook
     * @param isbn Unique identifier
     * @param title Book title
     * @param author Book author
     * @param year Publication year
     * @return SoftwareEngineeringBook object
     */
    public static Book createSoftwareEngineeringBook(String isbn, String title, String author, int year) {
        return new SoftwareEngineeringBook(isbn, title, author, year);
    }
    
    /**
     * Factory method: Creates a ManagementBook
     * @param isbn Unique identifier
     * @param title Book title
     * @param author Book author
     * @param year Publication year
     * @return ManagementBook object
     */
    public static Book createManagementBook(String isbn, String title, String author, int year) {
        return new ManagementBook(isbn, title, author, year);
    }
    
    /**
     * Factory method: Creates an AIBook
     * @param isbn Unique identifier
     * @param title Book title
     * @param author Book author
     * @param year Publication year
     * @return AIBook object
     */
    public static Book createAIBook(String isbn, String title, String author, int year) {
        return new AIBook(isbn, title, author, year);
    }
}

