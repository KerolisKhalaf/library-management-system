package com.library.model;

/**
 * Abstract Book class demonstrating Abstraction and Inheritance.
 * 
 * Purpose: Base class for all book types in the library system.
 * 
 * OOP Concepts Used:
 * - Abstraction: Defines common interface for all books without implementation details
 * - Encapsulation: Private fields with public getters/setters
 * - Inheritance: Base class for specialized book types
 * 
 * Design Pattern: None (base class for Factory pattern)
 */
public abstract class Book {
    // Encapsulation: Private fields
    private String isbn;
    private String title;
    private String author;
    private int year;
    private boolean isAvailable;
    
    /**
     * Constructor for Book
     * @param isbn Unique identifier
     * @param title Book title
     * @param author Book author
     * @param year Publication year
     */
    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = true;
    }
    
    // Encapsulation: Getters and Setters
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    /**
     * Abstract method demonstrating Polymorphism.
     * Each book type will provide its own implementation.
     * @return Book category/type
     */
    public abstract String getCategory();
    
    @Override
    public String toString() {
        return String.format("ISBN: %s, Title: %s, Author: %s, Year: %d, Available: %s, Category: %s",
                isbn, title, author, year, isAvailable, getCategory());
    }
}

