package utils; // Tạo một package mới cho các lớp tiện ích

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig { // Đặt tên là AppConfig cho dễ hiểu hơn là ConfigReader

    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    // Static initializer block để load properties một lần duy nhất khi class được tải
    static {
        properties = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Error: Unable to find " + CONFIG_FILE + ". Make sure it's in your project's 'resources' folder and Ant is configured to copy it.");
                // Hoặc throw new RuntimeException("Config file not found: " + CONFIG_FILE);
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error loading config.properties: " + ex.getMessage());
        }
    }

    private AppConfig() {
        // Private constructor để ngăn việc tạo đối tượng
    }

    public static String getProperty(String key) {
        if (properties == null) {
            // Điều này chỉ xảy ra nếu có lỗi trong static block
            System.err.println("Properties object is null. Configuration might not have loaded.");
            return null;
        }
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            System.err.println("Warning: Configuration key '" + key + "' not found or is empty in " + CONFIG_FILE);
        }
        return value;
    }

    // Các phương thức tiện ích để lấy các giá trị cụ thể
    public static String getGoogleClientId() {
        return getProperty("google.client.id");
    }

    public static String getGoogleClientSecret() {
        return getProperty("google.client.secret");
    }

    public static String getGoogleRedirectUri() {
        return getProperty("google.redirect.uri");
    }
}