package dao;

import connect.DBConnection;
import model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Notification> getUnreadNotifications(int shipperId) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE shipper_id = ? AND is_read = 0 ORDER BY created_at DESC";
        List<Notification> notifications = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, shipperId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setId(rs.getInt("id"));
                    n.setShipperId(rs.getInt("shipper_id"));
                    n.setMessage(rs.getString("message"));
                    n.setType(rs.getString("type"));
                    n.setCreatedAt(rs.getTimestamp("created_at"));
                    n.setRead(rs.getBoolean("is_read"));
                    notifications.add(n);
                }
            }
        }
        return notifications;
    }
    
    public boolean markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = 1 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, notificationId);
            return stmt.executeUpdate() > 0;
        }
    }
}