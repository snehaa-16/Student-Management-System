import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtil {

    private static final int BCRYPT_COST = 12;

    private PasswordUtil() {
    }

    public static String hash(char[] password) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password);
    }

    public static boolean verify(char[] password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password, storedHash);
        return result.verified;
    }
}
