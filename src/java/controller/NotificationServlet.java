package controller;

import dao.NotificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Notification;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/shipper/notifications")
public class NotificationServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer shipperId = (Integer) session.getAttribute("shipper_id");
        
        if (shipperId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            NotificationDAO notificationDAO = new NotificationDAO();
            // Lấy danh sách thông báo chưa đọc, logic này đã đúng
            List<Notification> notifications = notificationDAO.getUnreadNotifications(shipperId);
            
            request.setAttribute("notifications", notifications);
            
            // Forward tới trang hiển thị
            request.setAttribute("contentPage", "/view/shipper/notifications.jsp");
            request.getRequestDispatcher("/view/common/shipper_layout.jsp").forward(request, response);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ServletException("Lỗi khi truy xuất thông báo", ex);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String notificationIdParam = request.getParameter("notification_id");
        if (notificationIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/shipper/notifications");
            return;
        }

        int notificationId = Integer.parseInt(notificationIdParam);
        
        try {
            NotificationDAO notificationDAO = new NotificationDAO();
            // Đánh dấu thông báo đã đọc, logic này đã đúng
            boolean success = notificationDAO.markAsRead(notificationId);
            
            if (success) {
                request.getSession().setAttribute("message", "Thông báo đã được đánh dấu là đã đọc.");
            } else {
                request.getSession().setAttribute("error", "Không thể đánh dấu thông báo.");
            }
            
            response.sendRedirect(request.getContextPath() + "/shipper/notifications");
            
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            throw new ServletException("Lỗi khi cập nhật trạng thái thông báo", ex);
        }
    }
}