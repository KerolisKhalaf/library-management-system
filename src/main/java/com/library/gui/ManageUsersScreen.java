package com.library.gui;

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
 * ManageUsersScreen class for user management.
 * 
 * Purpose: Provides interface for adding, updating, deleting, and viewing users.
 * Only accessible to admin users.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private fields and methods
 * 
 * Design Pattern: None
 */
public class ManageUsersScreen {
    private Stage stage;
    private User currentUser;
    private LibraryService libraryService;
    private ObservableList<User> users;
    private TableView<User> userTable;
    
    public ManageUsersScreen(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.libraryService = new LibraryService();
        this.users = FXCollections.observableArrayList();
        
        // Check if user is admin
        if (!currentUser.isAdmin()) {
            showAlert("Access Denied", "Only administrators can access this screen");
            new MainMenuScreen(stage, currentUser).show();
            return;
        }
    }
    
    /**
     * Display the manage users screen
     */
    public void show() {
        // Create table columns
        TableColumn<User, String> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userIdCol.setPrefWidth(120);
        
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(150);
        
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);
        
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(150);
        
        // Create table
        userTable = new TableView<>();
        userTable.setItems(users);
        userTable.getColumns().add(userIdCol);
        userTable.getColumns().add(usernameCol);
        userTable.getColumns().add(emailCol);
        userTable.getColumns().add(roleCol);
        userTable.setPrefHeight(400);
        
        // Form fields
        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Admin", "RegularUser");
        roleCombo.setPromptText("Role");
        
        // Buttons
        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> {
            String userId = userIdField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();
            
            if (userId.isEmpty() || username.isEmpty() || password.isEmpty() || 
                email.isEmpty() || role == null) {
                showAlert("Error", "Please fill all fields");
                return;
            }
            
            User user = com.library.factory.UserFactory.createUser(role, userId, username, password, email);
            if (libraryService.addUser(user)) {
                refreshTable();
                clearFields(userIdField, usernameField, passwordField, emailField, roleCombo);
                showAlert("Success", "User added successfully");
            } else {
                showAlert("Error", "Failed to add user. User ID or username might already exist.");
            }
        });
        
        Button updateButton = new Button("Update User");
        updateButton.setOnAction(e -> {
            User selected = userTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a user to update");
                return;
            }
            
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleCombo.getValue();
            
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
                showAlert("Error", "Please fill all fields");
                return;
            }
            
            selected.setUsername(username);
            selected.setPassword(password);
            selected.setEmail(email);
            
            if (libraryService.updateUser(selected)) {
                refreshTable();
                clearFields(userIdField, usernameField, passwordField, emailField, roleCombo);
                showAlert("Success", "User updated successfully");
            } else {
                showAlert("Error", "Failed to update user");
            }
        });
        
        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> {
            User selected = userTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a user to delete");
                return;
            }
            
            if (selected.getUserId().equals(currentUser.getUserId())) {
                showAlert("Error", "You cannot delete your own account");
                return;
            }
            
            if (showConfirmDialog("Delete User", "Are you sure you want to delete this user?")) {
                if (libraryService.deleteUser(selected.getUserId())) {
                    refreshTable();
                    showAlert("Success", "User deleted successfully");
                } else {
                    showAlert("Error", "Failed to delete user");
                }
            }
        });
        
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> new MainMenuScreen(stage, currentUser).show());
        
        // Populate fields when row is selected
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                userIdField.setText(newSelection.getUserId());
                usernameField.setText(newSelection.getUsername());
                passwordField.setText(newSelection.getPassword());
                emailField.setText(newSelection.getEmail());
                roleCombo.setValue(newSelection.getRole());
            }
        });
        
        // Form layout
        HBox formBox = new HBox(10);
        formBox.setPadding(new Insets(10));
        formBox.getChildren().addAll(
            new Label("User ID:"), userIdField,
            new Label("Username:"), usernameField,
            new Label("Password:"), passwordField,
            new Label("Email:"), emailField,
            new Label("Role:"), roleCombo
        );
        
        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, backButton);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(userTable, formBox, buttonBox);
        
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Library Management System - Manage Users");
        stage.setScene(scene);
        stage.show();
        
        refreshTable();
    }
    
    /**
     * Refresh the user table
     */
    private void refreshTable() {
        users.clear();
        users.addAll(libraryService.getAllUsers());
    }
    
    /**
     * Clear form fields
     */
    private void clearFields(TextField userId, TextField username, PasswordField password, 
                            TextField email, ComboBox<String> role) {
        userId.clear();
        username.clear();
        password.clear();
        email.clear();
        role.setValue(null);
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

