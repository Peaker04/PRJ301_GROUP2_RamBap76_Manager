package service;

import connect.DBConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.NumberFormat;
import java.util.Locale;

public class ChatService {

    private static final String API_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "llama3";

    private final List<ChatMessage> conversationHistory = new ArrayList<>();
    private final Gson gson = new Gson();
    private final DBConnection dbConnection;

    public ChatService() {
        this.dbConnection = new DBConnection();
    }

    public boolean isOllamaAvailable() {
        HttpURLConnection connection = null;
        try {
            URL statusUrl = new URL("http://localhost:11434");
            connection = (HttpURLConnection) statusUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String generateResponse(String userMessage) {
        try {
            if (!isOllamaAvailable()) {
                return "⚠️ Dịch vụ AI hiện không khả dụng. Vui lòng kiểm tra Ollama!";
            }

            // Xử lý truy vấn database
            String dbResponse = handleDatabaseQuery(userMessage);
            if (dbResponse != null) {
                conversationHistory.add(new ChatMessage("user", userMessage));
                conversationHistory.add(new ChatMessage("model", dbResponse));
                return dbResponse;
            }

            // Xử lý bằng AI
            conversationHistory.add(new ChatMessage("user", userMessage));
            if (conversationHistory.size() > 5) {
                conversationHistory.subList(0, conversationHistory.size() - 5).clear();
            }

            String systemPrompt = buildSystemPrompt();
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append(systemPrompt);
            for (ChatMessage msg : conversationHistory) {
                String prefix = msg.role.equals("user") ? "User: " : "Assistant: ";
                promptBuilder.append(prefix).append(msg.text).append("\n");
            }
            promptBuilder.append("Assistant: ");

            return callOllamaAPI(promptBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "😵 Xin lỗi, có lỗi xảy ra khi gọi AI. Vui lòng thử lại!";
        }
    }

    private String buildSystemPrompt() {
        return "Bạn là trợ lý AI cho hệ thống quản lý RamBap76. Dưới đây là cấu trúc cơ sở dữ liệu và các chức năng chính:\n"
                + "\n"
                + "### CẤU TRÚC CƠ SỞ DỮ LIỆU:\n"
                + "1. Bảng orders (đơn hàng):\n"
                + "   - Trạng thái: NEW, DELIVERING, DELIVERED, CANCELLED, URGENT, APPOINTMENT\n"
                + "   - Thông tin: order_date, appointment_time, priority_delivery_date\n"
                + "\n"
                + "2. Bảng deliveries (giao hàng):\n"
                + "   - Trạng thái: ASSIGNED, IN_TRANSIT, COMPLETED, CANCELLED\n"
                + "   - Thông tin: delivery_fee, box_fee, collected_amount\n"
                + "\n"
                + "3. Bảng shippers (shipper):\n"
                + "   - Thông tin: area, priority_level, daily_income\n"
                + "   - Liên kết với users qua user_id\n"
                + "\n"
                + "4. Bảng shipper_debts (công nợ):\n"
                + "   - Thông tin: date, amount, payment_date\n"
                + "   - Xem công nợ quá hạn qua view V_ShipperDebt_Overdue\n"
                + "\n"
                + "5. Bảng customers (khách hàng):\n"
                + "   - Thông tin: name, phone, address, notes\n"
                + "   - Vị trí: latitude, longitude\n"
                + "\n"
                + "6. Bảng products (sản phẩm):\n"
                + "   - Loại: RAM MẶN, RAM CHAY, RAM ĐẶC BIỆT\n"
                + "   - Tồn kho: stock\n"
                + "\n"
                + "### CÁC CHỨC NĂNG CHÍNH:\n"
                + "1. Trạng thái đơn hàng:\n"
                + "   - Kiểm tra trạng thái đơn hàng (NEW, DELIVERING,...)\n"
                + "   - Xem lịch sử giao hàng\n"
                + "\n"
                + "2. Thông tin shipper:\n"
                + "   - Khu vực hoạt động (area)\n"
                + "   - Mức độ ưu tiên (priority_level)\n"
                + "   - Thu nhập hàng ngày (daily_income)\n"
                + "\n"
                + "3. Công nợ:\n"
                + "   - Xem công nợ hiện tại\n"
                + "   - Kiểm tra công nợ quá hạn\n"
                + "   - Lịch sử thanh toán\n"
                + "\n"
                + "4. Vị trí giao hàng:\n"
                + "   - Địa chỉ khách hàng\n"
                + "   - Tọa độ (latitude, longitude)\n"
                + "   - Tự động phân shipper theo khu vực\n"
                + "\n"
                + "### YÊU CẦU TRẢ LỜI:\n"
                + "- Luôn trả lời bằng tiếng Việt, ngắn gọn, rõ ràng\n"
                + "- Tập trung vào 4 chủ đề chính: Trạng thái đơn hàng, Thông tin shipper, Công nợ, Vị trí giao hàng\n"
                + "- Sử dụng dữ liệu mẫu khi cần thiết\n"
                + "- Giải thích đơn giản các khái niệm liên quan\n"
                + "\n"
                + "### VÍ DỤ:\n"
                + "User: Trạng thái đơn hàng số 5\n"
                + "Assistant: Đơn hàng số 5 hiện đang ở trạng thái 'DELIVERING'. Shipper đang trên đường giao hàng.\n"
                + "\n"
                + "User: Thông tin shipper của đơn hàng 10\n"
                + "Assistant: Shipper phụ trách đơn hàng 10 là Nguyễn Văn A. Khu vực: Quận Sơn Trà, Mức ưu tiên: 1.\n"
                + "\n"
                + "User: Công nợ shipper 15\n"
                + "Assistant: Shipper 15 hiện có tổng công nợ 1,500,000 VND. Công nợ quá hạn: 500,000 VND (từ ngày 2023-10-01).\n"
                + "\n"
                + "User: Vị trí giao hàng đơn hàng 20\n"
                + "Assistant: Đơn hàng 20 sẽ giao đến: 123 Lê Duẩn, Đà Nẵng. Tọa độ: 16.0614, 108.2145.\n"
                + "\n"
                + "Hãy trả lời câu hỏi tiếp theo của người dùng dựa trên thông tin trên:";
    }

    private String callOllamaAPI(String fullPrompt) throws Exception {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", MODEL_NAME);
        requestJson.addProperty("prompt", fullPrompt);
        requestJson.addProperty("stream", false);
        requestJson.addProperty("temperature", 0.7);

        String requestBody = gson.toJson(requestJson);
        System.out.println("Ollama Request JSON:\n" + requestBody);

        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(30000);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                String aiResponse = extractTextFromResponse(response.toString());
                conversationHistory.add(new ChatMessage("model", aiResponse));
                return aiResponse;
            }
        } else {
            System.err.println("Ollama error code: " + connection.getResponseCode());
            return "🚫 AI không phản hồi. Vui lòng thử lại sau.";
        }
    }

    private String handleDatabaseQuery(String userMessage) {
        Matcher m;

        // 1. Truy vấn trạng thái đơn hàng
        m = Pattern.compile("(?i)(đơn hàng|trạng thái).*số\\s*(\\d+)").matcher(userMessage);
        if (m.find()) {
            int orderId = Integer.parseInt(m.group(2));
            return getOrderStatus(orderId);
        }

        // 2. Truy vấn thông tin shipper
        m = Pattern.compile("(?i)(shipper|tài xế).*?\\b(\\d+)\\b").matcher(userMessage);
        if (m.find()) {
            int shipperId = Integer.parseInt(m.group(2));
            return getShipperInfo(shipperId);
        }

        // 3. Truy vấn công nợ
        m = Pattern.compile("(?i)(công nợ|nợ).*(shipper|tài xế).*số\\s*(\\d+)").matcher(userMessage);
        if (m.find()) {
            int shipperId = Integer.parseInt(m.group(3));
            return getShipperDebt(shipperId);
        }

        // 4. Truy vấn vị trí giao hàng
        m = Pattern.compile("(?i)(vị trí|địa chỉ|tọa độ).*(đơn hàng|giao hàng).*số\\s*(\\d+)").matcher(userMessage);
        if (m.find()) {
            int orderId = Integer.parseInt(m.group(3));
            return getDeliveryLocation(orderId);
        }

        return null;
    }

    // ========== DATABASE QUERY METHODS ==========
    private String getOrderStatus(int orderId) {
        String sql = "SELECT o.status, o.appointment_time, d.status AS delivery_status "
                + "FROM orders o LEFT JOIN deliveries d ON o.id = d.order_id "
                + "WHERE o.id = ?";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String orderStatus = rs.getString("status");
                    String deliveryStatus = rs.getString("delivery_status");
                    Timestamp appointment = rs.getTimestamp("appointment_time");

                    return String.format("Đơn hàng %d:\n- Trạng thái: %s\n- Giao hàng: %s\n- Lịch hẹn: %s",
                            orderId,
                            translateStatus(orderStatus),
                            deliveryStatus != null ? translateDeliveryStatus(deliveryStatus) : "Chưa giao",
                            formatAppointment(appointment));
                }
                return "❌ Không tìm thấy đơn hàng " + orderId;
            }
        } catch (SQLException e) {
            return "⛔ Lỗi truy vấn đơn hàng: " + e.getMessage();
        }
    }

    private String getShipperInfo(int shipperId) {
    // Sửa câu truy vấn: thay s.id bằng s.user_id
    String sql = "SELECT u.full_name, s.area, s.priority_level, s.daily_income "
            + "FROM shippers s JOIN users u ON s.user_id = u.id "
            + "WHERE s.user_id = ?"; // Sửa tại đây

    try (Connection conn = dbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, shipperId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                // Sử dụng format tiền tệ Việt Nam
                NumberFormat vnFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
                String dailyIncome = vnFormat.format(rs.getDouble("daily_income"));
                
                return String.format("Shipper %d:\n- Tên: %s\n- Khu vực: %s\n- Mức ưu tiên: %d\n- Thu nhập/ngày: %s VND",
                        shipperId,
                        rs.getString("full_name"),
                        rs.getString("area"),
                        rs.getInt("priority_level"),
                        dailyIncome);
            }
            return "❌ Không tìm thấy shipper " + shipperId;
        }
    } catch (SQLException e) {
        return "⛔ Lỗi truy vấn shipper: " + e.getMessage();
    }
}

    private String getShipperDebt(int shipperId) {
          String sql = "SELECT SUM(amount) AS total_debt, "
            + "(SELECT SUM(amount) FROM shipper_debts WHERE shipper_id = ? AND payment_date IS NULL) AS overdue "
            + "FROM shipper_debts WHERE shipper_id = ?";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);
            stmt.setInt(2, shipperId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double totalDebt = rs.getDouble("total_debt");
                    double overdue = rs.getDouble("overdue");

                    return String.format("Công nợ shipper %d:\n- Tổng nợ: %,.0f VND\n- Quá hạn: %,.0f VND",
                            shipperId, totalDebt, overdue);
                }
                return "ℹ️ Shipper " + shipperId + " không có công nợ";
            }
        } catch (SQLException e) {
            return "⛔ Lỗi truy vấn công nợ: " + e.getMessage();
        }
    }

    private String getDeliveryLocation(int orderId) {
        String sql = "SELECT c.name, c.address, c.latitude, c.longitude "
                + "FROM orders o JOIN customers c ON o.customer_id = c.id "
                + "WHERE o.id = ?";

        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return String.format("Vị trí giao đơn hàng %d:\n- Khách hàng: %s\n- Địa chỉ: %s\n- Tọa độ: %s, %s",
                            orderId,
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("latitude"),
                            rs.getString("longitude"));
                }
                return "❌ Không tìm thấy đơn hàng " + orderId;
            }
        } catch (SQLException e) {
            return "⛔ Lỗi truy vấn vị trí: " + e.getMessage();
        }
    }

    // ========== HELPER METHODS ==========
    private String translateStatus(String status) {
        switch (status) {
            case "NEW":
                return "MỚI";
            case "DELIVERING":
                return "ĐANG GIAO";
            case "DELIVERED":
                return "ĐÃ GIAO";
            case "CANCELLED":
                return "ĐÃ HỦY";
            case "URGENT":
                return "KHẨN CẤP";
            case "APPOINTMENT":
                return "HẸN GIỜ";
            default:
                return status;
        }
    }

    private String translateDeliveryStatus(String status) {
        switch (status) {
            case "ASSIGNED":
                return "ĐÃ PHÂN CÔNG";
            case "IN_TRANSIT":
                return "ĐANG VẬN CHUYỂN";
            case "COMPLETED":
                return "HOÀN THÀNH";
            case "CANCELLED":
                return "ĐÃ HỦY";
            default:
                return status;
        }
    }

    private String formatAppointment(Timestamp ts) {
        return (ts != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ts) : "Chưa đặt lịch";
    }

    private String extractTextFromResponse(String jsonResponse) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            return jsonObject.get("response").getAsString().trim();
        } catch (Exception e) {
            System.out.println("❌ JSON parse error: " + e.getMessage());
            return "Không thể hiểu phản hồi từ AI.";
        }
    }

    private static class ChatMessage {

        private final String role;
        private final String text;

        public ChatMessage(String role, String text) {
            this.role = role;
            this.text = text;
        }
    }
}
