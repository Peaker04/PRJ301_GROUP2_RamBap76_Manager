package dao;

import model.CustomerFeedback;
import connect.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFeedbackDAO {

    // Thêm đánh giá mới từ shipper
    public boolean addFeedback(CustomerFeedback feedback) {
        String sql = "INSERT INTO customer_feedbacks (customer_id, shipper_id, order_id, notes, rating, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, feedback.getCustomerId());
            ps.setInt(2, feedback.getShipperId());
            ps.setInt(3, feedback.getOrderId());
            ps.setString(4, feedback.getNotes());
            ps.setInt(5, feedback.getRating());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả đánh giá của một khách hàng cụ thể
    public List<CustomerFeedback> getFeedbackByCustomerId(int customerId) {
        List<CustomerFeedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM customer_feedbacks WHERE customer_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerFeedback fb = extractFeedbackFromResultSet(rs);
                    feedbacks.add(fb);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbacks;
    }

    // Lấy tất cả đánh giá (admin dùng)
    public List<CustomerFeedback> getAllFeedbacks() {
        List<CustomerFeedback> feedbacks = new ArrayList<>();
        String sql = "SELECT * FROM customer_feedbacks ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CustomerFeedback fb = extractFeedbackFromResultSet(rs);
                feedbacks.add(fb);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbacks;
    }

    // Helper - Chuyển ResultSet thành CustomerFeedback
    private CustomerFeedback extractFeedbackFromResultSet(ResultSet rs) throws SQLException {
        CustomerFeedback fb = new CustomerFeedback();
        fb.setId(rs.getInt("id"));
        fb.setCustomerId(rs.getInt("customer_id"));
        fb.setShipperId(rs.getInt("shipper_id"));
        fb.setOrderId(rs.getInt("order_id"));
        fb.setNotes(rs.getString("notes"));
        fb.setRating(rs.getInt("rating"));
        fb.setCreatedAt(rs.getTimestamp("created_at"));
        return fb;
    }
}

