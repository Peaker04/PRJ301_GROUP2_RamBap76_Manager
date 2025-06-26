

package BCrypt;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptExample {
    public static void main(String[] args) {
        String plainPassword = "admin";  // Mật khẩu thuần
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        
        System.out.println("Mat khau đa ma hoa: " + hashedPassword);
    }
}
