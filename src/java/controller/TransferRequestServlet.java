package controller;

import dao.DeliveryTransferDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.DeliveryTransfer;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/shipper/transfer-request")
public class TransferRequestServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer fromShipperId = (Integer) session.getAttribute("shipper_id");
        
        if (fromShipperId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        int deliveryId = Integer.parseInt(request.getParameter("delivery_id"));
        int toShipperId = Integer.parseInt(request.getParameter("to_shipper_id"));
        String reason = request.getParameter("reason");
        
        try {
            DeliveryTransfer transfer = new DeliveryTransfer();
            transfer.setDeliveryId(deliveryId);
            transfer.setFromShipperId(fromShipperId);
            transfer.setToShipperId(toShipperId);
            transfer.setReason(reason);
            
            DeliveryTransferDAO transferDAO = new DeliveryTransferDAO();
            boolean success = transferDAO.createTransfer(transfer);
            
            if (success) {
                session.setAttribute("message", "Transfer request sent successfully");
            } else {
                session.setAttribute("error", "Failed to send transfer request");
            }
            
            response.sendRedirect(request.getContextPath() + "/shipper/delivery-detail?id=" + deliveryId);
            
        } catch (SQLException | NumberFormatException ex) {
            throw new ServletException("Error creating transfer request", ex);
        }
    }
}