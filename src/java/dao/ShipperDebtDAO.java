package dao;

import connect.DBConnection;
import model.ShipperDebt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipperDebtDAO {
    private final DBConnection dbConnection;

    public ShipperDebtDAO() {
        dbConnection = new DBConnection();
    }

    //  Lấy công nợ của 1 shipper
    public ShipperDebt getDebtByShipperId(int shipperId) throws SQLException {
        String sql = "SELECT shipper_id, amount FROM shipper_debts WHERE shipper_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ShipperDebt(
                    rs.getInt("shipper_id"),
                    rs.getDouble("amount")
                );
            }
        }
        return null; // Không có công nợ
    }

    //  Thêm mới hoặc cập nhật công nợ
    public void insertOrUpdateDebt(ShipperDebt debt) throws SQLException {
        String sql = """
            MERGE INTO shipper_debts AS target
            USING (SELECT ? AS shipper_id) AS source
            ON target.shipper_id = source.shipper_id
            WHEN MATCHED THEN
                UPDATE SET amount = ?
            WHEN NOT MATCHED THEN
                INSERT (shipper_id, amount) VALUES (?, ?);
            """;

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, debt.getShipperId());
            stmt.setDouble(2, debt.getDebtAmount());
            stmt.setInt(3, debt.getShipperId());
            stmt.setDouble(4, debt.getDebtAmount());

            stmt.executeUpdate();
        }
    }

    //  Lấy danh sách công nợ của tất cả shipper
    public List<ShipperDebt> getAllDebts() throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = "SELECT shipper_id, amount FROM shipper_debts ORDER BY shipper_id";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ShipperDebt debt = new ShipperDebt(
                    rs.getInt("shipper_id"),
                    rs.getDouble("amount")
                );
                list.add(debt);
            }
        }

        return list;
    }
}
