package dao;

import connect.DBConnection;
import model.ShipperDebt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DebtHistory;

/*

Lấy tất cả công nợ của shipper

Lấy công nợ của một shipper theo tên shipper (getDebtByShipperName)

Lấy công nợ của một shipper theo ID

Thêm hoặc cập nhật công nợ (insertOrUpdateDebt)

Lấy tất cả công nợ (với thông tin ngày thanh toán) (getAllDebtsWithPaymentDate)

Tính công nợ hàng ngày cho shipper (createDailyDebts)

Đánh dấu đã thanh toán công nợ  (payDebt)

Lấy công nợ chưa thanh toán (getUnpaidDebts)

Lấy tổng công nợ của tất cả shipper (getTotalDebt)

Lấy công nợ của shipper theo ngày (getDebtsByDate)

Lấy công nợ của shipper theo trạng thái thanh toán (getDebtsByPaymentStatus)

Xóa công nợ của shipper (deleteDebt)

Kiểm tra xem một công nợ của shipper (với một shipper_id và debtDate cụ thể) đã được thanh toán hay chưa. 

Kiểm tra danh sách công nợ quá hạn

Kiểm tra danh sách shipper có bị khoá do nợ quá hạn không

 */
public class ShipperDebtDAO {

    private final DBConnection dbConnection;

    public ShipperDebtDAO() {
        dbConnection = new DBConnection();
    }

// Lấy tất cả công nợ của shipper
    public List<ShipperDebt> getAllDebts() throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = """
        SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date
        FROM shipper_debts sd
        JOIN users u ON sd.shipper_id = u.id
        ORDER BY u.full_name, sd.date;
    """;

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShipperDebt debt = new ShipperDebt(
                        rs.getInt("shipper_id"), // shipper_id (khóa chính)
                        rs.getString("shipper_name"), // shipper_name
                        rs.getDate("date"), // Ngày công nợ
                        rs.getDouble("amount"), // Số tiền công nợ
                        rs.getTimestamp("payment_date") // Ngày thanh toán (nếu có)
                );
                list.add(debt);  // Thêm công nợ vào danh sách
            }
        }

        return list;  // Trả về danh sách công nợ của tất cả shipper
    }

// Lấy công nợ của một shipper theo tên shipper
    public ShipperDebt getDebtByShipperName(String shipperName) throws SQLException {
        String sql = """
        SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date
        FROM shipper_debts sd
        JOIN users u ON sd.shipper_id = u.id
        WHERE u.full_name = ?  -- Lọc theo tên shipper
        ORDER BY sd.date;  -- Sắp xếp theo ngày công nợ
    """;

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Lọc theo tên shipper
            stmt.setString(1, shipperName); // Gán shipperName vào câu truy vấn
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra nếu có kết quả trả về
            if (rs.next()) {
                // Trả về công nợ của shipper dựa trên shipper_id (tên)
                return new ShipperDebt(
                        rs.getInt("shipper_id"), // shipper_id
                        rs.getString("shipper_name"), // shipper_name
                        rs.getDate("date"), // Lấy ngày công nợ (java.sql.Date)
                        rs.getDouble("amount"), // Lấy số tiền công nợ (double)
                        rs.getTimestamp("payment_date") // Lấy ngày thanh toán (java.sql.Timestamp)
                );
            }
        }
        return null; // Trả về null nếu không tìm thấy công nợ của shipper
    }

    // Lấy công nợ của một shipper theo ID
    public ShipperDebt getDebtByShipperId(int shipperId) throws SQLException {
        String sql = """
    SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date
    FROM shipper_debts sd
    JOIN users u ON sd.shipper_id = u.id
    WHERE sd.shipper_id = ?  -- Lọc theo shipper_id
    ORDER BY sd.date;  -- Sắp xếp theo ngày công nợ
    """;

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Lọc theo shipper_id
            stmt.setInt(1, shipperId);  // Gán shipperId vào câu truy vấn
            ResultSet rs = stmt.executeQuery();

            // Kiểm tra nếu có kết quả trả về
            if (rs.next()) {
                // Trả về công nợ của shipper dựa trên shipper_id
                return new ShipperDebt(
                        rs.getInt("shipper_id"), // shipper_id
                        rs.getString("shipper_name"), // shipper_name
                        rs.getDate("date"), // Lấy ngày công nợ (java.sql.Date)
                        rs.getDouble("amount"), // Lấy số tiền công nợ (double)
                        rs.getTimestamp("payment_date") // Lấy ngày thanh toán (java.sql.Timestamp)
                );
            }
        }
        return null; // Trả về null nếu không tìm thấy công nợ của shipper
    }

    //  Thêm mới hoặc cập nhật công nợ
    public void insertOrUpdateDebt(ShipperDebt debt) throws SQLException {
        String sql = """
    MERGE INTO shipper_debts AS target
    USING (SELECT ? AS shipper_id, ? AS date, ? AS amount) AS source
    ON target.shipper_id = source.shipper_id AND target.date = source.date
    WHEN MATCHED THEN
        UPDATE SET amount = source.amount
    WHEN NOT MATCHED THEN
        INSERT (shipper_id, date, amount) 
        VALUES (source.shipper_id, source.date, source.amount);
    """;  // Sử dụng shipper_id thay vì shipper_name làm điều kiện

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Tham số đầu tiên: shipperId
            stmt.setInt(1, debt.getShipperId());  // Gán shipperId vào câu truy vấn

            // Tham số thứ hai: date (chuyển đổi sang java.sql.Date nếu cần)
            stmt.setDate(2, new java.sql.Date(debt.getDate().getTime()));  // Chuyển từ java.util.Date sang java.sql.Date

            // Tham số thứ ba: debtAmount
            stmt.setDouble(3, debt.getDebtAmount());

            // Thực thi câu lệnh SQL
            stmt.executeUpdate();
        }
    }

