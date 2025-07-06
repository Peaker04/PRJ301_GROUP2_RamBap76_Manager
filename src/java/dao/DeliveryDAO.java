package dao;

import connect.DBConnection;
import model.Delivery;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class DeliveryDAO {
    
    public List<Delivery> getDeliveriesByShipperAndStatus(int shipperId, String status) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = """
            SELECT d.*, c.name as customer_name, c.phone as customer_phone, 
                   c.address as customer_address, u.full_name as shipper_name, o.status as order_status
            FROM deliveries d
            JOIN orders o ON d.order_id = o.id
            JOIN customers c ON o.customer_id = c.id
            JOIN users u ON d.current_shipper_id = u.id
            WHERE d.current_shipper_id = ? AND d.status = ?
            ORDER BY d.assigned_time DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            ps.setString(2, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Delivery delivery = mapResultSetToDelivery(rs);
                    deliveries.add(delivery);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    
    public Delivery getDeliveryById(int deliveryId) {
        String sql = """
            SELECT d.*, c.name as customer_name, c.phone as customer_phone, 
                   c.address as customer_address, u.full_name as shipper_name, o.status as order_status
            FROM deliveries d
            JOIN orders o ON d.order_id = o.id
            JOIN customers c ON o.customer_id = c.id
            JOIN users u ON d.current_shipper_id = u.id
            WHERE d.id = ?
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, deliveryId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDelivery(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateDeliveryStatus(int deliveryId, String status, LocalDateTime acceptedTime) {
        String sql = "UPDATE deliveries SET status = ?, accepted_time = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setTimestamp(2, acceptedTime != null ? Timestamp.valueOf(acceptedTime) : null);
            ps.setInt(3, deliveryId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean completeDelivery(int deliveryId, LocalDateTime actualDeliveryTime, 
                                   BigDecimal collectedAmount, int completedShipperId) {
        String sql = """
            UPDATE deliveries 
            SET status = 'COMPLETED', actual_delivery_time = ?, 
                collected_amount = ?, completed_shipper_id = ?
            WHERE id = ?
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.valueOf(actualDeliveryTime));
            ps.setBigDecimal(2, collectedAmount);
            ps.setInt(3, completedShipperId);
            ps.setInt(4, deliveryId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, orderId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateCurrentShipper(int deliveryId, int newShipperId) {
        String sql = "UPDATE deliveries SET current_shipper_id = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, newShipperId);
            ps.setInt(2, deliveryId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public BigDecimal getDailyIncome(int shipperId) {
        String sql = "SELECT daily_income FROM shippers WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("daily_income");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    public boolean updateDailyIncome(int shipperId, BigDecimal newIncome) {
        String sql = "UPDATE shippers SET daily_income = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setBigDecimal(1, newIncome);
            ps.setInt(2, shipperId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Delivery mapResultSetToDelivery(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setId(rs.getInt("id"));
        delivery.setOrderId(rs.getInt("order_id"));
        delivery.setInitialShipperId(rs.getInt("initial_shipper_id"));
        delivery.setCurrentShipperId(rs.getInt("current_shipper_id"));
        
        int completedShipperId = rs.getInt("completed_shipper_id");
        delivery.setCompletedShipperId(rs.wasNull() ? null : completedShipperId);
        
        delivery.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
        delivery.setBoxFee(rs.getBigDecimal("box_fee"));
        delivery.setCollectedAmount(rs.getBigDecimal("collected_amount"));
        delivery.setStatus(rs.getString("status"));
        
        Timestamp actualDeliveryTime = rs.getTimestamp("actual_delivery_time");
        delivery.setActualDeliveryTime(actualDeliveryTime != null ? actualDeliveryTime.toLocalDateTime() : null);
        
        Timestamp assignedTime = rs.getTimestamp("assigned_time");
        delivery.setAssignedTime(assignedTime != null ? assignedTime.toLocalDateTime() : null);
        
        Timestamp acceptedTime = rs.getTimestamp("accepted_time");
        delivery.setAcceptedTime(acceptedTime != null ? acceptedTime.toLocalDateTime() : null);
        
        delivery.setNotes(rs.getString("notes"));
        delivery.setPriorityLevel(rs.getInt("priority_level"));
        
        Timestamp estimatedDeliveryTime = rs.getTimestamp("estimated_delivery_time");
        delivery.setEstimatedDeliveryTime(estimatedDeliveryTime != null ? estimatedDeliveryTime.toLocalDateTime() : null);
        
        int stationReceiptId = rs.getInt("station_receipt_id");
        delivery.setStationReceiptId(rs.wasNull() ? null : stationReceiptId);
        
        // Additional display fields
        delivery.setCustomerName(rs.getString("customer_name"));
        delivery.setCustomerPhone(rs.getString("customer_phone"));
        delivery.setCustomerAddress(rs.getString("customer_address"));
        delivery.setShipperName(rs.getString("shipper_name"));
        delivery.setOrderStatus(rs.getString("order_status"));
        
        return delivery;
    }
} 