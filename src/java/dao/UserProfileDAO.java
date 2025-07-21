package dao;

import connect.DBConnection; // Giả sử bạn có lớp này để kết nối DB
import model.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileDAO {

    public UserProfileDAO() {

    }

    // Lấy thông tin profile dựa trên user_id
    public static UserProfile getUserProfileByUserId(int userId) {
        UserProfile profile = null;
        String sql = "SELECT * FROM UserProfiles WHERE user_id = ?";
        // BƯỚC 3: Lấy kết nối trong từng phương thức sử dụng try-with-resources
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    profile = new UserProfile();
                    profile.setId(rs.getInt("id"));
                    profile.setUserId(rs.getInt("user_id"));
                    profile.setFirstName(rs.getString("first_name"));
                    profile.setLastName(rs.getString("last_name"));
                    profile.setEmail(rs.getString("email"));
                    profile.setGender(rs.getString("gender"));
                    profile.setPhoneNumber(rs.getString("phone_number"));
                    profile.setAddress(rs.getString("address"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profile;
    }

    // Cập nhật thông tin profile
    public boolean updateUserProfile(UserProfile profile) {
        String sql = "UPDATE UserProfiles SET first_name = ?, last_name = ?, email = ?, "
                + "gender = ?, phone_number = ?, address = ? WHERE user_id = ?";
        // BƯỚC 4: Lấy kết nối trong từng phương thức sử dụng try-with-resources
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getEmail());
            ps.setString(4, profile.getGender());
            ps.setString(5, profile.getPhoneNumber());
            ps.setString(6, profile.getAddress());
            ps.setInt(7, profile.getUserId());
            int rowsAffected = ps.executeUpdate(); // Lấy số hàng bị ảnh hưởng
            System.out.println("DEBUG DAO: Rows affected = " + rowsAffected);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("ERROR DAO: SQLException during update: " + e.getMessage());
            return false;
        }
    }
    
  
    public boolean insertUserProfile(UserProfile profile) {
        String sql = "INSERT INTO UserProfiles (user_id, first_name, last_name, email, gender, phone_number, address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getEmail());
            ps.setString(5, profile.getGender());
            ps.setString(6, profile.getPhoneNumber());
            ps.setString(7, profile.getAddress());
            System.out.println("DEBUG DAO: Inserting profile for user_id = " + profile.getUserId()); // Debug
            int rowsAffected = ps.executeUpdate();
            System.out.println("DEBUG DAO: Rows affected (Insert) = " + rowsAffected); // Debug
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // =======================================================================
}
