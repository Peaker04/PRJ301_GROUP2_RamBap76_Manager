package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;

public class ProductDAO {
    private Connection conn;
    public ProductDAO(Connection conn) { this.conn = conn; }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setStock(rs.getInt("stock"));
                list.add(p);
            }
        }
        return list;
    }
}