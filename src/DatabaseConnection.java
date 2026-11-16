import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String ENV_URL = "DB_URL";
    private static final String ENV_USER = "DB_USER";
    private static final String ENV_PASSWORD = "DB_PASSWORD";

    private static String getRequiredEnv(String name) throws SQLException {

        String value = System.getenv(name);

        if (value == null || value.trim().isEmpty()) {
            throw new SQLException(
                    "Missing environment variable: " + name
                    + ". Please set DB_URL, DB_USER and DB_PASSWORD "
                    + "(see README.md for setup steps)."
            );
        }

        return value;
    }

    public static Connection getConnection() throws SQLException {

        String url = getRequiredEnv(ENV_URL);
        String user = getRequiredEnv(ENV_USER);
        String password = getRequiredEnv(ENV_PASSWORD);

        try {
            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            throw new SQLException(
                    "Could not connect to the database. Check that MySQL "
                    + "is running and that DB_URL, DB_USER and DB_PASSWORD "
                    + "are correct.",
                    e.getSQLState(),
                    e.getErrorCode()
            );
        }
    }
}
