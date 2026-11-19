import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StudentManagementSystem {

    private static final int MIN_ID = 1;
    private static final int MIN_AGE = 5;
    private static final int MAX_AGE = 100;
    private static final double MIN_MARKS = 0;
    private static final double MAX_MARKS = 100;
    private static final int MAX_TEXT_LENGTH = 100;

    private Scanner scanner;
    private Role currentRole;

public StudentManagementSystem() {
    this(new Scanner(System.in));
}

public StudentManagementSystem(Scanner scanner) {
    this.scanner = scanner;
}
    // ---------- Input helper methods ----------

    private int readInt(String prompt, int min, int max) {

        while (true) {

            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(line);

                if (value < min || value > max) {
                    String range = (max == Integer.MAX_VALUE)
                            ? "a number " + min + " or greater"
                            : "a number between " + min + " and " + max;
                    System.out.println("Invalid input. Please enter " + range + ".");
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number (digits only).");
            }
        }
    }

    private double readDouble(String prompt, double min, double max) {

        while (true) {

            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(line);

                // Written this way so NaN and Infinity are rejected too
                if (!(value >= min && value <= max)) {
                    System.out.println("Invalid input. Please enter a number between "
                            + min + " and " + max + ".");
                } else {
                    return value;
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (for example 75.5).");
            }
        }
    }

    private String readText(String prompt, String fieldName) {

        while (true) {

            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println(fieldName + " cannot be empty.");
            } else if (line.length() > MAX_TEXT_LENGTH) {
                System.out.println(fieldName + " must be at most "
                        + MAX_TEXT_LENGTH + " characters.");
            } else {
                return line;
            }
        }
    }

    private boolean readYesNo(String prompt) {

        while (true) {

            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("Y")) {
                return true;
            } else if (line.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
        }
    }

    // ---------- Database error helper ----------

    private void handleSQLException(String action, SQLException e) {

        System.out.println("Unable to " + action + ".");

        String message = e.getMessage();

        // These two messages are written by our own DatabaseConnection class
        // and never contain passwords or connection details, so they are safe.
        if (message != null && (message.startsWith("Missing environment variable")
                || message.startsWith("Could not connect"))) {

            System.out.println(message);

        } else {
            System.out.println("A database error occurred (error code: "
                    + e.getErrorCode() + ", SQL state: " + e.getSQLState() + ").");

            if (e.getErrorCode() == 1146) {
                System.out.println("Hint: the 'students' table may be missing. "
                        + "Run database.sql first.");
            }
        }
    }

    // ---------- Shared database helper ----------

    private boolean studentExists(int id) throws SQLException {

        String sql = "SELECT id FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    // ---------- CRUD operations ----------

    public void addStudent() {
        if (currentRole != Role.ADMIN) {
    System.out.println("Access denied. Administrator permission required.");
    return;
}

        int id = readInt("Enter Student ID: ", MIN_ID, Integer.MAX_VALUE);

        // Check for a duplicate ID BEFORE asking for the other details.
        try {
            if (studentExists(id)) {
                System.out.println("Student ID already exists! Student was not added.");
                return;
            }
        } catch (SQLException e) {
            handleSQLException("check student", e);
            return;
        }

        String name = readText("Enter Student Name: ", "Name");
        int age = readInt("Enter Student Age: ", MIN_AGE, MAX_AGE);
        String course = readText("Enter Course: ", "Course");
        double marks = readDouble("Enter Marks (0-100): ", MIN_MARKS, MAX_MARKS);

        String sql = "INSERT INTO students (id, name, age, course, marks) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setInt(3, age);
            statement.setString(4, course);
            statement.setDouble(5, marks);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Student added successfully!");
            } else {
                System.out.println("Student was not added. No changes were made.");
            }

        } catch (SQLException e) {

            // Safeguard: someone else may have added the same ID
            // after our check above.
            if (e.getErrorCode() == 1062) {
                System.out.println("Unable to add student.");
                System.out.println("Student ID already exists!");
            } else {
                handleSQLException("add student", e);
            }
        }
    }

    public void viewStudents() {

        String sql = "SELECT id, name, age, course, marks FROM students ORDER BY id";

        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                students.add(new Student(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("age"),
                        result.getString("course"),
                        result.getDouble("marks")
                ));
            }

        } catch (SQLException e) {
            handleSQLException("retrieve students", e);
            return;
        }

        System.out.println("\n===== ALL STUDENTS =====");

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        String line = "-".repeat(65);

        System.out.println(line);
        System.out.printf("%-6s %-22s %-5s %-22s %6s%n",
                "ID", "Name", "Age", "Course", "Marks");
        System.out.println(line);

        for (Student student : students) {
            System.out.printf("%-6d %-22.22s %-5d %-22.22s %6.2f%n",
                    student.getId(),
                    student.getName(),
                    student.getAge(),
                    student.getCourse(),
                    student.getMarks());
        }

        System.out.println(line);
        System.out.println("Total students: " + students.size());
    }

    public void searchStudent() {

        int id = readInt("Enter Student ID: ", MIN_ID, Integer.MAX_VALUE);

        String sql = "SELECT id, name, age, course, marks FROM students WHERE id = ?";

        Student student = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    student = new Student(
                            result.getInt("id"),
                            result.getString("name"),
                            result.getInt("age"),
                            result.getString("course"),
                            result.getDouble("marks")
                    );
                }
            }

        } catch (SQLException e) {
            handleSQLException("search student", e);
            return;
        }

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("\nStudent found!");
            student.displayStudent();
        }
    }

    public void updateStudent() {

        int id = readInt("Enter Student ID to update: ", MIN_ID, Integer.MAX_VALUE);

        try {
            if (!studentExists(id)) {
                System.out.println("Student not found.");
                return;
            }
        } catch (SQLException e) {
            handleSQLException("check student", e);
            return;
        }

        System.out.println("Student found. Enter the new details.");

        String name = readText("Enter new name: ", "Name");
        int age = readInt("Enter new age: ", MIN_AGE, MAX_AGE);
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
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Update failed: the student no longer exists. "
                        + "It may have been deleted. No changes were made.");
            }

        } catch (SQLException e) {
            handleSQLException("update student", e);
        }
    }

    public void deleteStudent() {

        int id = readInt("Enter Student ID to delete: ", MIN_ID, Integer.MAX_VALUE);

        try {
            if (!studentExists(id)) {
                System.out.println("Student not found.");
                return;
            }
        } catch (SQLException e) {
            handleSQLException("check student", e);
            return;
        }

        boolean confirmed = readYesNo(
                "Are you sure you want to delete student ID " + id + "? (Y/N): ");

        if (!confirmed) {
            System.out.println("Deletion cancelled.");
            return;
        }

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

   
public void start(Role role) {
    if (role == null) {
        System.out.println("Access denied.");
        return;
    }
    currentRole = role;

    try {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("      STUDENT MANAGEMENT SYSTEM");
            System.out.println("      Logged in as: " + role);
            System.out.println("=================================");

            if (role == Role.ADMIN) {
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Search Student");
                System.out.println("4. Update Student");
                System.out.println("5. Delete Student");
                System.out.println("6. Logout");

                int choice = readInt("Enter choice (1-6): ", 1, 6);

                switch (choice) {
                    case 1: addStudent(); break;
                    case 2: viewStudents(); break;
                    case 3: searchStudent(); break;
                    case 4: updateStudent(); break;
                    case 5: deleteStudent(); break;
                    case 6:
                        System.out.println("Logged out.");
                        return;
                }
            } else {
                System.out.println("1. View All Students");
                System.out.println("2. Search Student");
                System.out.println("3. Logout");

                int choice = readInt("Enter choice (1-3): ", 1, 3);

                switch (choice) {
                    case 1: viewStudents(); break;
                    case 2: searchStudent(); break;
                    case 3:
                        System.out.println("Logged out.");
                        return;
                }
            }
        }
    } catch (NoSuchElementException e) {
        System.out.println("\nInput was closed. Exiting program.");
    }
}


       
}
