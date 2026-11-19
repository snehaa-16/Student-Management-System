import java.sql.SQLException;
import java.util.regex.Pattern;

public class AuthService {

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("[A-Za-z0-9_]{3,30}");

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 64;

    private final UserDao userDao = new UserDao();

    public String validateUsername(String username) {
        if (username == null
                || !USERNAME_PATTERN.matcher(username).matches()) {
            return "Username must be 3-30 characters: letters, digits and underscore only.";
        }
        return null;
    }

    public String validatePassword(char[] password, String username) {
        if (password == null
                || password.length < MIN_PASSWORD_LENGTH
                || password.length > MAX_PASSWORD_LENGTH) {
            return "Password must be 8-64 characters long.";
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password) {
            if (c < 32 || c > 126) {
                return "Password may only use standard keyboard characters (no accents or emojis).";
            }

            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                hasLetter = true;
            }

            if (c >= '0' && c <= '9') {
                hasDigit = true;
            }
        }

        if (!hasLetter || !hasDigit) {
            return "Password must contain at least one letter and one digit.";
        }

        if (sameIgnoringCase(password, username)) {
            return "Password must not be the same as the username.";
        }

        return null;
    }

    // True only when the users table has no accounts.
    public boolean noUsersExist() throws SQLException {
        return !userDao.hasAnyUser();
    }

    public RegistrationResult registerUser(String username, char[] password) {
        return create(username, password, false);
    }

    public RegistrationResult createFirstAdmin(String username, char[] password) {
        return create(username, password, true);
    }

    private RegistrationResult create(
            String username, char[] password, boolean admin) {

        if (validateUsername(username) != null) {
            return RegistrationResult.INVALID_USERNAME;
        }

        if (validatePassword(password, username) != null) {
            return RegistrationResult.INVALID_PASSWORD;
        }

        try {
            String hash = PasswordUtil.hash(password);

            if (admin) {
                return userDao.createFirstAdmin(username, hash);
            }

            userDao.registerUser(username, hash);
            return RegistrationResult.SUCCESS;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return RegistrationResult.USERNAME_TAKEN;
            }
            return RegistrationResult.SYSTEM_ERROR;

        } catch (RuntimeException e) {
            return RegistrationResult.SYSTEM_ERROR;
        }
    }

    private boolean sameIgnoringCase(char[] password, String text) {
        if (text == null || password.length != text.length()) {
            return false;
        }

        for (int i = 0; i < password.length; i++) {
            if (Character.toLowerCase(password[i])
                    != Character.toLowerCase(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
