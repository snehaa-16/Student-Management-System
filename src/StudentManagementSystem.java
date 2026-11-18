import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentManagementSystem {

    private static final int MIN_ID = 1;
    private static final int MIN_AGE = 5;
    private static final int MAX_AGE = 100;
    private static final double MIN_MARKS = 0;
    private static final double MAX_MARKS = 100;
    private static final int MAX_TEXT_LENGTH = 100;

    private final Scanner scanner;

    public StudentManagementSystem() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Exit");

            int choice = readInt("Enter your choice: ", 1, 6);

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
                    running = false;
                    System.out.println("Thank you for using Student Management System.");
                    break;
                default:
                    // readInt() already restricts the choice to 1 through 6.
                    break;
            }
        }
    }

    // ---------- Input helper methods ----------

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    String range = max == Integer.MAX_VALUE
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
                if (!(value >= min && value <= max)) {
                    System.out.println("Invalid input. Please enter a number between "
                            + min + " and " + max + ".");
                } else {
