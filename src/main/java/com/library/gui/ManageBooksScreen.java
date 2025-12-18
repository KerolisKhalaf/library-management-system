package com.library.gui;

import com.library.model.Book;
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

/**
 * ManageBooksScreen class for book management.
 * 
 * Purpose: Provides interface for adding, updating, deleting, and viewing books.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private fields and methods
 * 
 * Design Pattern: None
 */
public class ManageBooksScreen {
    private Stage stage;
    private User currentUser;
    private LibraryService libraryService;
    private ObservableList<Book> books;
    private TableView<Book> bookTable;
    
    public ManageBooksScreen(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.libraryService = new LibraryService();
        this.books = FXCollections.observableArrayList();
    }
    
    /**
     * Display the manage books screen
     */
    public void show() {
        // Create table columns
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(120);
        
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        
        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);
        
        TableColumn<Book, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setPrefWidth(80);
        
        TableColumn<Book, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setPrefWidth(150);
        
        TableColumn<Book, Boolean> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        availableCol.setPrefWidth(100);
        
        // Create table
        bookTable = new TableView<>();
        bookTable.setItems(books);
        bookTable.getColumns().add(isbnCol);
        bookTable.getColumns().add(titleCol);
        bookTable.getColumns().add(authorCol);
        bookTable.getColumns().add(yearCol);
        bookTable.getColumns().add(categoryCol);
        bookTable.getColumns().add(availableCol);
        bookTable.setPrefHeight(400);
        
        // Form fields
        TextField isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        
        TextField yearField = new TextField();
        yearField.setPromptText("Year");
        
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("SoftwareEngineering", "Management", "AI");
        categoryCombo.setPromptText("Category");
        
        // Buttons
        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> {
            try {
                String isbn = isbnField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String category = categoryCombo.getValue();
                
                if (isbn.isEmpty() || title.isEmpty() || author.isEmpty() || category == null) {
                    showAlert("Error", "Please fill all fields");
                    return;
                }
                
                Book book = com.library.factory.BookFactory.createBook(category, isbn, title, author, year);
                if (libraryService.addBook(book)) {
                    refreshTable();
                    clearFields(isbnField, titleField, authorField, yearField, categoryCombo);
                    showAlert("Success", "Book added successfully");
                } else {
                    showAlert("Error", "Failed to add book. ISBN might already exist.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid year");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });
        
        Button updateButton = new Button("Update Book");
        updateButton.setOnAction(e -> {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a book to update");
                return;
            }
            
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String category = categoryCombo.getValue();
                
                if (title.isEmpty() || author.isEmpty() || category == null) {
                    showAlert("Error", "Please fill all fields");
                    return;
                }
                
                selected.setTitle(title);
                selected.setAuthor(author);
                selected.setYear(year);
                
                if (libraryService.updateBook(selected)) {
                    refreshTable();
                    clearFields(isbnField, titleField, authorField, yearField, categoryCombo);
                    showAlert("Success", "Book updated successfully");
                } else {
                    showAlert("Error", "Failed to update book");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid year");
            }
        });
        
        Button deleteButton = new Button("Delete Book");
        deleteButton.setOnAction(e -> {
            Book selected = bookTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a book to delete");
                return;
            }
            
            if (showConfirmDialog("Delete Book", "Are you sure you want to delete this book?")) {
                if (libraryService.deleteBook(selected.getIsbn())) {
                    refreshTable();
                    showAlert("Success", "Book deleted successfully");
                } else {
                    showAlert("Error", "Failed to delete book");
                }
            }
        });
        
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> new MainMenuScreen(stage, currentUser).show());
        
        // Populate fields when row is selected
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                isbnField.setText(newSelection.getIsbn());
                titleField.setText(newSelection.getTitle());
                authorField.setText(newSelection.getAuthor());
                yearField.setText(String.valueOf(newSelection.getYear()));
                categoryCombo.setValue(newSelection.getCategory());
            }
        });
        
        // Form layout
        HBox formBox = new HBox(10);
        formBox.setPadding(new Insets(10));
        formBox.getChildren().addAll(
            new Label("ISBN:"), isbnField,
            new Label("Title:"), titleField,
            new Label("Author:"), authorField,
            new Label("Year:"), yearField,
            new Label("Category:"), categoryCombo
        );
        
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, backButton);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(bookTable, formBox, buttonBox);
        
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Library Management System - Manage Books");
        stage.setScene(scene);
        stage.show();
        
        refreshTable();
    }
    
    /**
     * Refresh the book table
     */
    private void refreshTable() {
        books.clear();
        books.addAll(libraryService.getAllBooks());
    }
    
    /**
     * Clear form fields
     */
    private void clearFields(TextField isbn, TextField title, TextField author, 
                            TextField year, ComboBox<String> category) {
        isbn.clear();
        title.clear();
        author.clear();
        year.clear();
        category.setValue(null);
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
    
    /**
     * Show confirmation dialog
     */
    private boolean showConfirmDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}

