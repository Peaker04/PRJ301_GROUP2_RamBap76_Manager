package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Product;
import model.StationReceipt;
import model.StationReceiptDetail;

public class ProductDAO {
    private Connection conn;
    public ProductDAO(Connection conn) { this.conn = conn; }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT product_id, product_name, current_stock FROM V_ProductStock";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setStock(rs.getInt("current_stock"));
                list.add(p);
            }
        }
        return list;
    }
    
    public String getProductName(int productId) throws SQLException {
        String sql = "SELECT name FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString(1);
        }
        return "";
    }

    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT product_id, product_name, current_stock FROM V_ProductStock WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setStock(rs.getInt("current_stock"));
                return p;
            }
        }
        return null;
    }
    
    public void createProduct(String name) throws SQLException {
        String sql = "INSERT INTO products(name, stock) VALUES (?, 0)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }
    
    public int getCurrentStock(int productId) throws SQLException {
        String sql = "SELECT current_stock FROM V_ProductStock WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("current_stock");
            }
        }
        return 0;
    }
    
    public void deductStockByBatch(int productId, int quantity) throws SQLException {
        String sql = """
            SELECT srd.receipt_id, srd.product_id, srd.remaining_quantity
            FROM station_receipt_details srd
            JOIN station_receipts sr ON sr.id = srd.receipt_id
            WHERE srd.product_id = ? AND srd.remaining_quantity > 0 AND sr.expiration_date >= CONVERT(date, GETDATE()) 
            ORDER BY sr.expiration_date, sr.receipt_date
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            int need = quantity;
            while (rs.next() && need > 0) {
                
                int receiptId = rs.getInt("receipt_id");
                int available = rs.getInt("remaining_quantity");
                int deduct = Math.min(available, need);
                String updateSql = "UPDATE station_receipt_details SET remaining_quantity = remaining_quantity - ? WHERE receipt_id = ? AND product_id = ?";
                try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                    ups.setInt(1, deduct);
                    ups.setInt(2, receiptId);
                    ups.setInt(3, productId);
                    ups.executeUpdate();
                }
                need -= deduct;
            }
            if (need > 0) throw new SQLException("Không đủ tồn kho còn hạn sử dụng cho sản phẩm ID " + productId);
        }
    }
     
    public void updateProductName(int id, String name) throws SQLException {
        String sql = "UPDATE products SET name = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void deleteProduct(int id) throws SQLException {
        String sql = "UPDATE products SET is_deleted = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public void deleteProducts(List<Integer> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM products WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append("?");
            if (i < ids.size() - 1) sql.append(",");
        }
        sql.append(")");
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        }
    }

    
    public List<StationReceiptDetail> getImportHistory(int productId) throws SQLException {
        List<StationReceiptDetail> list = new ArrayList<>();
        String sql = """
            SELECT srd.*, sr.station_name, sr.receipt_date, sr.expiration_date, sr.id as receipt_id
            FROM station_receipt_details srd
            JOIN station_receipts sr ON sr.id = srd.receipt_id
            WHERE srd.product_id = ?
            ORDER BY sr.receipt_date DESC
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StationReceiptDetail d = new StationReceiptDetail();
                StationReceipt r = new StationReceipt();
                d.setReceiptId(rs.getInt("receipt_id"));
                d.setProductId(productId);
                d.setQuantity(rs.getInt("quantity"));
                d.setCurrentStock(rs.getInt("current_stock"));
                d.setRemainingQuantity(rs.getInt("remaining_quantity"));
                r.setStationName(rs.getString("station_name"));
                r.setReceiptDate(rs.getDate("receipt_date"));
                r.setExpirationDate(rs.getDate("expiration_date"));
                d.setStationReceipt(r); 
                list.add(d);
            }
        }
        return list;
    }
    
    public int countProducts(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM V_ProductStock WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            sql += " AND product_name LIKE ?";
            params.add("%" + search + "%");
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i+1, params.get(i));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public List<Product> getProductsByPage(String search, String sort, int page, int size) throws SQLException {
        List<Product> list = new ArrayList<>();
        int offset = (page-1)*size;
        String sql = "SELECT * FROM V_ProductStock WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            sql += " AND product_name LIKE ?";
            params.add("%" + search + "%");
        }
        if (sort == null || sort.equals("az")) sql += " ORDER BY product_name ASC";
        else sql += " ORDER BY product_name DESC";
        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset);
        params.add(size);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i+1, params.get(i));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setStock(rs.getInt("current_stock"));
                list.add(p);
            }
        }
        return list;
    }
    
    public Map<String, Integer> getSoldProductsThisMonth() throws SQLException {
        String sql = """
            SELECT p.name, SUM(od.quantity) as total
            FROM order_details od
            JOIN products p ON p.id = od.product_id
            JOIN orders o ON o.id = od.order_id
            WHERE MONTH(o.order_date) = MONTH(GETDATE()) AND YEAR(o.order_date) = YEAR(GETDATE())
            GROUP BY p.name
        """;
        Map<String, Integer> result = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("total"));
            }
        }
        return result;
    }

    public Map<String, Integer> getCurrentStockAllProducts() throws SQLException {
        String sql = "SELECT product_name, current_stock FROM V_ProductStock";
        Map<String, Integer> result = new LinkedHashMap<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("product_name"), rs.getInt("current_stock"));
            }
        }
        return result;
    }

}