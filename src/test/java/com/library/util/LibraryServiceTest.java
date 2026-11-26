package com.library.util;

import com.library.factory.BookFactory;
import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.RegularUser;
import com.library.model.User;
import com.library.singleton.DatabaseConnectionManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryServiceTest {
    private static LibraryService libraryService;

    @BeforeAll
    static void initializeDatabase() throws IOException {
        String dataDirectory = System.getenv("LIBRARY_DATA_DIR");
        assertNotNull(dataDirectory, "Surefire should provide an isolated database directory");
        Files.createDirectories(Paths.get(dataDirectory));
        libraryService = new LibraryService();
    }

    @BeforeEach
    void clearDatabase() throws SQLException {
        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM borrow_records");
            statement.executeUpdate("DELETE FROM books");
            statement.executeUpdate("DELETE FROM users WHERE userId <> 'admin001'");
        }
    }

    @Test
    void addsReadsUpdatesAndDeletesBooks() {
        Book book = BookFactory.createBook(
            "Software Engineering", "SE-1", "Software Design", "Author", 2024
        );

        assertTrue(libraryService.addBook(book));

        Book savedBook = libraryService.getBookByIsbn("SE-1");
        assertNotNull(savedBook);
        assertEquals("Software Design", savedBook.getTitle());
        assertEquals("Software Engineering", savedBook.getCategory());
        assertTrue(libraryService.getAllBooks().stream()
            .anyMatch(existingBook -> existingBook.getIsbn().equals("SE-1")));

        savedBook.setTitle("Updated Software Design");
        assertTrue(libraryService.updateBook(savedBook));
        assertEquals("Updated Software Design", libraryService.getBookByIsbn("SE-1").getTitle());

        assertTrue(libraryService.deleteBook("SE-1"));
        assertNull(libraryService.getBookByIsbn("SE-1"));
        assertFalse(libraryService.deleteBook("SE-1"));
    }

    @Test
    void addsReadsUpdatesAndDeletesUsers() {
        User user = new RegularUser("U1", "reader", "secret", "reader@example.com");

        assertTrue(libraryService.addUser(user));
        User savedUser = libraryService.getUserByUsername("reader");
        assertNotNull(savedUser);
        assertEquals("Regular User", savedUser.getRole());
        assertEquals("U1", savedUser.getUserId());
        assertEquals(1, libraryService.getAllUsers().stream()
            .filter(existingUser -> existingUser.getUserId().equals("U1"))
            .count());

        savedUser.setUsername("updated-reader");
        assertTrue(libraryService.updateUser(savedUser));
        assertNull(libraryService.getUserByUsername("reader"));
        assertNotNull(libraryService.getUserByUsername("updated-reader"));

        assertTrue(libraryService.deleteUser("U1"));
        assertNull(libraryService.getUserByUsername("updated-reader"));
    }

    @Test
    void borrowingAndReturningUpdatesBookAvailabilityAndHistory() {
        libraryService.addUser(new RegularUser("U1", "reader", "secret", "reader@example.com"));
        libraryService.addBook(
            BookFactory.createBook("Artificial Intelligence", "AI-1", "Machine Learning", "Author", 2025)
        );

        assertTrue(libraryService.borrowBook("U1", "AI-1"));
        assertFalse(libraryService.getBookByIsbn("AI-1").isAvailable());

        List<BorrowRecord> borrowedRecords = libraryService.getAllBorrowRecords();
        assertEquals(1, borrowedRecords.size());
        assertEquals("U1", borrowedRecords.get(0).getUserId());
        assertEquals("AI-1", borrowedRecords.get(0).getBookIsbn());
        assertEquals(LocalDate.now(), borrowedRecords.get(0).getBorrowDate());
        assertFalse(borrowedRecords.get(0).isReturned());

        assertTrue(libraryService.returnBook("U1", "AI-1"));
        assertTrue(libraryService.getBookByIsbn("AI-1").isAvailable());

        BorrowRecord returnedRecord = libraryService.getAllBorrowRecords().get(0);
        assertTrue(returnedRecord.isReturned());
        assertEquals(LocalDate.now(), returnedRecord.getReturnDate());
    }

    @Test
    void doesNotBorrowMissingOrUnavailableBooks() {
        libraryService.addUser(new RegularUser("U1", "reader", "secret", "reader@example.com"));
        assertFalse(libraryService.borrowBook("U1", "missing"));

        Book unavailableBook = BookFactory.createBook(
            "Management", "MG-1", "Managing Teams", "Author", 2023
        );
        unavailableBook.setAvailable(false);
        libraryService.addBook(unavailableBook);

        assertFalse(libraryService.borrowBook("U1", "MG-1"));
        assertTrue(libraryService.getAllBorrowRecords().isEmpty());
    }
}
