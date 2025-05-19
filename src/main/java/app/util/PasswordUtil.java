package app.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // Bruges ved login
    public static boolean checkPassword(String plainPassword, String hashedPasswordFromDB) {
        return BCrypt.checkpw(plainPassword, hashedPasswordFromDB);
    }

    // Bruges ved oprettelse
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
}
