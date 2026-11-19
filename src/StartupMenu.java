import java.sql.SQLException;
import java.util.Arrays;

public class StartupMenu {

    private final StudentManagementSystem studentSystem;
    private final InputHelper input;
    private final AuthService authService;

    public StartupMenu(StudentManagementSystem studentSystem, InputHelper input) {
        this.studentSystem = studentSystem;
        this.input = input;
        this.authService = new AuthService();
    }

    public void run() {

        if (!ensureAdminExists()) {
            return;
        }

        while (true) {
            System.out.println("\n=================================");
            System.out.println("          STARTUP MENU");
            System.out.println("=================================");
            System.out.println("1. Register New User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("=================================");

            String choice = input.readLine("Enter your choice: ");

            if (choice == null) {
                return;
            }

            switch (choice.trim()) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
        private void login() {
    System.out.println("\n===== USER LOGIN =====");

    String username = input.readLine("Username: ");

    if (username == null || username.trim().isEmpty()) {
        return;
    }

    char[] password = input.readPassword("Password: ");

    if (password == null) {
        return;
    }

    try {
        User user = authService.authenticate(username.trim(), password);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        System.out.println("Login successful. Welcome, " + user.getUsername() + "!");
        studentSystem.start(user.getRole());

    } catch (SQLException e) {
        System.out.println("Login failed because the database is unavailable.");
        } finally {
        wipe(password);
    }
}

    private boolean ensureAdminExists() {

    private boolean ensureAdminExists() {
        boolean setupNeeded;

        try {
            setupNeeded = authService.noUsersExist();
        } catch (SQLException | RuntimeException e) {
            System.out.println("Unable to start: the database could not be used. "
                    + "Please check your database configuration and setup.");
            return false;
        }

        if (!setupNeeded) {
            return true;
        }

        return setUpFirstAdmin();
    }

    private boolean setUpFirstAdmin() {
        System.out.println("\n===== FIRST-TIME SETUP =====");
        System.out.println("No accounts exist yet. Create the administrator account.");

        while (true) {
            String username = readValidUsername();

            if (username == null) {
                return false;
            }

            char[] password = readValidPassword(username);

            if (password == null) {
                return false;
            }

            RegistrationResult result;

            try {
                result = authService.createFirstAdmin(username, password);
            } finally {
                wipe(password);
            }

            if (result == RegistrationResult.SUCCESS) {
                System.out.println("Administrator account created.");
                return true;

            } else if (result == RegistrationResult.ALREADY_INITIALIZED) {
                System.out.println("Accounts already exist. First-admin setup is no longer available.");
                return false;

            } else if (result == RegistrationResult.USERNAME_TAKEN) {
                System.out.println("That username is not available. Please choose another.");

            } else if (result == RegistrationResult.BUSY) {
                System.out.println("Setup is being completed by another session. "
                        + "Please start the application again in a moment.");
                return false;

            } else {
                System.out.println("Could not create the administrator account. "
                        + "Please try again later.");
                return false;
            }
        }
    }

    private void registerUser() {
        System.out.println("\n===== REGISTER NEW USER =====");

        String username = readValidUsername();

        if (username == null) {
            return;
        }

        char[] password = readValidPassword(username);

        if (password == null) {
            return;
        }

        try {
            RegistrationResult result =
                    authService.registerUser(username, password);

            if (result == RegistrationResult.SUCCESS) {
                System.out.println("Registration successful. Your account has the USER role.");
            } else if (result == RegistrationResult.USERNAME_TAKEN) {
                System.out.println("That username is not available.");
            } else {
                System.out.println("Registration failed. Please try again later.");
            }
        } finally {
            wipe(password);
        }
    }

    private String readValidUsername() {
        while (true) {
            String line = input.readLine("Username (blank to cancel): ");

            if (line == null || line.trim().isEmpty()) {
                return null;
            }

            String username = line.trim();
            String problem = authService.validateUsername(username);

            if (problem == null) {
                return username;
            }

            System.out.println(problem);
        }
    }

    private char[] readValidPassword(String username) {
        System.out.println("Password rules: 8-64 characters, at least one letter "
                + "and one digit, not the same as the username.");

        while (true) {
            char[] password = input.readPassword("Password (blank to cancel): ");

            if (password == null || password.length == 0) {
                wipe(password);
                return null;
            }

            String problem = authService.validatePassword(password, username);

            if (problem != null) {
                System.out.println(problem);
                wipe(password);
                continue;
            }

            char[] confirm = input.readPassword("Confirm password: ");

            if (confirm == null) {
                wipe(password);
                return null;
            }

            boolean same = Arrays.equals(password, confirm);
            wipe(confirm);

            if (!same) {
                System.out.println("Passwords do not match. Please try again.");
                wipe(password);
                continue;
            }

            return password;
        }
    }

    private void wipe(char[] data) {
        if (data != null) {
            Arrays.fill(data, '\0');
        }
    }
}
