# Library Management System

A Java Desktop GUI Application built with JavaFX for managing a library system. This project demonstrates Object-Oriented Programming principles and design patterns.

## System Architecture

The application follows a layered architecture:

```
┌─────────────────────────────────────┐
│         GUI Layer (JavaFX)          │
│  Login, ManageBooks, ManageUsers,   │
│      BorrowReturn Screens           │
└─────────────────────────────────────┘
                  │
┌─────────────────────────────────────┐
│         Service Layer                │
│      LibraryService                  │
└─────────────────────────────────────┘
                  │
┌─────────────────────────────────────┐
│      Factory Pattern Layer          │
│   BookFactory, UserFactory          │
└─────────────────────────────────────┘
                  │
┌─────────────────────────────────────┐
│      Singleton Pattern Layer        │
│  DatabaseConnectionManager, Logger  │
└─────────────────────────────────────┘
                  │
┌─────────────────────────────────────┐
│         Model Layer                  │
│  Book, User, BorrowRecord           │
└─────────────────────────────────────┘
                  │
┌─────────────────────────────────────┐
│         Database (SQLite)           │
└─────────────────────────────────────┘
```

## Project Structure

```
src/main/java/com/library/
├── model/                    # Model classes
│   ├── Book.java            # Abstract book class
│   ├── SoftwareEngineeringBook.java
│   ├── ManagementBook.java
│   ├── AIBook.java
│   ├── User.java            # Abstract user class
│   ├── Admin.java
│   ├── RegularUser.java
│   └── BorrowRecord.java
├── factory/                  # Factory pattern implementation
│   ├── BookFactory.java
│   └── UserFactory.java
├── singleton/                # Singleton pattern implementation
│   ├── DatabaseConnectionManager.java
│   └── Logger.java
├── gui/                      # GUI screens
│   ├── LoginScreen.java
│   ├── MainMenuScreen.java
│   ├── ManageBooksScreen.java
│   ├── ManageUsersScreen.java
│   └── BorrowReturnScreen.java
├── util/                     # Utility classes
│   └── LibraryService.java
└── LibraryApplication.java   # Main application class
```

## OOP Concepts Implemented

### 1. Encapsulation
- All model classes use private fields with public getters/setters
- Singleton classes use private constructors
- Factory classes encapsulate object creation logic

### 2. Inheritance
- `Book` abstract class with subclasses: `SoftwareEngineeringBook`, `ManagementBook`, `AIBook`
- `User` abstract class with subclasses: `Admin`, `RegularUser`
- `LibraryApplication` extends JavaFX `Application`

### 3. Polymorphism
- Abstract methods in `Book` and `User` classes are overridden in subclasses
- Factory methods return abstract types (`Book`, `User`) but create concrete instances
- Method overriding for `getCategory()`, `getRole()`, `isAdmin()`

### 4. Abstraction
- Abstract `Book` class defines common interface without implementation
- Abstract `User` class defines common interface without implementation
- Abstract methods enforce contract for subclasses

## Design Patterns

### Singleton Pattern

**DatabaseConnectionManager**
- Ensures only one database connection exists
- Provides global access point via `getInstance()`
- Manages SQLite database connection and initialization

**Logger**
- Ensures only one logger instance exists
- Provides centralized logging functionality
- All log entries go to the same file

### Factory Pattern

**BookFactory**
- Creates different book types without exposing creation logic
- Returns abstract `Book` type (polymorphism)
- Easy to extend with new book categories

**UserFactory**
- Creates different user types without exposing creation logic
- Returns abstract `User` type (polymorphism)
- Easy to extend with new user roles

## Features

1. **User Authentication**
   - Login screen with username/password
   - Role-based access control (Admin vs Regular User)

2. **Book Management**
   - Add, Update, Delete books
   - View all books in a table
   - Support for multiple book categories (Software Engineering, Management, AI)

3. **User Management** (Admin only)
   - Add, Update, Delete users
   - Create Admin or Regular User accounts
   - View all users in a table

4. **Borrow/Return Books**
   - View available books
   - Borrow books (updates availability)
   - Return books (updates availability)
   - View personal borrowing history

5. **Logging**
   - All important actions are logged
   - Logs saved to `library.log` file
   - Different log levels (INFO, WARNING, ERROR)

## Requirements

- Java 11 or higher
- Maven 3.6 or higher
- SQLite (included via Maven dependency)

## How to Run

### Using Maven:

```bash
# Compile the project
mvn clean compile

# Run the application
mvn javafx:run
```

### Using IDE:

1. Import the project as a Maven project
2. Run `LibraryApplication.java` as a Java application
3. Make sure JavaFX SDK is configured in your IDE

## Default Login Credentials

- **Username:** admin
- **Password:** admin123
- **Role:** Admin

## Database

The application uses SQLite database (`library.db`) which is automatically created on first run. The database includes:

- `books` table: Stores book information
- `users` table: Stores user information
- `borrow_records` table: Stores borrowing transactions

## Logging

All application logs are written to `library.log` file in the project root directory. Logs include:
- User login/logout
- Book operations (add, update, delete)
- User operations (add, update, delete)
- Borrow/return transactions
- Error messages

## Notes

- This project strictly follows the requirements: Only Singleton and Factory patterns are used
- No additional design patterns are implemented
- Code is well-documented with JavaDoc comments
- Each class includes documentation about OOP concepts and design patterns used

## Author

University Project - Library Management System
IT-L4 Selected Labs - Project 1

