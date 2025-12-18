package com.library.singleton;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class implementing Singleton Pattern.
 * 
 * Purpose: Handles application-wide logging for the library system.
 * Ensures only one logger instance exists throughout the application.
 * 
 * OOP Concepts Used:
 * - Encapsulation: Private constructor and instance variable
 * 
 * Design Pattern: Singleton Pattern
 * Why Singleton: Logging should be centralized and consistent.
 * Having a single logger instance ensures:
 * 1. All log entries go to the same file/stream
 * 2. Consistent log format across the application
 * 3. Efficient resource management (one file handle)
 * 4. Easy log management and monitoring
 */
public class Logger {
    // Singleton: Private static instance
    private static Logger instance;
    
    // Encapsulation: Private fields
    private PrintWriter logWriter;
    private String logFile = "library.log";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Singleton: Private constructor to prevent instantiation
    private Logger() {
        try {
            logWriter = new PrintWriter(new FileWriter(logFile, true), true);
        } catch (IOException e) {
            System.err.println("Error creating log file: " + e.getMessage());
        }
    }
    
    /**
     * Singleton: Global access point to get the instance
     * @return The single instance of Logger
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    /**
     * Log an information message
     * @param message Message to log
     */
    public void logInfo(String message) {
        log("INFO", message);
    }
    
    /**
     * Log a warning message
     * @param message Message to log
     */
    public void logWarning(String message) {
        log("WARNING", message);
    }
    
    /**
     * Log an error message
     * @param message Message to log
     */
    public void logError(String message) {
        log("ERROR", message);
    }
    
    /**
     * Log a message with specified level
     * @param level Log level (INFO, WARNING, ERROR)
     * @param message Message to log
     */
    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);
        
        if (logWriter != null) {
            logWriter.println(logEntry);
        }
        
        // Also print to console
        System.out.println(logEntry);
    }
    
    /**
     * Close the logger and flush all pending writes
     */
    public void close() {
        if (logWriter != null) {
            logWriter.close();
        }
    }
}

