package controller;

import dao.DeliveryDAO;
import dao.DeliveryTransferDAO;
import dao.NotificationDAO;
import model.DeliveryTransfer;
import model.Notification;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "HandleTransferServlet", urlPatterns = {"/shipper/handle-transfer"})
public class HandleTransferServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"SHIPPER".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String transferIdStr = request.getParameter("transferId");
        String action = request.getParameter("action"); // "accept" or "reject"
        
        if (transferIdStr == null || action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin cần thiết");
            return;
        }
        
        try {
            int transferId = Integer.parseInt(transferIdStr);
            
            DeliveryTransferDAO transferDAO = new DeliveryTransferDAO();
            DeliveryDAO deliveryDAO = new DeliveryDAO();
            NotificationDAO notificationDAO = new NotificationDAO();
            
            // Lấy thông tin yêu cầu chuyển giao
            DeliveryTransfer transfer = transferDAO.getTransferById(transferId);
            if (transfer == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy yêu cầu chuyển giao");
                return;
            }
            
            // Kiểm tra quyền
            if (transfer.getToShipperId() != currentUser.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xử lý yêu cầu này");
                return;
            }
            
            LocalDateTime acceptedTime = null;
            String newStatus = "";
            
            if ("accept".equals(action)) {
                newStatus = "ACCEPTED";
                acceptedTime = LocalDateTime.now();
                
                // Cập nhật current_shipper_id trong bảng deliveries
                deliveryDAO.updateCurrentShipper(transfer.getDeliveryId(), currentUser.getId());
                
                // Tạo thông báo cho shipper gửi yêu cầu
                Notification notification = new Notification();
                notification.setShipperId(transfer.getFromShipperId());
                notification.setMessage("Yêu cầu chuyển giao đơn hàng #" + transfer.getDeliveryId() + " đã được chấp nhận bởi " + currentUser.getFullName());
                notification.setType("TRANSFER");
                notificationDAO.createNotification(notification);
                
            } else if ("reject".equals(action)) {
                newStatus = "REJECTED";
                
                // Tạo thông báo cho shipper gửi yêu cầu
                Notification notification = new Notification();
                notification.setShipperId(transfer.getFromShipperId());
                notification.setMessage("Yêu cầu chuyển giao đơn hàng #" + transfer.getDeliveryId() + " đã bị từ chối bởi " + currentUser.getFullName());
                notification.setType("TRANSFER");
                notificationDAO.createNotification(notification);
                
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ");
                return;
            }
            
            // Cập nhật trạng thái yêu cầu chuyển giao
            boolean success = transferDAO.updateTransferStatus(transferId, newStatus, acceptedTime);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shipper/notifications?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/shipper/notifications?error=1");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID yêu cầu không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/shipper/notifications?error=1");
        }
    }
} 