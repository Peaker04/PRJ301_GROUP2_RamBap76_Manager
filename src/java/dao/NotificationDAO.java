package dao;

import connect.DBConnection;
import model.Notification;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    
    public boolean createNotification(Notification notification) {
        String sql = "INSERT INTO notifications (shipper_id, message, type) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, notification.getShipperId());
            ps.setString(2, notification.getMessage());
            ps.setString(3, notification.getType());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        notification.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Notification> getNotificationsByShipper(int shipperId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT n.*, u.full_name as shipper_name
            FROM notifications n
            JOIN users u ON n.shipper_id = u.id
            WHERE n.shipper_id = ?
            ORDER BY n.created_at DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    public List<Notification> getUnreadNotificationsByShipper(int shipperId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = """
            SELECT n.*, u.full_name as shipper_name
            FROM notifications n
            JOIN users u ON n.shipper_id = u.id
            WHERE n.shipper_id = ? AND n.is_read = 0
            ORDER BY n.created_at DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification notification = mapResultSetToNotification(rs);
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
    
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = 1 WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markAllAsRead(int shipperId) {
        String sql = "UPDATE notifications SET is_read = 1 WHERE shipper_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getUnreadCount(int shipperId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE shipper_id = ? AND is_read = 0";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM notifications WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, notificationId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setShipperId(rs.getInt("shipper_id"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        notification.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        notification.setRead(rs.getBoolean("is_read"));
        notification.setShipperName(rs.getString("shipper_name"));
        
        return notification;
    }
} 