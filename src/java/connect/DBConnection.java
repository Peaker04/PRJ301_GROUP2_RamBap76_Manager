package connect;

import jakarta.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    // Phương thức này sẽ được gọi một lần khi ứng dụng khởi động bởi Listener
    public static void initialize(ServletContext context) {
        DB_URL = context.getInitParameter("dbUrl");
        DB_USER = context.getInitParameter("dbUser");
        DB_PASSWORD = context.getInitParameter("dbPassword");
        DB_DRIVER = context.getInitParameter("dbDriver");

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            // Xử lý lỗi nghiêm trọng: không tìm thấy driver
            e.printStackTrace();
            throw new RuntimeException("Failed to load database driver.", e);
        }
    }

 public static Connection getConnection() throws SQLException {
        if (DB_URL == null || DB_USER == null || DB_PASSWORD == null) {
            throw new SQLException("Database configuration is not initialized. Check AppContextListener.");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
