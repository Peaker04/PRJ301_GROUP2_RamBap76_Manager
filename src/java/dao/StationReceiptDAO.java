package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.StationReceipt;
import model.StationReceiptDetail;

public class StationReceiptDAO {
    
    private Connection conn;
    public StationReceiptDAO(Connection conn) { this.conn = conn; }
    
    public List<StationReceipt> getAllReceipts(String search, String sort) throws SQLException {
        List<StationReceipt> list = new ArrayList<>();
        String sql = "SELECT * FROM station_receipts WHERE 1=1";
        if (search != null && !search.isEmpty()) {
            sql += " AND (station_name LIKE ?)";
        }
        if (sort == null || (!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc"))) sort = "desc";
        sql += " ORDER BY receipt_date " + sort.toUpperCase() + ", id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (search != null && !search.isEmpty()) {
                ps.setString(1, "%" + search + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StationReceipt sr = new StationReceipt();
                sr.setId(rs.getInt("id"));
                sr.setReceiptDate(rs.getDate("receipt_date"));
                sr.setStationName(rs.getString("station_name"));
                sr.setTransportFee(rs.getDouble("transport_fee"));
                sr.setTotalQuantity(rs.getInt("total_quantity"));
                sr.setIsVerified(rs.getBoolean("is_verified"));
                sr.setVerifiedBy((Integer)rs.getObject("verified_by"));
                sr.setVerifiedAt(rs.getTimestamp("verified_at"));
                sr.setExpirationDate(rs.getDate("expiration_date"));
                list.add(sr);
            }
        }
        return list;
    }

    public StationReceipt getReceiptById(int id) throws SQLException {
        String sql = "SELECT * FROM station_receipts WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                StationReceipt sr = new StationReceipt();
                sr.setId(rs.getInt("id"));
                sr.setReceiptDate(rs.getDate("receipt_date"));
                sr.setStationName(rs.getString("station_name"));
                sr.setTransportFee(rs.getDouble("transport_fee"));
                sr.setTotalQuantity(rs.getInt("total_quantity"));
                sr.setIsVerified(rs.getBoolean("is_verified"));
                sr.setVerifiedBy((Integer)rs.getObject("verified_by"));
                sr.setVerifiedAt(rs.getTimestamp("verified_at"));
                sr.setExpirationDate(rs.getDate("expiration_date"));
                return sr;
            }
        }
        return null;
    }

    public List<StationReceiptDetail> getDetails(int receiptId) throws SQLException {
        List<StationReceiptDetail> list = new ArrayList<>();
        String sql = "SELECT srd.*, p.name as product_name FROM station_receipt_details srd JOIN products p ON srd.product_id = p.id WHERE receipt_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                StationReceiptDetail d = new StationReceiptDetail();
                d.setReceiptId(rs.getInt("receipt_id"));
                d.setProductId(rs.getInt("product_id"));
                d.setProductName(rs.getString("product_name"));
                d.setQuantity(rs.getInt("quantity"));
                d.setCurrentStock(rs.getInt("current_stock"));
                d.setRemainingQuantity(rs.getInt("remaining_quantity"));
                list.add(d);
            }
        }
        return list;
    }

    // Thêm mới phiếu nhập
    public int createReceipt(StationReceipt sr, List<StationReceiptDetail> details) throws SQLException {
        String sql = "INSERT INTO station_receipts (receipt_date, station_name, transport_fee, total_quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, sr.getReceiptDate());
            ps.setString(2, sr.getStationName());
            ps.setDouble(3, sr.getTransportFee());
            ps.setInt(4, sr.getTotalQuantity());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int receiptId = rs.getInt(1);
                for (StationReceiptDetail d : details) {
                    addDetail(receiptId, d);
                }
                return receiptId;
            }
        }
        return -1;
    }

    // Thêm chi tiết & cập nhật tồn kho
    public void addDetail(int receiptId, StationReceiptDetail d) throws SQLException {
        String sql = "INSERT INTO station_receipt_details (receipt_id, product_id, quantity, current_stock, remaining_quantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, receiptId);
            ps.setInt(2, d.getProductId());
            ps.setInt(3, d.getQuantity());
            ps.setInt(4, d.getCurrentStock());
            ps.setInt(5, d.getQuantity()); // remaining_quantity = nhập mới
            ps.executeUpdate();
        }
    }

    public void updateDetail(int receiptId, StationReceiptDetail d) throws SQLException {
        String sql = "UPDATE station_receipt_details SET quantity=?, current_stock=?, remaining_quantity=? WHERE receipt_id=? AND product_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getQuantity());
            ps.setInt(2, d.getCurrentStock());
            ps.setInt(3, d.getRemainingQuantity());
            ps.setInt(4, receiptId);
            ps.setInt(5, d.getProductId());
            ps.executeUpdate();
        }
    }

    public void updateReceipt(StationReceipt sr) throws SQLException {
        String sql = "UPDATE station_receipts SET station_name=?, transport_fee=?, total_quantity=?, is_verified=?, verified_by=?, verified_at=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sr.getStationName());
            ps.setDouble(2, sr.getTransportFee());
            ps.setInt(3, sr.getTotalQuantity());
            ps.setBoolean(4, sr.isVerified());
            if (sr.getVerifiedBy() != null)
                ps.setInt(5, sr.getVerifiedBy());
            else
                ps.setNull(5, Types.INTEGER);
            ps.setTimestamp(6, sr.getVerifiedAt());
            ps.setInt(7, sr.getId());
            ps.executeUpdate();
        }
    }

    public void deleteReceipt(int id) throws SQLException {
        String sql = "DELETE FROM station_receipts WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    
    public int countReceipts(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM station_receipts WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            sql += " AND (station_name LIKE ?)";
            String q = "%" + search + "%";
            params.add(q);
        }
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public List<StationReceipt> getReceiptsByPage(String search, String sort, int page, int size) throws SQLException {
        List<StationReceipt> list = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM station_receipts WHERE 1=1";
        List<Object> params = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            sql += " AND (station_name LIKE ?)";
            String q = "%" + search + "%";
            params.add(q);
        }
        if (sort == null || (!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc"))) sort = "desc";
        sql += " ORDER BY receipt_date " + sort.toUpperCase() + ", id DESC";
        sql += " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset); params.add(size);

        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            StationReceipt sr = new StationReceipt();
            sr.setId(rs.getInt("id"));
            sr.setReceiptDate(rs.getDate("receipt_date"));
            sr.setStationName(rs.getString("station_name"));
            sr.setTransportFee(rs.getDouble("transport_fee"));
            sr.setTotalQuantity(rs.getInt("total_quantity"));
            sr.setIsVerified(rs.getBoolean("is_verified"));
            sr.setVerifiedBy((Integer)rs.getObject("verified_by"));
            sr.setVerifiedAt(rs.getTimestamp("verified_at"));
            sr.setExpirationDate(rs.getDate("expiration_date"));
            list.add(sr);
        }
        return list;
    }
    
    public String checkImportLimit(int productId, int quantity, Date receiptDate) throws SQLException {
        String sql = "SELECT dbo.fn_CanImportProductToday(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, quantity);
            ps.setDate(3, receiptDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1); // trả về null nếu không lỗi
                }
            }
        }
        return null;
    }
}
