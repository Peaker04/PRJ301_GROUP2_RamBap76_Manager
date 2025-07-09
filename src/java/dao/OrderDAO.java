package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Customer;
import model.Order;
import model.OrderDetail;

public class OrderDAO {
    
    private Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Order> getOrders(String status, String search, String sort) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name FROM orders o JOIN customers c ON o.customer_id = c.id WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql += " AND o.status = ?";
            params.add(status);
        }
        if (search != null && !search.isEmpty()) {
            sql += " AND (CAST(o.id AS NVARCHAR) LIKE ? OR c.name LIKE ? OR o.notes LIKE ?)";
            String q = "%" + search + "%";
            params.add(q);
            params.add(q);
            params.add(q);
        }
        if (sort == null || (!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc"))) {
            sort = "desc";
        }
        sql += " ORDER BY o.order_date " + sort.toUpperCase();
        
        PreparedStatement ps = conn.prepareStatement(sql);

        // Set params động
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setStatus(rs.getString("status"));
            o.setOrderDate(rs.getTimestamp("order_date"));
            o.setAppointmentTime(rs.getTimestamp("appointment_time"));
            o.setPriorityDeliveryDate(rs.getDate("priority_delivery_date"));
            o.setNotes(rs.getString("notes"));
            // Gán customer
            Customer c = new Customer();
            c.setId(rs.getInt("customer_id"));
            c.setName(rs.getString("customer_name"));
            o.setCustomer(c);
            list.add(o);
        }
        return list;
    }
    
    public void createOrder(int customerId, String status, Timestamp appointmentTime, String notes, Date priorityDeliveryDate,
        List<Integer> productIds, List<Integer> quantities) throws SQLException {
        String sqlOrder = "INSERT INTO orders (customer_id, status, appointment_time, notes, priority_delivery_date) VALUES (?, ?, ?, ?, ?)";
        String sqlOrderDetail = "INSERT INTO order_details (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (
            PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psDetail = conn.prepareStatement(sqlOrderDetail)
        ) {
            psOrder.setInt(1, customerId);
            psOrder.setString(2, status);
            if (appointmentTime != null)
                psOrder.setTimestamp(3, appointmentTime);
            else
                psOrder.setNull(3, Types.TIMESTAMP);
            psOrder.setString(4, notes);
            if (priorityDeliveryDate != null)
                psOrder.setDate(5, priorityDeliveryDate);
            else
                psOrder.setNull(5, Types.DATE);
            psOrder.executeUpdate();
            ResultSet rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);
                for (int i = 0; i < productIds.size(); i++) {
                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, productIds.get(i));
                    psDetail.setInt(3, quantities.get(i));
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }
        }
    }
    
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT o.*, c.name as customer_name, c.phone, c.address FROM orders o JOIN customers c ON o.customer_id = c.id WHERE o.id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Order o = new Order();
            o.setId(orderId);
            o.setStatus(rs.getString("status"));
            o.setOrderDate(rs.getTimestamp("order_date"));
            o.setAppointmentTime(rs.getTimestamp("appointment_time"));
            o.setPriorityDeliveryDate(rs.getDate("priority_delivery_date"));
            o.setNotes(rs.getString("notes"));
            Customer c = new Customer();
            c.setId(rs.getInt("customer_id"));
            c.setName(rs.getString("customer_name"));
            c.setPhone(rs.getString("phone"));
            c.setAddress(rs.getString("address"));
            o.setCustomer(c);
            return o;
        }
        return null;
    }
    
    public List<OrderDetail> getOrderDetails(int orderId) throws SQLException {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT od.*, p.name FROM order_details od JOIN products p ON od.product_id = p.id WHERE od.order_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            OrderDetail d = new OrderDetail();
            d.setOrderId(orderId);
            d.setProductId(rs.getInt("product_id"));
            d.setProductName(rs.getString("name"));
            d.setQuantity(rs.getInt("quantity"));
            details.add(d);
        }
        return details;
    }
    
    public void updateOrder(int id, int customerId, String status, java.sql.Timestamp appointmentTime, String notes, java.sql.Date priorityDeliveryDate) throws SQLException {
        String sql = "UPDATE orders SET customer_id=?, status=?, appointment_time=?, notes=?, priority_delivery_date=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.setString(2, status);
        if (appointmentTime != null)
            ps.setTimestamp(3, appointmentTime);
        else
            ps.setNull(3, Types.TIMESTAMP);
        ps.setString(4, notes);
        if (priorityDeliveryDate != null)
            ps.setDate(5, priorityDeliveryDate);
        else
            ps.setNull(5, Types.DATE);
        ps.setInt(6, id);
        ps.executeUpdate();
    }
    
    public void updateOrderItems(int orderId, List<Integer> productIds, List<Integer> quantities) throws SQLException {
        
        if(productIds == null || productIds.isEmpty()) {
            throw new SQLException("Order must have at least 1 item.");
        }
        
        String deleteSQL = "DELETE FROM order_details WHERE order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteSQL)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }

        String insertSQL = "INSERT INTO order_details (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            for (int i = 0; i < productIds.size(); i++) {
                ps.setInt(1, orderId);
                ps.setInt(2, productIds.get(i));
                ps.setInt(3, quantities.get(i));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public void deleteOrder(int orderId) throws SQLException {
        String sql1 = "DELETE FROM order_details WHERE order_id = ?";
        String sql2 = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(sql1);
             PreparedStatement ps2 = conn.prepareStatement(sql2)) {
            ps1.setInt(1, orderId);
            ps1.executeUpdate();
            ps2.setInt(1, orderId);
            ps2.executeUpdate();
        }
    }
    
    public int countOrders(String status, String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders o JOIN customers c ON o.customer_id = c.id WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (status != null && !status.isEmpty()) {
            sql += " AND o.status = ?";
            params.add(status);
        }
        if (search != null && !search.isEmpty()) {
            sql += " AND (CAST(o.id AS NVARCHAR) LIKE ? OR c.name LIKE ? OR o.notes LIKE ?)";
            String q = "%" + search + "%";
            params.add(q); params.add(q); params.add(q);
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public List<Order> getOrdersByPage(String status, String search, String sort, int page, int size) throws SQLException {
        List<Order> list = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT o.*, c.name as customer_name FROM orders o JOIN customers c ON o.customer_id = c.id WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (status != null && !status.isEmpty()) {
            sql += " AND o.status = ?";
            params.add(status);
        }
        if (search != null && !search.isEmpty()) {
            sql += " AND (CAST(o.id AS NVARCHAR) LIKE ? OR c.name LIKE ? OR o.notes LIKE ?)";
            String q = "%" + search + "%";
            params.add(q); params.add(q); params.add(q);
        }
        if (sort == null || (!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc"))) {
            sort = "desc";
        }
        sql += " ORDER BY o.order_date " + sort.toUpperCase() + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset);
        params.add(size);

        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setStatus(rs.getString("status"));
            o.setOrderDate(rs.getTimestamp("order_date"));
            o.setAppointmentTime(rs.getTimestamp("appointment_time"));
            o.setPriorityDeliveryDate(rs.getDate("priority_delivery_date"));
            o.setNotes(rs.getString("notes"));
            Customer c = new Customer();
            c.setId(rs.getInt("customer_id"));
            c.setName(rs.getString("customer_name"));
            o.setCustomer(c);
            list.add(o);
        }
        return list;
    }
    
    public List<Order> getOrdersByCustomer(int customerId, String sort) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_date " + ("asc".equalsIgnoreCase(sort) ? "ASC" : "DESC");
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setStatus(rs.getString("status"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                o.setAppointmentTime(rs.getTimestamp("appointment_time"));
                o.setPriorityDeliveryDate(rs.getDate("priority_delivery_date"));
                o.setNotes(rs.getString("notes"));
                list.add(o);
            }
        }
        return list;
    }

    public Map<String, Integer> getOrderCountByMonth(int numMonths) throws SQLException {
        String sql = """
            SELECT FORMAT(order_date, 'yyyy-MM') AS ym, COUNT(*) as cnt
            FROM orders
            WHERE order_date >= DATEADD(MONTH, -?, GETDATE())
            GROUP BY FORMAT(order_date, 'yyyy-MM')
            ORDER BY ym
        """;
        Map<String, Integer> map = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numMonths-1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        }
        return map;
    }
}