//  Lấy danh sách công nợ của tất cả shipper (có thêm ngày thanh toán)
    public List<ShipperDebt> getAllDebtsWithPaymentDate() throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = "SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date "
                + "FROM shipper_debts sd "
                + "JOIN users u ON sd.shipper_id = u.id "
                + "WHERE sd.payment_date IS NULL "
                + "ORDER BY sd.shipper_id, sd.date;";  // Truy vấn thông tin công nợ chưa thanh toán

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ShipperDebt debt = new ShipperDebt(
                        rs.getInt("shipper_id"), // Lấy shipper_id làm khóa chính
                        rs.getString("shipper_name"), // Lấy tên shipper từ alias shipper_name
                        rs.getDate("date"), // Lấy ngày công nợ (java.sql.Date)
                        rs.getDouble("amount"), // Lấy số tiền công nợ
                        rs.getTimestamp("payment_date") // Lấy ngày thanh toán (java.sql.Timestamp)
                );
                list.add(debt);  // Thêm công nợ vào danh sách
            }
        }

        return list;  // Trả về danh sách công nợ
    }

    // Hàm tính công nợ hàng ngày cho shipper
    public void createDailyDebts() throws SQLException {
        String sql = """
    MERGE INTO shipper_debts AS target
    USING (SELECT 
                d.initial_shipper_id AS shipper_id,  
                CAST(GETDATE() AS DATE) AS date,        -- Lấy ngày hiện tại làm ngày công nợ
                SUM(d.delivery_fee + d.box_fee) AS amount 
            FROM deliveries d
            WHERE d.status = 'COMPLETED' 
            AND CAST(d.actual_delivery_time AS DATE) = CAST(GETDATE() AS DATE)  -- Lọc các đơn hàng giao trong ngày
            GROUP BY d.initial_shipper_id) AS source
    ON target.shipper_id = source.shipper_id AND target.date = source.date
    WHEN MATCHED THEN
        UPDATE SET amount = source.amount
    WHEN NOT MATCHED THEN
        INSERT (shipper_id, date, amount) 
        VALUES (source.shipper_id, source.date, source.amount);
    """;

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);  // Tắt auto commit để dùng giao dịch
            stmt.executeUpdate();
            conn.commit();  // Xác nhận giao dịch
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu có
        }
    }

    // Đánh dấu đã thanh toán công nợ 
    public void payDebt(int shipperId, Date debtDate) throws SQLException {
        String sql = "UPDATE shipper_debts SET payment_date = GETDATE() WHERE shipper_id = ? AND date = ? AND payment_date IS NULL";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Cập nhật ngày thanh toán
            stmt.setInt(2, shipperId); // Thay shipperId vào câu lệnh
            stmt.setDate(3, debtDate); // Thay debtDate vào câu lệnh

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Không tìm thấy công nợ của shipper với ID " + shipperId + " và ngày công nợ " + debtDate);
            } else {
                System.out.println("Cập nhật ngày thanh toán thành công cho shipper ID " + shipperId + " và ngày công nợ " + debtDate);
            }
        }
    }

    // Hàm lấy công nợ chưa thanh toán (getUnpaidDebts)
    public List<ShipperDebt> getUnpaidDebts() throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = "SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date "
                + "FROM shipper_debts sd JOIN users u ON sd.shipper_id = u.id "
                + "WHERE sd.payment_date IS NULL ORDER BY sd.shipper_id, sd.date";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShipperDebt debt = new ShipperDebt(
                        rs.getInt("shipper_id"),
                        rs.getString("shipper_name"),
                        rs.getDate("date"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date")
                );
                list.add(debt);
            }
        }
        return list;
    }

    // Lấy tổng công nợ của tất cả shipper
    public double getTotalDebt() throws SQLException {
        String sql = "SELECT SUM(amount) AS total_debt FROM shipper_debts WHERE payment_date IS NULL";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total_debt");
            }
        }

        return 0;
    }

    // Lấy công nợ của shipper theo ngày
    public List<ShipperDebt> getDebtsByDate(int shipperId, Date date) throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = """
    SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date
    FROM shipper_debts sd
    JOIN users u ON sd.shipper_id = u.id
    WHERE sd.shipper_id = ? AND sd.date = ?
    ORDER BY sd.date;
    """;  // Thực hiện JOIN với bảng users để lấy shipper_name từ bảng users

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipperId);  // Gán shipperId vào câu truy vấn
            stmt.setDate(2, date);       // Gán date vào câu truy vấn

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Lấy thông tin công nợ từ kết quả truy vấn
                    ShipperDebt debt = new ShipperDebt(
                            rs.getInt("shipper_id"), // Lấy shipper_id từ bảng shipper_debts (khóa chính)
                            rs.getString("shipper_name"), // Lấy tên shipper từ bảng users
                            rs.getDate("date"), // Lấy ngày công nợ
                            rs.getDouble("amount"), // Lấy số tiền công nợ
                            rs.getTimestamp("payment_date") // Lấy ngày thanh toán
                    );
                    list.add(debt);  // Thêm công nợ vào danh sách
                }
            }
        }

        return list;  // Trả về danh sách công nợ của shipper theo ngày
    }

    // Lấy công nợ của shipper theo trạng thái thanh toán (Thanh toán hay Chưa thanh toán)
    public List<ShipperDebt> getDebtsByPaymentStatus(boolean isPaid) throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = """
        SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date
        FROM shipper_debts sd
        JOIN users u ON sd.shipper_id = u.id
        WHERE sd.payment_date IS """ + (isPaid ? "NOT NULL" : "NULL");

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShipperDebt debt = new ShipperDebt(
                        rs.getInt("shipper_id"), // Lấy shipper_id từ bảng shipper_debts
                        rs.getString("shipper_name"), // Lấy shipper_name từ bảng users
                        rs.getDate("date"), // Ngày công nợ
                        rs.getDouble("amount"), // Số tiền công nợ
                        rs.getTimestamp("payment_date") // Ngày thanh toán (nếu có)
                );
                list.add(debt);  // Thêm công nợ vào danh sách
            }
        }

        return list;  // Trả về danh sách công nợ theo trạng thái thanh toán
    }

    // Xóa công nợ của shipper
    public void deleteDebt(int debtId) throws SQLException {
        String sql = "DELETE FROM shipper_debts WHERE id = ?";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, debtId);  // Xác định công nợ cần xóa
            stmt.executeUpdate();  // Thực hiện xóa
        }
    }

    // Kiểm tra xem một công nợ của shipper (với một shipper_id và debtDate cụ thể) đã được thanh toán hay chưa. 
    public boolean isDebtPaid(int shipperId, Date debtDate) throws SQLException {
        String sql = "SELECT payment_date FROM shipper_debts WHERE shipper_id = ? AND date = ? AND payment_date IS NOT NULL";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipperId);  // Xác định shipperId
            stmt.setDate(2, debtDate);   // Xác định ngày công nợ

            ResultSet rs = stmt.executeQuery();

            return rs.next();  // Nếu có payment_date, tức là công nợ đã được thanh toán
        }
    }

    // Kiểm tra danh sách công nợ quá hạn
    public List<ShipperDebt> getOverdueDebts() throws SQLException {
        List<ShipperDebt> list = new ArrayList<>();
        String sql = """
        SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date
        FROM shipper_debts sd
        JOIN users u ON sd.shipper_id = u.id
        WHERE sd.payment_date IS NULL 
        AND DATEDIFF(DAY, sd.date, GETDATE()) > 2  -- Lọc công nợ quá hạn hơn 2 ngày
        ORDER BY sd.date;
    """;  // JOIN bảng users để lấy shipper_name

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ShipperDebt debt = new ShipperDebt(
                        rs.getInt("shipper_id"), // shipper_id
                        rs.getString("shipper_name"), // shipper_name
                        rs.getDate("date"), // ngày công nợ
                        rs.getDouble("amount"), // số tiền công nợ
                        rs.getTimestamp("payment_date") // ngày thanh toán (nếu có)
                );
                list.add(debt);  // Thêm công nợ vào danh sách
            }
        }

        return list;  // Trả về danh sách công nợ quá hạn
    }

    // Kiểm tra shipper có bị khoá do nợ quá hạn không
    public boolean isShipperBlocked(int shipperId) throws SQLException {
        String sql = """
    SELECT 1 
    FROM shipper_debts 
    WHERE shipper_id = ? 
    AND payment_date IS NULL 
    AND date <= DATEADD(DAY, -2, CAST(GETDATE() AS DATE)) 
    """;

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);  // Gán shipperId vào câu truy vấn

            try (ResultSet rs = stmt.executeQuery()) {
                // Nếu có bản ghi (rs.next() trả về true), tức là shipper có nợ quá hạn và chưa thanh toán
                return rs.next();
            }
        }
    }

    public ShipperDebt getDebtByShipperIdAndDate(int shipperId, java.sql.Date date) throws SQLException {
        String sql = "SELECT sd.shipper_id, u.full_name AS shipper_name, sd.date, sd.amount, sd.payment_date "
                + "FROM shipper_debts sd JOIN users u ON sd.shipper_id = u.id "
                + "WHERE sd.shipper_id = ? AND sd.date = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipperId);
            stmt.setDate(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ShipperDebt(
                            rs.getInt("shipper_id"),
                            rs.getString("shipper_name"),
                            rs.getDate("date"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("payment_date")
                    );
                }
            }
        }
        return null;
    }

    public void updateDebt(int shipperId, java.sql.Date date, double amount) throws SQLException {
        String sql = "UPDATE shipper_debts SET amount = ? WHERE shipper_id = ? AND date = ? AND payment_date IS NULL";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, shipperId);
            stmt.setDate(3, date);
            stmt.executeUpdate();
        }
    }

    public void updateDebtWithHistory(int shipperId, java.sql.Date date, double newAmount, String reason, int adminId) throws SQLException {
        // Lấy số tiền cũ
        double oldAmount = 0;
        String selectSql = "SELECT amount FROM shipper_debts WHERE shipper_id = ? AND date = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, shipperId);
            selectStmt.setDate(2, date);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    oldAmount = rs.getDouble("amount");
                }
            }
        }

        // Cập nhật số tiền mới
        String updateSql = "UPDATE shipper_debts SET amount = ? WHERE shipper_id = ? AND date = ? AND payment_date IS NULL";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setDouble(1, newAmount);
            updateStmt.setInt(2, shipperId);
            updateStmt.setDate(3, date);
            updateStmt.executeUpdate();
        }

        // Ghi lịch sử thay đổi
        String insertHistory = "INSERT INTO shipper_debt_history (shipper_id, debt_date, old_amount, new_amount, change_reason, changed_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(insertHistory)) {
            stmt.setInt(1, shipperId);
            stmt.setDate(2, date);
            stmt.setDouble(3, oldAmount);
            stmt.setDouble(4, newAmount);
            stmt.setString(5, reason);
            stmt.setInt(6, adminId);
            stmt.executeUpdate();
        }
    }

    public List<DebtHistory> getDebtHistory(int shipperId, java.sql.Date date) throws SQLException {
        List<DebtHistory> list = new ArrayList<>();
        String sql = """
        SELECT h.*, u.full_name AS changer_name, u.role AS changer_role
        FROM shipper_debt_history h
        JOIN users u ON h.changed_by = u.id
        WHERE h.shipper_id = ? AND h.debt_date = ?
        ORDER BY h.changed_at DESC
    """;
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipperId);
            stmt.setDate(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DebtHistory h = new DebtHistory(
                            rs.getInt("id"),
                            rs.getInt("shipper_id"),
                            rs.getDate("debt_date"),
                            rs.getDouble("old_amount"),
                            rs.getDouble("new_amount"),
                            rs.getString("change_reason"),
                            rs.getInt("changed_by"),
                            rs.getTimestamp("changed_at"),
                            rs.getString("changer_name"), // Thêm tên người thay đổi
                            rs.getString("changer_role") // Thêm vai trò
                    );
                    list.add(h);
                }
            }
        }
        return list;
    }

    public List<DebtHistory> getDebtHistory(Date date) throws SQLException {
        List<DebtHistory> list = new ArrayList<>();

        String sql = "SELECT dh.*, a.name AS changer_name, a.role AS changer_role "
                + "FROM debt_history dh "
                + "JOIN accounts a ON dh.changed_by = a.id "
                + "WHERE dh.debt_date = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, date);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DebtHistory dh = new DebtHistory(
                        rs.getInt("id"),
                        rs.getInt("shipper_id"),
                        rs.getDate("debt_date"),
                        rs.getDouble("old_amount"),
                        rs.getDouble("new_amount"),
                        rs.getString("change_reason"),
                        rs.getInt("changed_by"),
                        rs.getTimestamp("changed_at"),
                        rs.getString("changer_name"),
                        rs.getString("changer_role")
                );
                list.add(dh);
            }
        }

        return list;
    }

    // Hàm Java lấy tên shipper theo ID
    public String getShipperNameById(int shipperId) {
        String name = "";
        String sql = "SELECT u.full_name FROM shippers s JOIN users u ON s.user_id = u.id WHERE s.user_id = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shipperId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("full_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void payDebtWithHistory(int shipperId, java.sql.Date date, String reason, int adminId) throws SQLException {
        // Lấy số tiền cũ
        double oldAmount = 0;
        String selectSql = "SELECT amount FROM shipper_debts WHERE shipper_id = ? AND date = ?";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setInt(1, shipperId);
            selectStmt.setDate(2, date);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    oldAmount = rs.getDouble("amount");
                }
            }
        }

        // Cập nhật trạng thái đã thanh toán
        String updateSql = "UPDATE shipper_debts SET payment_date = GETDATE() WHERE shipper_id = ? AND date = ? AND payment_date IS NULL";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, shipperId);
            updateStmt.setDate(2, date);
            updateStmt.executeUpdate();
        }

        // Ghi lịch sử thay đổi
        String insertHistory = "INSERT INTO shipper_debt_history (shipper_id, debt_date, old_amount, new_amount, change_reason, changed_by) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(insertHistory)) {
            stmt.setInt(1, shipperId);
            stmt.setDate(2, date);
            stmt.setDouble(3, oldAmount);
            stmt.setDouble(4, oldAmount); // Số tiền không đổi, chỉ trạng thái đổi
            stmt.setString(5, reason);
            stmt.setInt(6, adminId);
            stmt.executeUpdate();
        }
    }

}
