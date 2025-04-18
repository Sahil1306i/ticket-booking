package ticket_booking.util;

import org.mindrot.jbcrypt.BCrypt;

// something related to stored hashed passwords

public class UserServiceUtil {
    public static String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    public static boolean checkPassword(String plainPassword, String hashedPassword){
        return BCrypt.checkpw(plainPassword,hashedPassword);

    }
}
