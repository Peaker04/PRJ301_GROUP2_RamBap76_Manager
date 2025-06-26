package dao;

import connect.DBConnection;
import model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private final DBConnection dbConnection;

    public TransactionDAO() {
        dbConnection = new DBConnection();
    }

    //  Thêm 1 giao dịch mới
    public void insertTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (shipper_id, amount, type, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transaction.getShipperId());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setString(3, transaction.getType());
            stmt.setString(4, transaction.getDescription());

            stmt.executeUpdate();
        }
    }

    //  Lấy tất cả giao dịch theo shipper
    public List<Transaction> getTransactionsByShipperId(int shipperId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE shipper_id = ? ORDER BY created_at DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setShipperId(rs.getInt("shipper_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setType(rs.getString("type"));
                t.setDescription(rs.getString("description"));
                list.add(t);
            }
        }

        return list;
    }

    //  Tính tổng số tiền theo loại (COLLECT, PAYMENT, ...)
    public double getTotalAmountByShipperIdAndType(int shipperId, String type) throws SQLException {
        String sql = "SELECT SUM(amount) AS total FROM transactions WHERE shipper_id = ? AND type = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0;
    }
}

