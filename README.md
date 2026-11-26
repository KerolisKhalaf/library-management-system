# Library Management System

A Java desktop application for managing a library's catalogue, user accounts, and book loans. The application uses JavaFX for its interface and SQLite for local data persistence. It is also packaged with Docker and can be accessed through a browser using noVNC.

## Features

- **Authentication and roles:** Sign in as an administrator or regular user. User administration is restricted to administrators.
- **Book catalogue:** Add, view, update, and remove books. Supported categories are Software Engineering, Management, and Artificial Intelligence.
- **User administration:** Administrators can create, view, update, and delete user accounts.
- **Loans and returns:** Browse available books, borrow and return books, and view personal loan history, including borrow and return dates.
- **Persistence:** Store books, users, and loan records in SQLite.
- **Activity logging:** Record application activity and errors with INFO, WARNING, and ERROR levels.
- **Automated tests:** JUnit tests cover factory mappings and core library service workflows.

## Technology stack

| Area | Technology |
| --- | --- |
| Language | Java (source and target compatibility: 11; Java 17 recommended) |
| Desktop UI | JavaFX 17.0.2 |
| Build and dependency management | Apache Maven |
| Database | SQLite with SQLite JDBC 3.36.0.3 |
| Testing | JUnit Jupiter 5.10.2, Maven Surefire 3.2.5 |
| Containerization | Docker and Docker Compose |
| Browser-based desktop access | Xvfb, x11vnc, and noVNC |
| Continuous integration | GitHub Actions |

## Architecture

The application separates the JavaFX interface from its library operations and persistence:

```text
JavaFX screens
      |
LibraryService
      |
Factories and domain models
      |
DatabaseConnectionManager ---- SQLite
Logger ------------------------ library.log
```

The project demonstrates object-oriented programming concepts including encapsulation, abstraction, inheritance, and polymorphism. The **Factory** pattern creates book and user types; **Singleton** classes manage the database connection and application logger.

## Getting started

### Run locally

Requirements: JDK 17 (Java 11+ compatible source), and Maven 3.6 or later.

```bash
# Run the automated tests
mvn test

# Launch the JavaFX application
mvn javafx:run
```

### Run with Docker

Requirements: Docker with the Compose plugin.

```bash
docker compose up --build
```

Open [http://localhost:6080/vnc.html?autoconnect=true](http://localhost:6080/vnc.html?autoconnect=true) to use the desktop application in your browser. The published port is bound to localhost. Docker Compose stores the database and log files in the persistent `library-data` volume.

Run the test suite inside the container with:

```bash
docker compose run --rm --no-deps --entrypoint mvn library -B -f /opt/app/pom.xml test
```

To stop the application, run `docker compose down`. The named data volume is retained.

## Demo account

On first run, the application initializes this administrator account:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | Administrator |

This is a demo/educational application, not a production authentication system. Do not expose it to untrusted users or reuse the demo credentials; passwords are currently stored as plain text in SQLite.

## Data and logs

The database contains `books`, `users`, and `borrow_records` tables. By default, `library.db` and `library.log` are created in the application's working directory. Set `LIBRARY_DATA_DIR` to use another directory; Docker sets it to `/data` and persists that directory in the `library-data` volume.

## Continuous integration

The GitHub Actions workflow in `.github/workflows/ci.yml` runs for pushes and pull requests. It sets up Java 17, runs `mvn clean verify` (including the automated tests), and builds the Docker image. The workflow validates the image but does not publish or deploy it.

## Project structure

```text
src/
├── main/java/com/library/
│   ├── factory/     # BookFactory and UserFactory
│   ├── gui/         # JavaFX login, catalogue, user, and loan screens
│   ├── model/       # Books, users, and loan records
│   ├── singleton/   # SQLite connection manager and logger
│   ├── util/        # LibraryService
│   └── LibraryApplication.java
└── test/java/com/library/
    ├── factory/     # Factory tests
    └── util/        # Library service workflow tests
```

## License

This project is distributed under the [MIT License](LICENSE).
