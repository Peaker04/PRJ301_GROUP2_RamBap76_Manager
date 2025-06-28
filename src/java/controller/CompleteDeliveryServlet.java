package controller;

import dao.DeliveryDAO;
import dao.TransactionDAO;
import model.Transaction;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.Delivery;

@WebServlet(name = "CompleteDeliveryServlet", urlPatterns = {"/shipper/complete-delivery"})
public class CompleteDeliveryServlet extends HttpServlet {

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
        String collectedAmountStr = request.getParameter("collectedAmount");
        
        if (deliveryIdStr == null || collectedAmountStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin cần thiết");
            return;
        }
        
        try {
            int deliveryId = Integer.parseInt(deliveryIdStr);
            BigDecimal collectedAmount = new BigDecimal(collectedAmountStr);
            
            DeliveryDAO deliveryDAO = new DeliveryDAO();
            TransactionDAO transactionDAO = new TransactionDAO();
            
            // Lấy thông tin đơn hàng
            Delivery delivery = deliveryDAO.getDeliveryById(deliveryId);
            if (delivery == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn hàng");
                return;
            }
            
            // Kiểm tra xem shipper có quyền hoàn thành đơn hàng này không
            if (delivery.getCurrentShipperId() != currentUser.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền hoàn thành đơn hàng này");
                return;
            }
            
            LocalDateTime actualDeliveryTime = LocalDateTime.now();
            
            // Hoàn thành đơn hàng
            boolean deliverySuccess = deliveryDAO.completeDelivery(
                deliveryId, actualDeliveryTime, collectedAmount, currentUser.getId());
            
            if (deliverySuccess) {
                // Cập nhật trạng thái đơn hàng
                deliveryDAO.updateOrderStatus(delivery.getOrderId(), "DELIVERED");
                
                // Ghi nhận tiền thu hộ
                Transaction collectTransaction = new Transaction();
                collectTransaction.setShipperId(currentUser.getId());
                collectTransaction.setType("COLLECT");
                collectTransaction.setAmount(collectedAmount.doubleValue());
                collectTransaction.setDescription("Thu hộ đơn hàng #" + delivery.getOrderId());
                transactionDAO.createTransaction(collectTransaction);
                
                // Ghi nhận phí giao hàng
                Transaction deliveryFeeTransaction = new Transaction();
                deliveryFeeTransaction.setShipperId(currentUser.getId());
                deliveryFeeTransaction.setType("COLLECT");
                deliveryFeeTransaction.setAmount(delivery.getDeliveryFee().doubleValue());
                deliveryFeeTransaction.setDescription("Phí giao hàng đơn #" + delivery.getOrderId());
                transactionDAO.createTransaction(deliveryFeeTransaction);
                
                // Ghi nhận phí hộp
                Transaction boxFeeTransaction = new Transaction();
                boxFeeTransaction.setShipperId(currentUser.getId());
                boxFeeTransaction.setType("COLLECT");
                boxFeeTransaction.setAmount(delivery.getBoxFee().doubleValue());
                boxFeeTransaction.setDescription("Phí hộp đơn hàng #" + delivery.getOrderId());
                transactionDAO.createTransaction(boxFeeTransaction);
                
                // Cập nhật thu nhập hàng ngày
                BigDecimal currentIncome = deliveryDAO.getDailyIncome(currentUser.getId());
                BigDecimal newIncome = currentIncome.add(collectedAmount)
                    .add(delivery.getDeliveryFee())
                    .add(delivery.getBoxFee());
                deliveryDAO.updateDailyIncome(currentUser.getId(), newIncome);
                
                response.sendRedirect(request.getContextPath() + "/shipper/dashboard?success=2");
            } else {
                response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=2");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/shipper/dashboard?error=2");
        }
    }
} 