package controller;

import dao.DeliveryDAO;
import dao.NotificationDAO;
import model.Delivery;
import model.Notification;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "ShipperDashboardServlet", urlPatterns = {"/shipper/dashboard"})
public class ShipperDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"SHIPPER".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        DeliveryDAO deliveryDAO = new DeliveryDAO();
        NotificationDAO notificationDAO = new NotificationDAO();
        
        try {
            // Lấy danh sách đơn hàng được gán
            List<Delivery> assignedDeliveries = deliveryDAO.getDeliveriesByShipperAndStatus(
                currentUser.getId(), "ASSIGNED");
            
            // Lấy danh sách đơn hàng đang giao
            List<Delivery> inTransitDeliveries = deliveryDAO.getDeliveriesByShipperAndStatus(
                currentUser.getId(), "IN_TRANSIT");
            
            // Lấy thu nhập trong ngày
            BigDecimal dailyIncome = deliveryDAO.getDailyIncome(currentUser.getId());
            
            // Lấy số thông báo chưa đọc
            int unreadCount = notificationDAO.getUnreadCount(currentUser.getId());
            
            // Set attributes
            request.setAttribute("assignedDeliveries", assignedDeliveries);
            request.setAttribute("inTransitDeliveries", inTransitDeliveries);
            request.setAttribute("dailyIncome", dailyIncome);
            request.setAttribute("unreadCount", unreadCount);
            request.setAttribute("currentUser", currentUser);
            
            // Forward to JSP
            request.getRequestDispatcher("/view/shipper/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải dashboard");
            request.getRequestDispatcher("/view/shipper/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 