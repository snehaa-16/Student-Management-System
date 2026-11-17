import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystem {

    private static final int MIN_ID = 1;
    private static final int MIN_AGE = 5;
    private static final int MAX_AGE = 100;
    private static final double MIN_MARKS = 0;
    private static final double MAX_MARKS = 100;
    private static final int MAX_TEXT_LENGTH = 100;

    private Scanner scanner;

    public StudentManagementSystem() {
        scanner = new Scanner(System.in);
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

                // Written this way so NaN and Infinity are rejected too.
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
