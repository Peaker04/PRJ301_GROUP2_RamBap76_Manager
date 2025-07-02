package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Customer;

public class CustomerDAO {
    private Connection conn;
    public CustomerDAO(Connection conn) { this.conn = conn; }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
}