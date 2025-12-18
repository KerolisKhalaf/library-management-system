package com.library.gui;

import com.library.model.User;
import com.library.util.LibraryService;
import com.library.singleton.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * LoginScreen class for user authentication.
 * 
 * Purpose: Provides login interface for the library management system.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private fields and methods
 * 
 * Design Pattern: None
 */
public class LoginScreen {
    private Stage stage;
    private LibraryService libraryService;
    private Logger logger;
    private User currentUser;
    
    public LoginScreen(Stage stage) {
        this.stage = stage;
        this.libraryService = new LibraryService();
        this.logger = Logger.getInstance();
    }
    
    /**
     * Display the login screen
     */
    public void show() {
        // Create UI components
        Label titleLabel = new Label("Library Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        
        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter both username and password");
                return;
            }
            
            User user = libraryService.getUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                logger.logInfo("User logged in: " + username);
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
                
                // Navigate to main menu after successful login
                new MainMenuScreen(stage, currentUser).show();
            } else {
                messageLabel.setText("Invalid username or password");
                messageLabel.setStyle("-fx-text-fill: red;");
                logger.logWarning("Failed login attempt: " + username);
            }
        });
        
        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(messageLabel, 0, 3, 2, 1);
        
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(titleLabel, grid);
        
        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Library Management System - Login");
        stage.setScene(scene);
        stage.show();
    }
}

