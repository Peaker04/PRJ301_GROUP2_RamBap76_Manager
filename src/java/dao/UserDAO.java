package dao;

import connect.DBConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

public class UserDAO extends DBConnection {

    public User login(String username, String plainPassword) {
        String sql = "SELECT * FROM users WHERE username = ? AND enabled = 1";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("password");

                    // ================== CODE DEBUG BAT DAU ==================
                    System.out.println("User '" + username + "' ton tai trong DB.");
                    System.out.println("Hashed password tu DB: " + hashedPasswordFromDB);

                    boolean passwordsMatch = BCrypt.checkpw(plainPassword, hashedPasswordFromDB);

                    System.out.println("Ket qua so sanh BCrypt.checkpw(): " + passwordsMatch);
                    System.out.println("--- KET THUC DEBUG LOGIN ---");
                    // ================== CODE DEBUG KẾT THÚC ==================

                    if (passwordsMatch) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(hashedPasswordFromDB);
                        user.setFullName(rs.getString("full_name"));
                        user.setRole(rs.getString("role"));
                        user.setEnabled(rs.getBoolean("enabled"));
                        return user;
                    }
                } else {
                    // ================== CODE DEBUG BAT DAU ==================
                    System.out.println("Khong tim thay user '" + username + "' hoac tai khoan bi khoa.");
                    System.out.println("--- KET THUC DEBUG LOGIN ---");
                    // ================== CODE DEBUG KẾT THÚC ==================
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveResetToken(String username, UUID token, Date expiry) throws SQLException {
        String sql = "UPDATE users SET reset_token = ?, token_expiry = ? WHERE username = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token.toString());  // Chuyển UUID thành String trước khi lưu
            ps.setTimestamp(2, new Timestamp(expiry.getTime()));
            ps.setString(3, username);
            ps.executeUpdate();
        }
    }

    public User findUserByResetToken(UUID token) {
        String sql = "SELECT * FROM users WHERE reset_token = ? AND token_expiry > GETDATE()";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, token); // Chuyển UUID thành chuẩn cho câu lệnh SQL
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setTokenExpiry(rs.getTimestamp("token_expiry"));

                    // Kiểm tra và set reset_token
                    String resetTokenStr = rs.getString("reset_token");
                    if (resetTokenStr != null && !resetTokenStr.isEmpty()) {
                        user.setResetToken(UUID.fromString(resetTokenStr)); // Chuyển đổi chuỗi thành UUID
                    }

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePasswordById(int userId, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

public void clearResetToken(int userId) {
    String sql = "UPDATE users SET reset_token = NULL, token_expiry = NULL WHERE id = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public User findUserByUsername(String username) {
    String sql = "SELECT * FROM users WHERE username = ? AND enabled = 1";  // Kiểm tra người dùng còn hoạt động

    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));  // Đảm bảo bạn có email trong bảng users
                user.setFullName(rs.getString("full_name"));
                user.setRole(rs.getString("role"));
                user.setEnabled(rs.getBoolean("enabled"));
                
                // Lấy reset_token từ ResultSet và chuyển đổi từ String sang UUID
                String tokenStr = rs.getString("reset_token");
                if (tokenStr != null && !tokenStr.isEmpty()) {
                    user.setResetToken(UUID.fromString(tokenStr)); // Chuyển String thành UUID
                }

                // Lấy token_expiry
                user.setTokenExpiry(rs.getTimestamp("token_expiry"));

                return user;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;  // Nếu không tìm thấy người dùng
}

}
