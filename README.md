# Student Management System

A console-based Student Management System developed using **Java, JDBC, and MySQL**. The application allows users to manage student records through a menu-driven interface with database connectivity.

## Features

- Add new student records
- View all students
- Search students by ID
- Update student information
- Delete students with confirmation
- Prevent duplicate student IDs
- Validate user inputs
- Store student records in a MySQL database
- Handle database errors and invalid inputs

## Technologies Used

- Java 17
- Object-Oriented Programming (OOP)
- JDBC
- MySQL
- Maven
- Git and GitHub

## OOP Concepts Used

- Classes and Objects
- Encapsulation
- Constructors
- Access Modifiers
- Getters and Setters
- Methods

## Database Configuration

The project uses a MySQL database named `student_management`.

The database contains a table named `students` with the following fields:

| Field | Data Type |
|---|---|
| id | INT (Primary Key) |
| name | VARCHAR(100) |
| age | INT |
| course | VARCHAR(100) |
| marks | DOUBLE |

The SQL database structure is provided in `database.sql`.

## Prerequisites

Before running the project, install:

- JDK 17 or later
- Apache Maven
- MySQL Server
- MySQL Workbench (optional)

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/snehaa-16/Student-Management-System.git
```

### 2. Create the database

Open MySQL Workbench and execute the SQL statements from `database.sql`.

This creates the `student_management` database and the `students` table.

### 3. Configure database credentials

The application reads database credentials through environment variables.

Set the following variables:

- `DB_URL` = `jdbc:mysql://localhost:3306/student_management`
- `DB_USER` = Your MySQL username
- `DB_PASSWORD` = Your MySQL password

Do not hardcode your database password in the Java source code.

### 4. Build the project

Open a terminal in the project directory and run:

```bash
mvn clean compile
```

### 5. Run the application

Open the project in a Java IDE, configure the database environment variables in the run configuration, and run `Main.java`.

## Project Structure

```text
Student-Management-System
│
├── src
│   ├── Main.java
│   ├── Student.java
│   ├── StudentManagementSystem.java
│   └── DatabaseConnection.java
│
├── database.sql
├── pom.xml
├── README.md
└── .gitignore
```

## Future Enhancements

- Graphical User Interface (GUI)
- Student login and authentication
- Improved reporting and data visualization
- Web-based application using Spring Boot

## Author

Developed as a B.Tech Computer Science and Engineering project.
