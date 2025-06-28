package dao;

import connect.DBConnection;
import model.DeliveryTransfer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryTransferDAO {
    
    public boolean createTransfer(DeliveryTransfer transfer) {
        String sql = """
            INSERT INTO delivery_transfers (delivery_id, from_shipper_id, to_shipper_id, reason, status)
            VALUES (?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, transfer.getDeliveryId());
            ps.setInt(2, transfer.getFromShipperId());
            ps.setInt(3, transfer.getToShipperId());
            ps.setString(4, transfer.getReason());
            ps.setString(5, transfer.getStatus());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        transfer.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<DeliveryTransfer> getPendingTransfersForShipper(int shipperId) {
        List<DeliveryTransfer> transfers = new ArrayList<>();
        String sql = """
            SELECT dt.*, u1.full_name as from_shipper_name, u2.full_name as to_shipper_name,
                   CONCAT('Đơn hàng #', d.order_id, ' - ', c.name) as delivery_info
            FROM delivery_transfers dt
            JOIN users u1 ON dt.from_shipper_id = u1.id
            JOIN users u2 ON dt.to_shipper_id = u2.id
            JOIN deliveries d ON dt.delivery_id = d.id
            JOIN orders o ON d.order_id = o.id
            JOIN customers c ON o.customer_id = c.id
            WHERE dt.to_shipper_id = ? AND dt.status = 'PENDING'
            ORDER BY dt.request_time DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, shipperId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DeliveryTransfer transfer = mapResultSetToTransfer(rs);
                    transfers.add(transfer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transfers;
    }
    
    public DeliveryTransfer getTransferById(int transferId) {
        String sql = """
            SELECT dt.*, u1.full_name as from_shipper_name, u2.full_name as to_shipper_name,
                   CONCAT('Đơn hàng #', d.order_id, ' - ', c.name) as delivery_info
            FROM delivery_transfers dt
            JOIN users u1 ON dt.from_shipper_id = u1.id
            JOIN users u2 ON dt.to_shipper_id = u2.id
            JOIN deliveries d ON dt.delivery_id = d.id
            JOIN orders o ON d.order_id = o.id
            JOIN customers c ON o.customer_id = c.id
            WHERE dt.id = ?
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, transferId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransfer(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updateTransferStatus(int transferId, String status, LocalDateTime acceptedTime) {
        String sql = "UPDATE delivery_transfers SET status = ?, accepted_time = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setTimestamp(2, acceptedTime != null ? Timestamp.valueOf(acceptedTime) : null);
            ps.setInt(3, transferId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean expireTransfers() {
        String sql = """
            UPDATE delivery_transfers 
            SET status = 'EXPIRED' 
            WHERE status = 'PENDING' AND expiration_time < GETDATE()
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private DeliveryTransfer mapResultSetToTransfer(ResultSet rs) throws SQLException {
        DeliveryTransfer transfer = new DeliveryTransfer();
        transfer.setId(rs.getInt("id"));
        transfer.setDeliveryId(rs.getInt("delivery_id"));
        transfer.setFromShipperId(rs.getInt("from_shipper_id"));
        transfer.setToShipperId(rs.getInt("to_shipper_id"));
        
        Timestamp requestTime = rs.getTimestamp("request_time");
        transfer.setRequestTime(requestTime != null ? requestTime.toLocalDateTime() : null);
        
        Timestamp acceptedTime = rs.getTimestamp("accepted_time");
        transfer.setAcceptedTime(acceptedTime != null ? acceptedTime.toLocalDateTime() : null);
        
        Timestamp expirationTime = rs.getTimestamp("expiration_time");
        transfer.setExpirationTime(expirationTime != null ? expirationTime.toLocalDateTime() : null);
        
        transfer.setReason(rs.getString("reason"));
        transfer.setStatus(rs.getString("status"));
        
        // Additional display fields
        transfer.setFromShipperName(rs.getString("from_shipper_name"));
        transfer.setToShipperName(rs.getString("to_shipper_name"));
        transfer.setDeliveryInfo(rs.getString("delivery_info"));
        
        return transfer;
    }
} 