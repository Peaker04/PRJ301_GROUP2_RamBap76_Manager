package controller;

import dao.ShipperDebtDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import model.User;

@WebServlet("/admin/pay-shipper-debt")
public class AdminPayDebtServlet extends HttpServlet {

    private final ShipperDebtDAO debtDAO = new ShipperDebtDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy tham số từ form
            int shipperId = Integer.parseInt(req.getParameter("shipperId"));
            String dateStr = req.getParameter("date");
            String reason = req.getParameter("reason");

            // Chuyển đổi ngày về java.sql.Date
            Date sqlDate = Date.valueOf(dateStr);

            // Lấy adminId từ session
            User admin = (User) req.getSession().getAttribute("user");
            int adminId = admin.getId();

            // Gọi DAO: vừa cập nhật trạng thái vừa ghi lịch sử
            debtDAO.payDebtWithHistory(shipperId, sqlDate, reason, adminId);

            // Redirect về trang danh sách công nợ
            resp.sendRedirect("shipper-debt");

        } catch (Exception e) {
            req.setAttribute("message", "Có lỗi khi thanh toán công nợ: " + e.getMessage());
            req.getRequestDispatcher("/view/admin/pay-result.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Không cho truy cập GET trực tiếp, chuyển về danh sách công nợ
        resp.sendRedirect("shipper-debt");
    }
}