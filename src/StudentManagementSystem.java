import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentManagementSystem {

    private Scanner scanner;

    public StudentManagementSystem() {
        scanner = new Scanner(System.in);
    }

    public void addStudent() {

        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Student Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Course: ");
        String course = scanner.nextLine();

        System.out.print("Enter Marks: ");
        double marks = scanner.nextDouble();
        scanner.nextLine();

        String sql = "INSERT INTO students (id, name, age, course, marks) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setInt(3, age);
            statement.setString(4, course);
            statement.setDouble(5, marks);

            statement.executeUpdate();

            System.out.println("Student added successfully!");

        } catch (SQLException e) {

            System.out.println("Unable to add student.");

            if (e.getErrorCode() == 1062) {
                System.out.println("Student ID already exists!");
            } else {
                e.printStackTrace();
            }
        }
    }

    public void viewStudents() {

        String sql = "SELECT * FROM students";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            boolean found = false;

            System.out.println("\n===== ALL STUDENTS =====");

            while (result.next()) {

                found = true;

                Student student = new Student(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("age"),
                        result.getString("course"),
                        result.getDouble("marks")
                );

                student.displayStudent();
            }

            if (!found) {
                System.out.println("No students found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve students.");
            e.printStackTrace();
        }
    }

    public void searchStudent() {

        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Student student = new Student(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getInt("age"),
                        result.getString("course"),
                        result.getDouble("marks")
                );

                System.out.println("\nStudent found!");
                student.displayStudent();

            } else {
                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to search student.");
            e.printStackTrace();
        }
    }

    public void updateStudent() {

        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();

        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new course: ");
        String course = scanner.nextLine();

        System.out.print("Enter new marks: ");
        double marks = scanner.nextDouble();
        scanner.nextLine();

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
                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to update student.");
            e.printStackTrace();
        }
    }

    public void deleteStudent() {

        System.out.print("Enter Student ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student not found.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to delete student.");
            e.printStackTrace();
        }
    }

    public void start() {

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

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

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
    }
}
