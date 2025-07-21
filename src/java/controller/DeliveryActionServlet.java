package controller;

import dao.DeliveryDAO;
import dao.TransactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import model.Transaction;

@WebServlet("/shipper/delivery-action")
public class DeliveryActionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Integer shipperId = (Integer) session.getAttribute("shipper_id");

        if (shipperId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        // Đảm bảo các tham số không null trước khi parse
        if (action == null || request.getParameter("delivery_id") == null) {
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard");
            return;
        }
        
        int deliveryId = Integer.parseInt(request.getParameter("delivery_id"));

        try {
            DeliveryDAO deliveryDAO = new DeliveryDAO();

            if ("accept".equals(action)) {
                // SỬA ĐỔI QUAN TRỌNG: Gọi phương thức acceptDelivery để tối ưu hóa
                // Phương thức này thực hiện 2 việc: cập nhật status và accepted_time trong 1 lần gọi DB
                boolean success = deliveryDAO.acceptDelivery(deliveryId, shipperId);
                if (success) {
                    session.setAttribute("message", "Đã nhận đơn hàng thành công!");
                } else {
                    session.setAttribute("error", "Không thể nhận đơn hàng. Đơn hàng có thể đã được giao cho người khác.");
                }

            } else if ("complete".equals(action)) {
                double collectedAmount = Double.parseDouble(request.getParameter("collected_amount"));

                // Logic hoàn thành đơn hàng đã đúng
                boolean success = deliveryDAO.completeDelivery(deliveryId, collectedAmount);

                if (success) {
                    // Tạo một giao dịch ghi nhận số tiền đã thu
                    Transaction transaction = new Transaction();
                    transaction.setShipperId(shipperId);
                    transaction.setType("COLLECT");
                    transaction.setAmount(collectedAmount);
                    transaction.setDescription("Thu tiền đơn hàng #" + deliveryId);

                    TransactionDAO transactionDAO = new TransactionDAO(); // Giả sử đã có TransactionDAO
                    transactionDAO.createTransaction(transaction);
                    
                    session.setAttribute("message", "Đã hoàn thành đơn hàng #" + deliveryId);
                } else {
                    session.setAttribute("error", "Không thể hoàn thành đơn hàng.");
                }
            }

            response.sendRedirect(request.getContextPath() + "/shipper/dashboard");

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace(); // Ghi lại lỗi
            throw new ServletException("Lỗi khi xử lý hành động giao hàng", ex);
        }
    }
}