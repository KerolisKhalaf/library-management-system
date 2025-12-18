package com.library.util;

import com.library.model.Book;
import com.library.model.User;
import com.library.model.BorrowRecord;
import com.library.factory.BookFactory;
import com.library.factory.UserFactory;
import com.library.singleton.DatabaseConnectionManager;
import com.library.singleton.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * LibraryService class for database operations.
 * 
 * Purpose: Handles all database operations for books, users, and borrow records.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private methods and organized data access
 * 
 * Design Pattern: None (uses Singleton and Factory patterns)
 */
public class LibraryService {
    private DatabaseConnectionManager dbManager;
    private Logger logger;
    
    public LibraryService() {
        this.dbManager = DatabaseConnectionManager.getInstance();
        this.logger = Logger.getInstance();
    }
    
    // ========== Book Operations ==========
    
    /**
     * Add a book to the database
     * @param book Book object to add
     * @return true if successful, false otherwise
     */
    public boolean addBook(Book book) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO books (isbn, title, author, year, category, isAvailable) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setInt(4, book.getYear());
            stmt.setString(5, book.getCategory());
            stmt.setInt(6, book.isAvailable() ? 1 : 0);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                logger.logInfo("Book added: " + book.getIsbn() + " - " + book.getTitle());
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error adding book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all books from database
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            
            while (rs.next()) {
                Book book = BookFactory.createBook(
                    rs.getString("category"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("year")
                );
                book.setAvailable(rs.getInt("isAvailable") == 1);
                books.add(book);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.logError("Error getting books: " + e.getMessage());
        }
        return books;
    }
    
    /**
     * Get a book by ISBN
     * @param isbn Book ISBN
     * @return Book object or null if not found
     */
    public Book getBookByIsbn(String isbn) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM books WHERE isbn = ?");
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Book book = BookFactory.createBook(
                    rs.getString("category"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("year")
                );
                book.setAvailable(rs.getInt("isAvailable") == 1);
                rs.close();
                stmt.close();
                return book;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.logError("Error getting book: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update a book in the database
     * @param book Book object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateBook(Book book) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE books SET title = ?, author = ?, year = ?, category = ?, isAvailable = ? WHERE isbn = ?"
            );
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setInt(3, book.getYear());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.isAvailable() ? 1 : 0);
            stmt.setString(6, book.getIsbn());
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                logger.logInfo("Book updated: " + book.getIsbn());
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error updating book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a book from the database
     * @param isbn Book ISBN to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBook(String isbn) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM books WHERE isbn = ?");
            stmt.setString(1, isbn);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                logger.logInfo("Book deleted: " + isbn);
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error deleting book: " + e.getMessage());
        }
        return false;
    }
    
    // ========== User Operations ==========
    
    /**
     * Add a user to the database
     * @param user User object to add
     * @return true if successful, false otherwise
     */
    public boolean addUser(User user) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (userId, username, password, email, role) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                logger.logInfo("User added: " + user.getUserId() + " - " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error adding user: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all users from database
     * @return List of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            
            while (rs.next()) {
                User user = UserFactory.createUser(
                    rs.getString("role"),
                    rs.getString("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                );
                users.add(user);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.logError("Error getting users: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Get a user by username
     * @param username Username to search for
     * @return User object or null if not found
     */
    public User getUserByUsername(String username) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = UserFactory.createUser(
                    rs.getString("role"),
                    rs.getString("userId"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                );
                rs.close();
                stmt.close();
                return user;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.logError("Error getting user: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update a user in the database
     * @param user User object with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateUser(User user) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE users SET username = ?, password = ?, email = ?, role = ? WHERE userId = ?"
            );
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getUserId());
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                logger.logInfo("User updated: " + user.getUserId());
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error updating user: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a user from the database
     * @param userId User ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteUser(String userId) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE userId = ?");
            stmt.setString(1, userId);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                logger.logInfo("User deleted: " + userId);
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error deleting user: " + e.getMessage());
        }
        return false;
    }
    
    // ========== Borrow/Return Operations ==========
    
    /**
     * Borrow a book
     * @param userId User ID borrowing the book
     * @param bookIsbn ISBN of the book to borrow
     * @return true if successful, false otherwise
     */
    public boolean borrowBook(String userId, String bookIsbn) {
        try {
            Book book = getBookByIsbn(bookIsbn);
            if (book == null || !book.isAvailable()) {
                logger.logWarning("Book not available for borrowing: " + bookIsbn);
                return false;
            }
            
            Connection conn = dbManager.getConnection();
            
            // Create borrow record
            String recordId = "BR" + System.currentTimeMillis();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO borrow_records (recordId, userId, bookIsbn, borrowDate, isReturned) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, recordId);
            stmt.setString(2, userId);
            stmt.setString(3, bookIsbn);
            stmt.setString(4, LocalDate.now().toString());
            stmt.setInt(5, 0);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                // Update book availability
                book.setAvailable(false);
                updateBook(book);
                
                logger.logInfo("Book borrowed: " + bookIsbn + " by user: " + userId);
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error borrowing book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Return a book
     * @param userId User ID returning the book
     * @param bookIsbn ISBN of the book to return
     * @return true if successful, false otherwise
     */
    public boolean returnBook(String userId, String bookIsbn) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "UPDATE borrow_records SET returnDate = ?, isReturned = ? WHERE userId = ? AND bookIsbn = ? AND isReturned = 0"
            );
            stmt.setString(1, LocalDate.now().toString());
            stmt.setInt(2, 1);
            stmt.setString(3, userId);
            stmt.setString(4, bookIsbn);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                // Update book availability
                Book book = getBookByIsbn(bookIsbn);
                if (book != null) {
                    book.setAvailable(true);
                    updateBook(book);
                }
                
                logger.logInfo("Book returned: " + bookIsbn + " by user: " + userId);
                return true;
            }
        } catch (SQLException e) {
            logger.logError("Error returning book: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get all borrow records
     * @return List of all borrow records
     */
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> records = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM borrow_records");
            
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord(
                    rs.getString("recordId"),
                    rs.getString("userId"),
                    rs.getString("bookIsbn"),
                    LocalDate.parse(rs.getString("borrowDate"))
                );
                if (rs.getString("returnDate") != null) {
                    record.setReturnDate(LocalDate.parse(rs.getString("returnDate")));
                }
                record.setReturned(rs.getInt("isReturned") == 1);
                records.add(record);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            logger.logError("Error getting borrow records: " + e.getMessage());
        }
        return records;
    }
}

