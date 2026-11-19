-- =====================================================
-- Student Management System - database setup
-- Safe to run more than once (uses IF NOT EXISTS).
-- =====================================================

CREATE DATABASE IF NOT EXISTS student_management;

USE student_management;

-- -----------------------------------------------------
-- Existing table: students (structure unchanged)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    course VARCHAR(100) NOT NULL,
    marks DOUBLE NOT NULL
);

-- -----------------------------------------------------
-- New table: users (authentication, roles, lockout)
-- password_hash stores a BCrypt hash, never a plain password.
-- username is explicitly case-insensitive (_ci collation),
-- so "Admin" and "admin" are the same account.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)
        CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
        NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
    account_status ENUM('ACTIVE', 'DISABLED') NOT NULL DEFAULT 'ACTIVE',
    failed_attempts INT NOT NULL DEFAULT 0,
    locked_until DATETIME NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------
-- New table: audit_log (who did what, and when)
-- Never store passwords, hashes or secrets here.
-- username uses the same collation as users.username.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    username VARCHAR(50)
        CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci
        NULL,
    action VARCHAR(50) NOT NULL,
    details VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE SET NULL
);
