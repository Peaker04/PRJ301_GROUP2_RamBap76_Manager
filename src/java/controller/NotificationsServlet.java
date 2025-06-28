package controller;

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
import java.util.List;

@WebServlet(name = "NotificationsServlet", urlPatterns = {"/shipper/notifications"})
public class NotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"SHIPPER".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        NotificationDAO notificationDAO = new NotificationDAO();
        DeliveryTransferDAO transferDAO = new DeliveryTransferDAO();
        
        try {
            // Lấy tất cả thông báo
            List<Notification> notifications = notificationDAO.getNotificationsByShipper(currentUser.getId());
            
            // Lấy yêu cầu chuyển giao đang chờ
            List<DeliveryTransfer> pendingTransfers = transferDAO.getPendingTransfersForShipper(currentUser.getId());
            
            // Đánh dấu tất cả thông báo là đã đọc
            notificationDAO.markAllAsRead(currentUser.getId());
            
            request.setAttribute("notifications", notifications);
            request.setAttribute("pendingTransfers", pendingTransfers);
            request.setAttribute("currentUser", currentUser);
            
            request.getRequestDispatcher("/view/shipper/notifications.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải thông báo");
            request.getRequestDispatcher("/view/shipper/notifications.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 