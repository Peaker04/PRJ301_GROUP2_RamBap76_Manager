

package dao;

import model.Shipper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connect.DBConnection;

public class ShipperDAO {

    // Lấy danh sách tất cả shipper (JOIN với bảng users để lấy tên)
    public List<Shipper> getAllShippers() throws SQLException {
        List<Shipper> list = new ArrayList<>();
        String sql = "SELECT s.user_id, u.full_name, s.area, s.priority_level, s.daily_income " +
                     "FROM shippers s JOIN users u ON s.user_id = u.id " +
                     "WHERE u.role = 'SHIPPER'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Shipper shipper = new Shipper(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("area"),
                    rs.getInt("priority_level"),
                    rs.getDouble("daily_income")
                );
                list.add(shipper);
            }
        }
        return list;
    }

    // Tìm 1 shipper theo ID
    public Shipper getShipperById(int id) throws SQLException {
        String sql = "SELECT s.user_id, u.full_name, s.area, s.priority_level, s.daily_income " +
                     "FROM shippers s JOIN users u ON s.user_id = u.id " +
                     "WHERE s.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Shipper(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("area"),
                        rs.getInt("priority_level"),
                        rs.getDouble("daily_income")
                    );
                }
            }
        }
        return null;
    }

    // Cập nhật thông tin shipper
    public void updateShipper(Shipper s) throws SQLException {
        String sql = "UPDATE shippers SET area = ?, priority_level = ?, daily_income = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getArea());
            ps.setInt(2, s.getPriorityLevel());
            ps.setDouble(3, s.getDailyIncome());
            ps.setInt(4, s.getId());
            ps.executeUpdate();
        }
    }

    // Thêm shipper mới
    public void insertShipper(Shipper s) throws SQLException {
        String sql = "INSERT INTO shippers(user_id, area, priority_level, daily_income) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getId()); // user_id
            ps.setString(2, s.getArea());
            ps.setInt(3, s.getPriorityLevel());
            ps.setDouble(4, s.getDailyIncome());
            ps.executeUpdate();
        }
    }

    // Xóa shipper (nếu cần)
    public void deleteShipper(int id) throws SQLException {
        String sql = "DELETE FROM shippers WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
