package dao;

import connect.DBConnection;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

// BƯỚC 1: Bỏ "extends DBConnection"
public class UserDAO {

    public User login(String username, String plainPassword) {
        String sql = "SELECT * FROM users WHERE username = ? AND enabled = 1";

        // BƯỚC 2: Sửa lại tất cả các lệnh gọi getConnection() thành DBConnection.getConnection()
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("password");
                    boolean passwordsMatch = BCrypt.checkpw(plainPassword, hashedPasswordFromDB);

                    // Các dòng debug của bạn, có thể giữ lại để kiểm tra
                    System.out.println("User '" + username + "' tồn tại trong DB.");
                    System.out.println("Kết quả so sánh BCrypt.checkpw(): " + passwordsMatch);

                   

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
                    System.out.println("Không tìm thấy user '" + username + "' hoặc tài khoản bị khóa.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveResetToken(String username, UUID token, Date expiry) throws SQLException {
    String sql = "UPDATE users SET reset_token = ?, token_expiry = ? WHERE username = ?";
    // Sử dụng try-with-resources để đảm bảo kết nối được đóng đúng cách
    try (Connection conn = DBConnection.getConnection(); // Sử dụng lớp DBConnection để lấy kết nối
         PreparedStatement ps = conn.prepareStatement(sql)) {

        // Chuyển UUID thành String để lưu trữ trong cơ sở dữ liệu
        // Hầu hết các JDBC driver yêu cầu kiểu dữ liệu cụ thể, và String là lựa chọn an toàn cho UUID.
        ps.setString(1, token.toString());

        // Chuyển java.util.Date thành java.sql.Timestamp để tương thích với cột SQL DATETIME/TIMESTAMP
        ps.setTimestamp(2, new Timestamp(expiry.getTime()));
        ps.setString(3, username);
        ps.executeUpdate();
    }
}

    public User findUserByResetToken(UUID token) {
        String sql = "SELECT * FROM users WHERE reset_token = ? AND token_expiry > GETDATE()";
        try (Connection conn = DBConnection.getConnection(); // Đã sửa
                 PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    // Dòng này có thể gây lỗi nếu model User không có setTokenExpiry
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
        try (Connection conn = DBConnection.getConnection(); // Đã sửa
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
     return null;
    }
    
    // ==========================================================
    // CÁC PHƯƠNG THỨC PHÍA DƯỚI NÀY VỐN ĐÃ ĐÚNG CHUẨN
    // ==========================================================
    
    public boolean checkUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean createUser(User user, String hashedPassword) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Bước 1: Thêm vào bảng users
            String userSql = "INSERT INTO users (username, password, full_name, role) VALUES (?, ?, ?, ?)";
            int userId;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, user.getUsername());
                psUser.setString(2, hashedPassword);
                psUser.setString(3, user.getFullName());
                psUser.setString(4, user.getRole());
                psUser.executeUpdate();

                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // Bước 2: Nếu là SHIPPER, thêm vào bảng shippers
            if ("SHIPPER".equals(user.getRole())) {
                String shipperSql = "INSERT INTO shippers (user_id, area) VALUES (?, ?)";
                try (PreparedStatement psShipper = conn.prepareStatement(shipperSql)) {
                    psShipper.setInt(1, userId);
                    psShipper.setString(2, "Chưa phân công"); // Giá trị mặc định
                    psShipper.executeUpdate();
                }
            }

            conn.commit(); // Hoàn tất transaction thành công
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Hoàn tác tất cả thay đổi nếu có lỗi
            }
            throw e; // Ném lỗi ra để tầng Service xử lý
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
