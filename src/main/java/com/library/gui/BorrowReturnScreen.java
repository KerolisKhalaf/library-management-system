package com.library.gui;

import com.library.model.Book;
import com.library.model.BorrowRecord;
import com.library.model.User;
import com.library.util.LibraryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * BorrowReturnScreen class for borrowing and returning books.
 * 
 * Purpose: Provides interface for borrowing and returning books.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private fields and methods
 * 
 * Design Pattern: None
 */
public class BorrowReturnScreen {
    private Stage stage;
    private User currentUser;
    private LibraryService libraryService;
    private ObservableList<Book> availableBooks;
    private ObservableList<BorrowRecord> userBorrowRecords;
    private TableView<Book> bookTable;
    private TableView<BorrowRecord> recordTable;
    
    public BorrowReturnScreen(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.libraryService = new LibraryService();
        this.availableBooks = FXCollections.observableArrayList();
        this.userBorrowRecords = FXCollections.observableArrayList();
    }
    
    /**
     * Display the borrow/return screen
     */
    public void show() {
        // Available Books Table
        Label availableBooksLabel = new Label("Available Books");
        availableBooksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(120);
        
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        
        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);
        
        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);
        
        bookTable = new TableView<>();
        bookTable.setItems(availableBooks);
        bookTable.getColumns().add(isbnCol);
        bookTable.getColumns().add(titleCol);
        bookTable.getColumns().add(authorCol);
        bookTable.getColumns().add(categoryCol);
        bookTable.setPrefHeight(200);
        
        // Borrow button
        Button borrowButton = new Button("Borrow Selected Book");
        borrowButton.setOnAction(e -> {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a book to borrow");
                return;
            }
            
            if (!selected.isAvailable()) {
                showAlert("Error", "This book is not available");
                return;
            }
            
            if (libraryService.borrowBook(currentUser.getUserId(), selected.getIsbn())) {
                refreshTables();
                showAlert("Success", "Book borrowed successfully");
            } else {
                showAlert("Error", "Failed to borrow book. Book might not be available.");
            }
        });
        
        // My Borrowed Books Table
        Label myBooksLabel = new Label("My Borrowed Books");
        myBooksLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        TableColumn<BorrowRecord, String> recordIdCol = new TableColumn<>("Record ID");
        recordIdCol.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        recordIdCol.setPrefWidth(150);
        
        TableColumn<BorrowRecord, String> bookIsbnCol = new TableColumn<>("Book ISBN");
        bookIsbnCol.setCellValueFactory(new PropertyValueFactory<>("bookIsbn"));
        bookIsbnCol.setPrefWidth(120);
        
        TableColumn<BorrowRecord, LocalDate> borrowDateCol = new TableColumn<>("Borrow Date");
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        borrowDateCol.setPrefWidth(120);
        
        TableColumn<BorrowRecord, LocalDate> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        returnDateCol.setPrefWidth(120);
        
        TableColumn<BorrowRecord, Boolean> returnedCol = new TableColumn<>("Returned");
        returnedCol.setCellValueFactory(new PropertyValueFactory<>("returned"));
        returnedCol.setPrefWidth(100);
        
        recordTable = new TableView<>();
        recordTable.setItems(userBorrowRecords);
        recordTable.getColumns().add(recordIdCol);
        recordTable.getColumns().add(bookIsbnCol);
        recordTable.getColumns().add(borrowDateCol);
        recordTable.getColumns().add(returnDateCol);
        recordTable.getColumns().add(returnedCol);
        recordTable.setPrefHeight(200);
        
        // Return button
        Button returnButton = new Button("Return Selected Book");
        returnButton.setOnAction(e -> {
            BorrowRecord selected = recordTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a record to return");
                return;
            }
            
            if (selected.isReturned()) {
                showAlert("Error", "This book has already been returned");
                return;
            }
            
            if (libraryService.returnBook(currentUser.getUserId(), selected.getBookIsbn())) {
                refreshTables();
                showAlert("Success", "Book returned successfully");
            } else {
                showAlert("Error", "Failed to return book");
            }
        });
        
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> new MainMenuScreen(stage, currentUser).show());
        
        // Layout
        VBox availableBooksBox = new VBox(10);
        availableBooksBox.setPadding(new Insets(10));
        availableBooksBox.getChildren().addAll(availableBooksLabel, bookTable, borrowButton);
        
        VBox myBooksBox = new VBox(10);
        myBooksBox.setPadding(new Insets(10));
        myBooksBox.getChildren().addAll(myBooksLabel, recordTable, returnButton);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(backButton);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(availableBooksBox, myBooksBox, buttonBox);
        
        Scene scene = new Scene(root, 900, 700);
        stage.setTitle("Library Management System - Borrow / Return Books");
        stage.setScene(scene);
        stage.show();
        
        refreshTables();
    }
    
    /**
     * Refresh both tables
     */
    private void refreshTables() {
        // Refresh available books
        availableBooks.clear();
        libraryService.getAllBooks().stream()
            .filter(Book::isAvailable)
            .forEach(availableBooks::add);
        
        // Refresh user's borrow records
        userBorrowRecords.clear();
        libraryService.getAllBorrowRecords().stream()
            .filter(record -> record.getUserId().equals(currentUser.getUserId()))
            .forEach(userBorrowRecords::add);
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

