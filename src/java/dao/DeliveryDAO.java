package dao;

import connect.DBConnection;
import java.math.BigDecimal;
import model.Delivery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Order;
import model.StationReceipt;

public class DeliveryDAO {

   public List<Delivery> getDeliveriesByShipper(int shipperId, String status) throws SQLException {
    String sql = "SELECT d.*, c.name AS customer_name, c.address AS customer_address "
               + "FROM deliveries d "
               + "JOIN orders o ON d.order_id = o.id "
               + "JOIN customers c ON o.customer_id = c.id "
               + "WHERE d.current_shipper_id = ? AND d.status = ? "
               + "ORDER BY d.priority_level DESC, d.assigned_time ASC";

    List<Delivery> deliveries = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection(); 
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, shipperId);
        stmt.setString(2, status);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                Delivery delivery = new Delivery();
                delivery.setId(rs.getInt("id"));
                delivery.setOrderId(rs.getInt("order_id"));
                delivery.setInitialShipperId(rs.getInt("initial_shipper_id"));
                delivery.setCurrentShipperId(rs.getInt("current_shipper_id"));
                delivery.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
                delivery.setBoxFee(rs.getBigDecimal("box_fee"));
                delivery.setCollectedAmount(rs.getDouble("collected_amount"));
                delivery.setStatus(rs.getString("status"));
                delivery.setActualDeliveryTime(rs.getTimestamp("actual_delivery_time"));
                delivery.setAssignedTime(rs.getTimestamp("assigned_time"));
                delivery.setAcceptedTime(rs.getTimestamp("accepted_time"));
                delivery.setNotes(rs.getString("notes"));
                delivery.setPriorityLevel(rs.getInt("priority_level"));
                
                // Thêm thông tin khách hàng để hiển thị trên giao diện
                customer.setName(rs.getString("customer_name"));
                customer.setAddress(rs.getString("customer_address"));

                deliveries.add(delivery);
            }
        }
    }
    return deliveries;
}

    public boolean updateDeliveryStatus(int deliveryId, String status, int shipperId) throws SQLException {
        String sql = "UPDATE deliveries SET status = ?, current_shipper_id = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, shipperId);
            stmt.setInt(3, deliveryId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean acceptDelivery(int deliveryId, int shipperId) throws SQLException {
        String sql = "UPDATE deliveries SET status = 'IN_TRANSIT', accepted_time = GETDATE() "
                + "WHERE id = ? AND current_shipper_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);
            stmt.setInt(2, shipperId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean completeDelivery(int deliveryId, double collectedAmount) throws SQLException {
        String sql = "UPDATE deliveries SET status = 'COMPLETED', actual_delivery_time = ?, collected_amount = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setDouble(2, collectedAmount);
            stmt.setInt(3, deliveryId);

            return stmt.executeUpdate() > 0;
        }
    }

    public Delivery getDeliveryById(int deliveryId) throws SQLException {
        String sql = "SELECT * FROM deliveries WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapFullDelivery(rs);
                }
            }
        }
        return null;
    }

    public boolean updateAcceptedTime(int deliveryId, Timestamp acceptedTime) throws SQLException {
        String sql = "UPDATE deliveries SET accepted_time = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, acceptedTime);
            stmt.setInt(2, deliveryId);

            return stmt.executeUpdate() > 0;
        }
    }

    public List<Delivery> getAssignedDeliveries(int shipperId) throws SQLException {
        String sql = "SELECT "
                + "d.id, d.order_id, d.initial_shipper_id, d.current_shipper_id, "
                + "d.completed_shipper_id, d.delivery_fee, d.box_fee, d.collected_amount, "
                + "d.status, d.actual_delivery_time, d.assigned_time, d.accepted_time, "
                + "d.notes, d.priority_level, d.estimated_delivery_time, d.station_receipt_id, "
                + "o.customer_id, o.status AS order_status, o.notes AS order_notes, "
                + "o.order_date, o.appointment_time, o.priority_delivery_date, "
                + "c.name AS customer_name, c.phone, c.address, c.latitude, c.longitude, "
                + "sr.station_name, sr.verified_at "
                + "FROM deliveries d "
                + "JOIN orders o ON d.order_id = o.id "
                + "JOIN customers c ON o.customer_id = c.id "
                + "LEFT JOIN station_receipts sr ON d.station_receipt_id = sr.id "
                + "WHERE d.current_shipper_id = ? AND d.status = 'ASSIGNED'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);
            ResultSet rs = stmt.executeQuery();

            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                Delivery delivery = mapFullDelivery(rs);
                deliveries.add(delivery);
            }
            return deliveries;
        }
    }

    public Delivery mapDeliveryBasic(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();

        delivery.setId(rs.getInt("id"));
        delivery.setOrderId(rs.getInt("order_id"));
        delivery.setInitialShipperId(rs.getInt("initial_shipper_id"));
        delivery.setCurrentShipperId(rs.getInt("current_shipper_id"));
        delivery.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
        delivery.setBoxFee(rs.getBigDecimal("box_fee"));

        double collectedAmount = rs.getDouble("collected_amount");
        if (rs.wasNull()) {
            delivery.setCollectedAmount(null);
        } else {
            delivery.setCollectedAmount(collectedAmount);
        }

        delivery.setStatus(rs.getString("status"));
        delivery.setActualDeliveryTime(rs.getTimestamp("actual_delivery_time"));
        delivery.setAssignedTime(rs.getTimestamp("assigned_time"));
        delivery.setAcceptedTime(rs.getTimestamp("accepted_time"));
        delivery.setNotes(rs.getString("notes"));
        delivery.setPriorityLevel(rs.getInt("priority_level"));

        return delivery;
    }

    public Order mapOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setStatus(rs.getString("order_status"));
        order.setOrderDate(rs.getDate("order_date"));
        order.setAppointmentTime(rs.getTimestamp("appointment_time"));
        order.setNotes(rs.getString("order_notes"));
        order.setPriorityDeliveryDate(rs.getDate("priority_delivery_date"));
        return order;
    }

    public StationReceipt mapStationReceiptBasic(ResultSet rs) throws SQLException {
        int stationReceiptId = rs.getInt("station_receipt_id");
        if (rs.wasNull()) {
            return null;
        }

        StationReceipt receipt = new StationReceipt();
        receipt.setId(stationReceiptId);

        // Nếu câu SQL có thêm các cột này thì map luôn:
        receipt.setStationName(rs.getString("station_name"));
        receipt.setVerifiedAt(rs.getTimestamp("verified_at"));

        return receipt;
    }

    public Delivery mapFullDelivery(ResultSet rs) throws SQLException {
        Delivery delivery = mapDeliveryBasic(rs);
        Order order = mapOrderFromResultSet(rs);
        StationReceipt receipt = mapStationReceiptBasic(rs);

        delivery.setOrder(order);
        delivery.setStationReceipt(receipt);

        return delivery;
    }

}
