package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Customer;

public class CustomerDAO {
    private Connection conn;
    public CustomerDAO(Connection conn) { this.conn = conn; }

     public List<Customer> getAllCustomers(String search, String sort) throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE is_active = 1";
        if (search != null && !search.isEmpty()) {
            sql += " AND (name LIKE ? OR phone LIKE ? OR address LIKE ?)";
        }
        
        String order = "az".equalsIgnoreCase(sort) ? "ASC" : "DESC";
        sql += " ORDER BY RIGHT(name, CHARINDEX(' ', REVERSE(name) + ' ') - 1) " + order;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int idx = 1;
            if (search != null && !search.isEmpty()) {
                String q = "%" + search + "%";
                ps.setString(idx++, q);
                ps.setString(idx++, q);
                ps.setString(idx++, q);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setNotes(rs.getString("notes"));
                c.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                c.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                list.add(c);
            }
        }
        return list;
    }
     
    public List<Customer> getAllCustomers() throws SQLException {
        return getAllCustomers(null, "asc");
    }
    
    public void createCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (name, phone, address, notes, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getNotes());
            if (c.getLatitude() != null) ps.setDouble(5, c.getLatitude()); else ps.setNull(5, Types.DOUBLE);
            if (c.getLongitude() != null) ps.setDouble(6, c.getLongitude()); else ps.setNull(6, Types.DOUBLE);
            ps.executeUpdate();
        }
    }

    public void softDeleteCustomers(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("UPDATE customers SET is_active = 0 WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append("?");
            if (i != ids.size() - 1) sql.append(",");
        }
        sql.append(")");
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            ps.executeUpdate();
        }
    }
    
    public int countCustomers(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE is_active =1";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            sql += " AND (name LIKE ? OR phone LIKE ? OR address LIKE ?)";
            String q = "%" + search + "%";
            params.add(q); params.add(q); params.add(q);
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public List<Customer> getCustomersByPage(String search, String sort, int page, int size) throws SQLException {
        List<Customer> list = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM customers WHERE is_active = 1";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            sql += " AND (name LIKE ? OR phone LIKE ? OR address LIKE ?)";
            String q = "%" + search + "%";
            params.add(q); params.add(q); params.add(q);
        }
        String order = "az".equalsIgnoreCase(sort) ? "ASC" : "DESC";
        // sort theo ký tự cuối cùng trong tên
        sql += " ORDER BY RIGHT(name, CHARINDEX(' ', REVERSE(name) + ' ') - 1) " + order;
        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset); params.add(size);

        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Customer c = new Customer();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setPhone(rs.getString("phone"));
            c.setAddress(rs.getString("address"));
            c.setNotes(rs.getString("notes"));
            c.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
            c.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
            list.add(c);
        }
        return list;
    }
    
    public List<Customer> getTopUsersByOrders(int topN) throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT c.id, c.name, COUNT(o.id) as totalOrders " +
                     "FROM customers c JOIN orders o ON c.id = o.customer_id " +
                     "GROUP BY c.id, c.name ORDER BY totalOrders DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, topN);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setTotalOrders(rs.getInt("totalOrders"));
                list.add(c);
            }
        }
        return list;
    }

    public Customer getCustomerById(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ? AND is_active = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setNotes(rs.getString("notes"));
                c.setLatitude(rs.getObject("latitude") != null ? rs.getDouble("latitude") : null);
                c.setLongitude(rs.getObject("longitude") != null ? rs.getDouble("longitude") : null);
                c.setActive(rs.getBoolean("is_active"));
                return c;
            }
        }
        return null;
    }

    public void addCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO customers (name, phone, address, notes, latitude, longitude, is_active) VALUES (?, ?, ?, ?, ?, ?, 1)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getNotes());
            ps.setDouble(5, c.getLatitude());
            ps.setDouble(6, c.getLongitude());
            ps.executeUpdate();
        }
    }

    public void updateCustomer(Customer c) throws SQLException {
        String sql = "UPDATE customers SET name=?, phone=?, address=?, notes=?, latitude=?, longitude=? WHERE id=? AND is_active = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getNotes());
            if (c.getLatitude() != null) ps.setDouble(5, c.getLatitude()); else ps.setNull(5, Types.DOUBLE);
            if (c.getLongitude() != null) ps.setDouble(6, c.getLongitude()); else ps.setNull(6, Types.DOUBLE);
            ps.setInt(7, c.getId());
            ps.executeUpdate();
        }
    }

    public void softDeleteCustomer(int id) throws SQLException {
        String sql = "UPDATE customers SET is_active = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean phoneExists(String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers WHERE phone = ? AND is_active = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}