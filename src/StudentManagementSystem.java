            handleSQLException("update student", e);
        }
    }

    public void deleteStudent() {
        int id = readInt("Enter Student ID to delete: ", MIN_ID, Integer.MAX_VALUE);

        // Step 1: Check whether the student exists.
        try {
            if (!studentExists(id)) {
                System.out.println("Student not found.");
                return;
            }
        } catch (SQLException e) {
            handleSQLException("check student", e);
            return;
        }

        // Step 2: Ask for confirmation.
        boolean confirmed = readYesNo(
                "Are you sure you want to delete student ID " + id + "? (Y/N): ");

        if (!confirmed) {
            System.out.println("Deletion cancelled.");
            return;
        }

        // Step 3: Delete the student.
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Delete failed: the student no longer exists. "
                        + "It may have already been deleted. No changes were made.");
            }

        } catch (SQLException e) {
            handleSQLException("delete student", e);
        }
    }

    // ---------- Menu ----------

    public void start() {
        try {
            while (true) {
                System.out.println("\n=================================");
                System.out.println("      STUDENT MANAGEMENT SYSTEM");
                System.out.println("=================================");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Search Student");
                System.out.println("4. Update Student");
                System.out.println("5. Delete Student");
                System.out.println("6. Exit");
                System.out.println("=================================");

                int choice = readInt("Enter your choice (1-6): ", 1, 6);

                switch (choice) {
                    case 1:
                        addStudent();
                        break;

                    case 2:
                        viewStudents();
                        break;

                    case 3:
                        searchStudent();
                        break;

                    case 4:
                        updateStudent();
                        break;

                    case 5:
                        deleteStudent();
                        break;

                    case 6:
                        System.out.println(
                                "Thank you for using Student Management System!"
                        );
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("\nInput was closed. Exiting program.");
        }
    }
}
