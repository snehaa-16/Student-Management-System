# Student Management System

A console-based Student Management System developed using **Java 17, JDBC, MySQL, and Maven**. The application provides database-backed student record management with user registration, login authentication, and role-based access control.

## Features

### Student Management
- Add new student records (ADMIN only)
- View all student records
- Search students by ID
- Update student information (ADMIN only)
- Delete student records with confirmation (ADMIN only)
- Prevent duplicate student IDs
- Validate user inputs
- Handle database errors and invalid inputs

### User Authentication
- User registration and login
- Secure password hashing using BCrypt
- Role-based access control (ADMIN and USER)
- First-time ADMIN account initialization
- Prevent duplicate usernames
- Password input handled through a shared input utility

## Technologies Used

- Java 17
- Object-Oriented Programming (OOP)
- JDBC
- MySQL
- Maven
- BCrypt
- Git and GitHub
- GitHub Actions

## OOP Concepts Used

- Classes and Objects
- Encapsulation
- Constructors
- Access Modifiers
- Getters and Setters
- Methods
- Enums

## Role-Based Access

The application supports two user roles:

| Role | Permissions |
|---|---|
| ADMIN | Add, view, search, update and delete student records |
| USER | View and search student records |

Student management operations also contain role checks in the application code.

## Database Structure

The project uses a MySQL database named `student_management`.

The database schema is provided in the root-level `database.sql` file.

It contains three tables:

**1. students**
- id (Primary Key)
- name
- age
- course
- marks

**2. users**
- id (Primary Key)
- username (Unique)
- password_hash
- role
- account_status
- failed_attempts
- locked_until
- created_at

**3. audit_log**
- id (Primary Key)
- user_id
- username
- action
- details
- created_at

Note: The audit log and account-lockout fields are defined in the database schema; their functionality is not yet integrated into the Java application.

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
cd Student-Management-System
```

### 2. Create the database

Open MySQL Workbench and execute the SQL statements from the root-level `database.sql` file.

This creates the `student_management` database and its tables.

### 3. Configure database credentials

The application reads database credentials through environment variables.

Set the following variables:

- `DB_URL` = `jdbc:mysql://localhost:3306/student_management`
- `DB_USER` = Your MySQL username
- `DB_PASSWORD` = Your MySQL password

Do not hardcode database credentials in the Java source code.

### 4. Build the project

Open a
