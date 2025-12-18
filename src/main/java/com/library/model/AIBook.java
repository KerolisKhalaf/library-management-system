package com.library.model;

/**
 * AIBook class demonstrating Inheritance.
 * 
 * Purpose: Represents an AI (Artificial Intelligence) book in the library.
 * 
 * OOP Concepts Used:
 * - Inheritance: Extends abstract Book class
 * - Polymorphism: Implements abstract getCategory() method
 * 
 * Design Pattern: None (used by BookFactory)
 */
public class AIBook extends Book {
    
    /**
     * Constructor for AIBook
     * @param isbn Unique identifier
     * @param title Book title
     * @param author Book author
     * @param year Publication year
     */
    public AIBook(String isbn, String title, String author, int year) {
        super(isbn, title, author, year);
    }
    
    /**
     * Polymorphism: Overrides abstract method from Book class
     * @return Category of the book
     */
    @Override
    public String getCategory() {
        return "Artificial Intelligence";
    }
}

