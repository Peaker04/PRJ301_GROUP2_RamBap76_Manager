package controller;

import dao.DeliveryDAO;
import dao.NotificationDAO;
import dao.ShipperDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Delivery;
import model.Notification;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.User;

@WebServlet("/shipper/dashboard")
public class ShipperDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Không tạo session mới nếu chưa tồn tại

        // Kiểm tra session và shipper_id một cách an toàn
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        Integer shipperId = (Integer) session.getAttribute("shipper_id");

        if ("SHIPPER".equals(user.getRole()) && shipperId == null) {
            try {
                shipperId = ShipperDAO.getShipperIdByUserId(user.getId());
                if (shipperId != null) {
                    session.setAttribute("shipper_id", shipperId);
                } else {
                    // Xử lý khi không tìm thấy shipper
                    request.setAttribute("error", "Shipper information not found");
                    request.getRequestDispatcher("/view/authentication/access_denied.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("error", "Database error");
                request.getRequestDispatcher("/view/authentication/access_denied.jsp").forward(request, response);
                return;
            }
        }

        try {
            DeliveryDAO deliveryDAO = new DeliveryDAO();
            NotificationDAO notificationDAO = new NotificationDAO();

            // Lấy danh sách các đơn hàng đã được giao cho shipper này
            List<Delivery> assignedDeliveries = deliveryDAO.getDeliveriesByShipper(shipperId, "ASSIGNED");

            // Lấy danh sách các đơn hàng shipper đang vận chuyển
            List<Delivery> inTransitDeliveries = deliveryDAO.getDeliveriesByShipper(shipperId, "IN_TRANSIT");

            // Lấy danh sách các thông báo chưa đọc
            List<Notification> notifications = notificationDAO.getUnreadNotifications(shipperId);

            // Đặt các thuộc tính vào request để hiển thị trên JSP
            request.setAttribute("assignedDeliveries", assignedDeliveries);
            request.setAttribute("inTransitDeliveries", inTransitDeliveries);
            request.setAttribute("notifications", notifications);

            // Chuyển hướng đến layout chung và nạp trang dashboard
            request.setAttribute("contentPage", "/view/shipper/dashboard.jsp");
            request.getRequestDispatcher("/view/common/shipper_layout.jsp").forward(request, response);

        } catch (SQLException e) {
            // Ghi lại lỗi và hiển thị trang lỗi
            e.printStackTrace(); // Nên sử dụng logger trong ứng dụng thực tế
            request.setAttribute("error", "Lỗi cơ sở dữ liệu khi tải trang dashboard.");
            throw new ServletException("Database error in ShipperDashboardServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
