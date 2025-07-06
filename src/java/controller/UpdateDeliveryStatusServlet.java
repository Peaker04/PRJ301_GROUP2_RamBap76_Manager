package controller;

import dao.DeliveryDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "UpdateDeliveryStatusServlet", urlPatterns = {"/shipper/update-delivery-status"})
public class UpdateDeliveryStatusServlet extends HttpServlet {

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
        String newStatus = request.getParameter("status");
        
        if (deliveryIdStr == null || newStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin cần thiết");
            return;
        }
        
        try {
            int deliveryId = Integer.parseInt(deliveryIdStr);
            DeliveryDAO deliveryDAO = new DeliveryDAO();
            
            LocalDateTime acceptedTime = null;
            if ("IN_TRANSIT".equals(newStatus)) {
                acceptedTime = LocalDateTime.now();
            }
            
            boolean success = deliveryDAO.updateDeliveryStatus(deliveryId, newStatus, acceptedTime);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/shipper/dashboard?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=1");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID đơn hàng không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=1");
        }
    }
} 