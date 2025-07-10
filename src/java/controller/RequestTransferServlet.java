package controller;

import dao.DeliveryDAO;
import dao.DeliveryTransferDAO;
import dao.NotificationDAO;
import dao.ShipperDAO;
import model.Delivery;
import model.DeliveryTransfer;
import model.Notification;
import model.Shipper;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RequestTransferServlet", urlPatterns = {"/shipper/request-transfer"})
public class RequestTransferServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"SHIPPER".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String deliveryIdStr = request.getParameter("deliveryId");
        
        if (deliveryIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID đơn hàng");
            return;
        }
        
        try {
            int deliveryId = Integer.parseInt(deliveryIdStr);
            
            DeliveryDAO deliveryDAO = new DeliveryDAO();
            ShipperDAO shipperDAO = new ShipperDAO();
            
            // Lấy thông tin đơn hàng
            Delivery delivery = deliveryDAO.getDeliveryById(deliveryId);
            if (delivery == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
                return;
            }
            
            // Kiểm tra quyền
            if (delivery.getCurrentShipperId() != currentUser.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền chuyển giao đơn hàng này");
                return;
            }
            
            // Lấy danh sách shipper khác để chuyển giao
            List<Shipper> availableShippers = shipperDAO.getAllShippersExcept(currentUser.getId());
            System.out.println("DEBUG: Số lượng shippers: " + availableShippers.size());
            for (Shipper s : availableShippers) {
                System.out.println("DEBUG: Shipper (userId): " + s.getUserId() + ", Name: " + s.getName());
            }
            request.setAttribute("delivery", delivery);
            request.setAttribute("availableShippers", availableShippers);
            request.getRequestDispatcher("/view/shipper/request-transfer.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đơn hàng không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=3");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null || !"SHIPPER".equals(currentUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String deliveryIdStr = request.getParameter("deliveryId");
        String toShipperIdStr = request.getParameter("toShipperId");
        String reason = request.getParameter("reason");
        
        if (deliveryIdStr == null || toShipperIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin cần thiết");
            return;
        }
        
        try {
            int deliveryId = Integer.parseInt(deliveryIdStr);
            int toShipperId = Integer.parseInt(toShipperIdStr);
            
            DeliveryTransferDAO transferDAO = new DeliveryTransferDAO();
            NotificationDAO notificationDAO = new NotificationDAO();
            
            // Tạo yêu cầu chuyển giao
            DeliveryTransfer transfer = new DeliveryTransfer(deliveryId, currentUser.getId(), toShipperId, reason);
            boolean transferSuccess = transferDAO.createTransfer(transfer);
            
            if (transferSuccess) {
                // Tạo thông báo cho shipper được yêu cầu
                Notification notification = new Notification();
                notification.setShipperId(toShipperId);
                notification.setMessage("Bạn có yêu cầu chuyển giao đơn hàng #" + deliveryId + " từ " + currentUser.getFullName());
                notification.setType("TRANSFER");
                notificationDAO.createNotification(notification);
                
                response.sendRedirect(request.getContextPath() + "/shipper/dashboard?success=3");
            } else {
                response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=3");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=3");
        }
    }
} 