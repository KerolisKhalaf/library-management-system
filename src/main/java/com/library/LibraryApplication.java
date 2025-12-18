package com.library;

import com.library.gui.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * LibraryApplication class - Main entry point for the Library Management System.
 * 
 * Purpose: Launches the JavaFX application and displays the login screen.
 * 
 * OOP Concepts Used:
 * - Inheritance: Extends JavaFX Application class
 * 
 * Design Pattern: None
 */
public class LibraryApplication extends Application {
    
    /**
     * Main method to launch the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Start method called by JavaFX framework
     * @param primaryStage Primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize and show login screen
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        loginScreen.show();
    }
    
    /**
     * Stop method called when application is closing
     */
    @Override
    public void stop() {
        // Close database connection and logger
        com.library.singleton.DatabaseConnectionManager.getInstance().closeConnection();
        com.library.singleton.Logger.getInstance().close();
    }
}

