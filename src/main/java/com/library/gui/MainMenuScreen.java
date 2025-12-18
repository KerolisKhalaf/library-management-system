package com.library.gui;

import com.library.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainMenuScreen class for main navigation.
 * 
 * Purpose: Provides main menu interface after login.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private fields and methods
 * 
 * Design Pattern: None
 */
public class MainMenuScreen {
    private Stage stage;
    private User currentUser;
    
    public MainMenuScreen(Stage stage, User currentUser) {
        this.stage = stage;
        this.currentUser = currentUser;
    }
    
    /**
     * Display the main menu screen
     */
    public void show() {
        Label titleLabel = new Label("Library Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setStyle("-fx-font-size: 14px;");
        
        Button manageBooksButton = new Button("Manage Books");
        manageBooksButton.setPrefWidth(200);
        manageBooksButton.setPrefHeight(40);
        manageBooksButton.setStyle("-fx-font-size: 14px;");
        manageBooksButton.setOnAction(e -> new ManageBooksScreen(stage, currentUser).show());
        
        Button manageUsersButton = new Button("Manage Users");
        manageUsersButton.setPrefWidth(200);
        manageUsersButton.setPrefHeight(40);
        manageUsersButton.setStyle("-fx-font-size: 14px;");
        manageUsersButton.setOnAction(e -> new ManageUsersScreen(stage, currentUser).show());
        
        Button borrowReturnButton = new Button("Borrow / Return Books");
        borrowReturnButton.setPrefWidth(200);
        borrowReturnButton.setPrefHeight(40);
        borrowReturnButton.setStyle("-fx-font-size: 14px;");
        borrowReturnButton.setOnAction(e -> new BorrowReturnScreen(stage, currentUser).show());
        
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefWidth(200);
        logoutButton.setPrefHeight(40);
        logoutButton.setStyle("-fx-font-size: 14px;");
        logoutButton.setOnAction(e -> new LoginScreen(stage).show());
        
        // Hide manage users button for non-admin users
        if (!currentUser.isAdmin()) {
            manageUsersButton.setVisible(false);
        }
        
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(titleLabel, welcomeLabel, manageBooksButton, 
                manageUsersButton, borrowReturnButton, logoutButton);
        
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("Library Management System - Main Menu");
        stage.setScene(scene);
        stage.show();
    }
}

