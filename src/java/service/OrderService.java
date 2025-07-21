package service;

import connect.DBConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.Order;

public class OrderService {
    // OrderService.java
        public void createOrder(Order order) throws SQLException {
            String sql = "INSERT INTO orders (customer_id, status, notes) VALUES (?, ?, ?)";

            try (java.sql.Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, order.getCustomerId());
                stmt.setString(2, order.getStatus()); // 'NEW', 'URGENT', 'APPOINTMENT'
                stmt.setString(3, order.getNotes());
                stmt.executeUpdate();

            }
        }
    
}
