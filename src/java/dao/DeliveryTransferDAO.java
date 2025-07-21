package dao;

import connect.DBConnection;
import model.DeliveryTransfer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryTransferDAO {

    public boolean createTransfer(DeliveryTransfer transfer) throws SQLException {
        String sql = "INSERT INTO delivery_transfers (delivery_id, from_shipper_id, to_shipper_id, "
                   + "request_time, reason, status) VALUES (?, ?, ?, GETDATE(), ?, 'PENDING')";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, transfer.getDeliveryId());
            stmt.setInt(2, transfer.getFromShipperId());
            stmt.setInt(3, transfer.getToShipperId());
            stmt.setString(4, transfer.getReason());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public List<DeliveryTransfer> getPendingTransfersForShipper(int shipperId) throws SQLException {
        String sql = "SELECT * FROM delivery_transfers WHERE to_shipper_id = ? AND status = 'PENDING'";
        List<DeliveryTransfer> transfers = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, shipperId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DeliveryTransfer t = new DeliveryTransfer();
                    t.setId(rs.getInt("id"));
                    t.setDeliveryId(rs.getInt("delivery_id"));
                    t.setFromShipperId(rs.getInt("from_shipper_id"));
                    t.setToShipperId(rs.getInt("to_shipper_id"));
                    t.setRequestTime(rs.getTimestamp("request_time"));
                    t.setAcceptedTime(rs.getTimestamp("accepted_time"));
                    t.setReason(rs.getString("reason"));
                    t.setStatus(rs.getString("status"));
                    transfers.add(t);
                }
            }
        }
        return transfers;
    }
    
    public boolean acceptTransfer(int transferId) throws SQLException {
        String sql = "EXEC sp_AcceptTransfer @transferId = ?, @acceptedTime = GETDATE()";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, transferId);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean rejectTransfer(int transferId, String reason) throws SQLException {
        String sql = "UPDATE delivery_transfers SET status = 'REJECTED', reason = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, reason);
            stmt.setInt(2, transferId);
            
            return stmt.executeUpdate() > 0;
        }
    }
}