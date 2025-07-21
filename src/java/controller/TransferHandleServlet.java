package controller;

import dao.DeliveryTransferDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/shipper/transfer-handle")
public class TransferHandleServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer shipperId = (Integer) session.getAttribute("shipper_id");
        
        if (shipperId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Kiểm tra các tham số đầu vào
        String action = request.getParameter("action");
        String transferIdParam = request.getParameter("transfer_id");
        
        if (action == null || transferIdParam == null) {
            session.setAttribute("error", "Yêu cầu không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard");
            return;
        }

        int transferId = Integer.parseInt(transferIdParam);
        
        try {
            DeliveryTransferDAO transferDAO = new DeliveryTransferDAO();
            boolean success = false;
            String message = "";
            String error = "";

            if ("accept".equals(action)) {
                // Gọi phương thức acceptTransfer, phương thức này sẽ gọi stored procedure sp_AcceptTransfer
                // để đảm bảo tính toàn vẹn dữ liệu (cập nhật bảng transfers và deliveries)
                success = transferDAO.acceptTransfer(transferId);
                if (success) {
                    message = "Chấp nhận chuyển giao đơn hàng thành công!";
                } else {
                    error = "Không thể chấp nhận chuyển giao. Yêu cầu có thể đã hết hạn hoặc bị hủy.";
                }

            } else if ("reject".equals(action)) {
                String reason = request.getParameter("reason"); // Lý do từ chối
                success = transferDAO.rejectTransfer(transferId, reason);
                if (success) {
                    message = "Đã từ chối yêu cầu chuyển giao.";
                } else {
                    error = "Không thể từ chối yêu cầu.";
                }
            }
            
            if (success) {
                session.setAttribute("message", message);
            } else {
                session.setAttribute("error", error);
            }
            
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard");
            
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            throw new ServletException("Lỗi khi xử lý yêu cầu chuyển giao", ex);
        }
    }
}