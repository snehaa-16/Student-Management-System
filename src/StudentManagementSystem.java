        System.out.println("\n--- Search Student ---");
        int id = readInt("Enter student ID to search: ", MIN_ID, Integer.MAX_VALUE);
        String sql = "SELECT id, name, age, course, marks FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    displayStudent(resultSet);
                } else {
                    System.out.println("Student not found.");
                }
            }
        } catch (SQLException e) {
            handleSQLException("search student", e);
        }
    }

    private void updateStudent() {
        System.out.println("\n--- Update Student ---");
        int id = readInt("Enter student ID to update: ", MIN_ID, Integer.MAX_VALUE);

        try {
            if (!studentExists(id)) {
                System.out.println("Student not found.");
                return;
            }
        } catch (SQLException e) {
            handleSQLException("check student before update", e);
            return;
        }

        String name = readText("Enter new student name: ", "Name");
        int age = readInt("Enter new student age: ", MIN_AGE, MAX_AGE);
        String course = readText("Enter new course: ", "Course");
        double marks = readDouble("Enter new marks (0-100): ", MIN_MARKS, MAX_MARKS);

        String sql = "UPDATE students SET name = ?, age = ?, course = ?, marks = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, course);
            statement.setDouble(4, marks);
            statement.setInt(5, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("No student record was updated.");
            }
        } catch (SQLException e) {
            handleSQLException("update student", e);
        }
    }

    private void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        int id = readInt("Enter student ID to delete: ", MIN_ID, Integer.MAX_VALUE);

        try {
            if (!studentExists(id)) {
                System.out.println("Student not found.");
                return;
            }
        } catch (SQLException e) {
            handleSQLException("check student before delete", e);
            return;
        }

        if (!readYesNo("Are you sure you want to delete this student? (Y/N): ")) {
            System.out.println("Delete operation cancelled.");
            return;
        }

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student record was not found or was already deleted.");
            }
        } catch (SQLException e) {
            handleSQLException("delete student", e);
        }
    }

    // ---------- Display helper ----------

    private void displayStudent(ResultSet resultSet) throws SQLException {
        System.out.println("----------------------------");
        System.out.println("Student ID : " + resultSet.getInt("id"));
        System.out.println("Name       : " + resultSet.getString("name"));
        System.out.println("Age        : " + resultSet.getInt("age"));
        System.out.println("Course     : " + resultSet.getString("course"));
        System.out.println("Marks      : " + resultSet.getDouble("marks"));
        System.out.println("----------------------------");
    }
}
