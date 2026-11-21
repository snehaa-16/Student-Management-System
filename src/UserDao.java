import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static final String FIRST_ADMIN_LOCK = "student_mgmt_first_admin";
    private static final int LOCK_TIMEOUT_SECONDS = 5;

    // Checks whether the users table contains ANY account.
    // Checks whether the users table contains ANY account.
public boolean hasAnyUser() throws SQLException {
    try (Connection connection = DatabaseConnection.getConnection()) {
        return hasAnyUser(connection);
    }
}

// Checks using the supplied connection.
private boolean hasAnyUser(Connection connection) throws SQLException {
    String sql = "SELECT 1 FROM users LIMIT 1";

    try (PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet result = statement.executeQuery()) {
        return result.next();
    }
}

        
public User findByUsername(String username) throws SQLException {
    String sql = "SELECT username, password_hash, role FROM users WHERE username = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, username);

        try (ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return new User(
                    result.getString("username"),
                    result.getString("password_hash"),
                    Role.valueOf(result.getString("role"))
                );
            }
        }
    }

    return null;
}

    // Normal registration always creates a USER.
    public void registerUser(String username, String passwordHash)
            throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            insertUser(connection, username, passwordHash, Role.USER);
        }
    }

    // Creates the first ADMIN only when the users table is completely empty.
    public RegistrationResult createFirstAdmin(
            String username, String passwordHash) throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (!acquireLock(connection)) {
                return RegistrationResult.BUSY;
            }

            try {
                // Check again while holding the lock.
                if (hasAnyUser(connection)) {
                    return RegistrationResult.ALREADY_INITIALIZED;
                }

                insertUser(connection, username, passwordHash, Role.ADMIN);
                return RegistrationResult.SUCCESS;

            } finally {
                releaseLock(connection);
            }
        }
    }

    private void insertUser(Connection connection, String username,
                            String passwordHash, Role role)
            throws SQLException {

        String sql = "INSERT INTO users (username, password_hash, role) "
                   + "VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            statement.setString(3, role.name());
            statement.executeUpdate();
        }
    }

    private boolean acquireLock(Connection connection) throws SQLException {
        String sql = "SELECT GET_LOCK(?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, FIRST_ADMIN_LOCK);
            statement.setInt(2, LOCK_TIMEOUT_SECONDS);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SQLException("GET_LOCK returned no result");
                }

                int value = result.getInt(1);

                if (result.wasNull()) {
                    throw new SQLException("GET_LOCK failed");
                }

                return value == 1;
            }
        }
    }

    private void releaseLock(Connection connection) {
        String sql = "SELECT RELEASE_LOCK(?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, FIRST_ADMIN_LOCK);

            try (ResultSet result = statement.executeQuery()) {
                // Reading the result completes the release query.
                if (result.next()) {
                    result.getObject(1);
                }
            }

        } catch (SQLException e) {
            // Closing the connection also releases the MySQL named lock.
        }
    }
}
