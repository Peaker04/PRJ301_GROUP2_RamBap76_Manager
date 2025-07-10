package dao;

import connect.DBConnection;
import static connect.DBConnection.getConnection;
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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
            conn = DBConnection.getConnection(); // Giả định DBConnection.getConnection() tồn tại
            conn.setAutoCommit(false);

            // SỬA: Thêm 'email' vào câu lệnh INSERT và placeholder tương ứng
            String userSql = "INSERT INTO users (username, password, full_name, role, email) VALUES (?, ?, ?, ?, ?)";
            int userId;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, user.getUsername());
                psUser.setString(2, hashedPassword);
                psUser.setString(3, user.getFullName());
                psUser.setString(4, user.getRole());
                psUser.setString(5, user.getEmail()); // SỬA: Đặt giá trị email từ đối tượng User
                psUser.executeUpdate();

                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // ... Phần logic xử lý SHIPPER nếu có ...
            if ("SHIPPER".equals(user.getRole())) {
                String shipperSql = "INSERT INTO shippers (user_id, area) VALUES (?, ?)";
                try (PreparedStatement psShipper = conn.prepareStatement(shipperSql)) {
                    psShipper.setInt(1, userId);
                    psShipper.setString(2, "Chưa phân công");
                    psShipper.executeUpdate();
                }
                
            }
            String userProfileSql = "INSERT INTO UserProfiles (user_id, first_name, last_name, email, gender, phone_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psUserProfile = conn.prepareStatement(userProfileSql)) {
                psUserProfile.setInt(1, userId);
                // Cố gắng tách full_name từ đối tượng user thành first_name và last_name
                String firstName = "";
                String lastName = "";
                String fullName = user.getFullName(); // Lấy full name từ đối tượng user
                if (fullName != null && !fullName.isEmpty()) {
                    int lastSpaceIndex = fullName.lastIndexOf(' ');
                    if (lastSpaceIndex != -1 && lastSpaceIndex != fullName.length() - 1) { // Đảm bảo không phải khoảng trắng cuối cùng
                        firstName = fullName.substring(0, lastSpaceIndex);
                        lastName = fullName.substring(lastSpaceIndex + 1);
                    } else {
                        firstName = fullName; // Không có khoảng trắng, coi toàn bộ là first name
                    }
                }

                psUserProfile.setString(2, firstName);
                psUserProfile.setString(3, lastName);
                psUserProfile.setString(4, user.getEmail()); // Lấy từ email của đối tượng user
                psUserProfile.setString(5, "N/A"); // Giá trị mặc định cho gender
                psUserProfile.setString(6, ""); // Giá trị mặc định cho phone_number
                psUserProfile.setString(7, ""); // Giá trị mặc định cho address
                psUserProfile.executeUpdate();
                
                
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Tìm một người dùng dựa trên địa chỉ email.
     *
     * @param email Email cần tìm kiếm.
     * @return Đối tượng User nếu tìm thấy, ngược lại trả về null.
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ? AND enabled = 1";
        User user = null;
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setFullName(rs.getString("full_name"));
                    user.setRole(rs.getString("role"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Tạo người dùng mới từ thông tin đăng nhập Google. Phương thức này xử lý
     * giao dịch tạo người dùng và một bản ghi shipper liên quan. Một mật khẩu
     * ngẫu nhiên không thể sử dụng sẽ được tạo vì người dùng xác thực qua
     * Google.
     *
     * @param email Email của người dùng.
     * @param fullName Tên đầy đủ của người dùng.
     * @return Đối tượng User vừa được tạo, hoặc null nếu thất bại.
     */
    public User createGoogleUser(String email, String fullName) throws SQLException {
        Connection conn = null;
        User newUser = null;
        // Đối với người dùng từ Google, có thể dùng email làm username để đảm bảo tính duy nhất
        String username = email;
        // Vai trò mặc định cho người dùng mới đăng ký qua Google
        String defaultRole = "SHIPPER";
        // Tạo một mật khẩu ngẫu nhiên, an toàn vì cột trong CSDL là NOT NULL
        String randomPassword = java.util.UUID.randomUUID().toString();
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(randomPassword, org.mindrot.jbcrypt.BCrypt.gensalt());

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            String userSql = "INSERT INTO users (username, password, full_name, role, email, enabled) VALUES (?, ?, ?, ?, ?, 1)";
            int userId;
            try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, username);
                psUser.setString(2, hashedPassword);
                psUser.setString(3, fullName);
                psUser.setString(4, defaultRole);
                psUser.setString(5, email);
                psUser.executeUpdate();

                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Tạo người dùng thất bại, không lấy được ID.");
                    }
                }
            }

            // Nếu vai trò mặc định là SHIPPER, tạo một bản ghi tương ứng trong bảng shippers
            if ("SHIPPER".equals(defaultRole)) {
                String shipperSql = "INSERT INTO shippers (user_id, area) VALUES (?, ?)";
                try (PreparedStatement psShipper = conn.prepareStatement(shipperSql)) {
                    psShipper.setInt(1, userId);
                    psShipper.setString(2, "Chưa phân công"); // Giá trị mặc định
                    psShipper.executeUpdate();
                }
            }
             String userProfileSql = "INSERT INTO UserProfiles (user_id, first_name, last_name, email, gender, phone_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psUserProfile = conn.prepareStatement(userProfileSql)) {
                psUserProfile.setInt(1, userId);
                // Cố gắng tách full_name thành first_name và last_name
                String firstName = "";
                String lastName = "";
                if (fullName != null && !fullName.isEmpty()) {
                    int lastSpaceIndex = fullName.lastIndexOf(' ');
                    if (lastSpaceIndex != -1 && lastSpaceIndex != fullName.length() - 1) { // Đảm bảo không phải khoảng trắng cuối cùng
                        firstName = fullName.substring(0, lastSpaceIndex);
                        lastName = fullName.substring(lastSpaceIndex + 1);
                    } else {
                        firstName = fullName; // Không có khoảng trắng, coi toàn bộ là first name
                    }
                }

                psUserProfile.setString(2, firstName);
                psUserProfile.setString(3, lastName);
                psUserProfile.setString(4, email); // Lấy từ email của người dùng Google
                psUserProfile.setString(5, "N/A"); // Giá trị mặc định cho gender
                psUserProfile.setString(6, ""); // Giá trị mặc định cho phone_number
                psUserProfile.setString(7, ""); // Giá trị mặc định cho address
                psUserProfile.executeUpdate();
            }

            conn.commit(); // Hoàn tất transaction

            // Điền thông tin vào đối tượng User để trả về cho servlet
            newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(username);
            newUser.setFullName(fullName);
            newUser.setRole(defaultRole);
            newUser.setEmail(email);
            newUser.setEnabled(true);

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Hoàn tác nếu có lỗi
            }
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để servlet xử lý
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return newUser;
    }
}
