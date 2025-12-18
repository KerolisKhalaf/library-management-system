package com.library.model;

import java.time.LocalDate;

/**
 * BorrowRecord class demonstrating Encapsulation.
 * 
 * Purpose: Represents a borrowing transaction record in the library system.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private fields with public getters/setters
 * 
 * Design Pattern: None
 */
public class BorrowRecord {
    // Encapsulation: Private fields
    private String recordId;
    private String userId;
    private String bookIsbn;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean isReturned;
    
    /**
     * Constructor for BorrowRecord
     * @param recordId Unique record identifier
     * @param userId User who borrowed the book
     * @param bookIsbn ISBN of the borrowed book
     * @param borrowDate Date when book was borrowed
     */
    public BorrowRecord(String recordId, String userId, String bookIsbn, LocalDate borrowDate) {
        this.recordId = recordId;
        this.userId = userId;
        this.bookIsbn = bookIsbn;
        this.borrowDate = borrowDate;
        this.isReturned = false;
    }
    
    // Encapsulation: Getters and Setters
    public String getRecordId() {
        return recordId;
    }
    
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getBookIsbn() {
        return bookIsbn;
    }
    
    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public boolean isReturned() {
        return isReturned;
    }
    
    public void setReturned(boolean returned) {
        isReturned = returned;
    }
    
    @Override
    public String toString() {
        return String.format("RecordID: %s, UserID: %s, BookISBN: %s, BorrowDate: %s, ReturnDate: %s, Returned: %s",
                recordId, userId, bookIsbn, borrowDate, returnDate, isReturned);
    }
}

