
CREATE DATABASE IF NOT EXISTS student_management;

USE student_management;

-- Table for user registration and login
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(30) NOT NULL PRIMARY KEY,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL,
    CHECK (role IN ('ADMIN', 'USER'))
);

-- Table for student records
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    course VARCHAR(100) NOT NULL,
    marks DOUBLE NOT NULL
);
