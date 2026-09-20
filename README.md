# Student Management System

A console-based Student Management System developed using Java, JDBC, and MySQL.

## Features

- Add new students
- View all students
- Search students by ID
- Update student information
- Delete students
- Prevent duplicate student IDs
- Store student records in a MySQL database
- Menu-driven console interface

## Technologies Used

- Java
- Object-Oriented Programming (OOP)
- JDBC
- MySQL
- Maven

## OOP Concepts Used

- Classes and Objects
- Encapsulation
- Constructors
- Access Modifiers
- Getters and Setters
- Methods

## Database

The project uses a MySQL database named:

`student_management`

The database contains a `students` table with the following fields:

| Field | Type |
|---|---|
| id | INT |
| name | VARCHAR(100) |
| age | INT |
| course | VARCHAR(100) |
| marks | DOUBLE |

The SQL database structure is available in `database.sql`.

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
