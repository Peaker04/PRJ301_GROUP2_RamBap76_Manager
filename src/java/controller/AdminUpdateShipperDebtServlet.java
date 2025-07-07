package controller;

import dao.ShipperDebtDAO;
import model.ShipperDebt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import model.User;

@WebServlet("/admin/update-shipper-debt")
public class AdminUpdateShipperDebtServlet extends HttpServlet {

    private final ShipperDebtDAO debtDAO = new ShipperDebtDAO();

    // Hiển thị form cập nhật công nợ
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String shipperIdStr = req.getParameter("shipperId");
            String dateStr = req.getParameter("date");

            if (shipperIdStr == null || shipperIdStr.isEmpty() || dateStr == null || dateStr.isEmpty()) {
                throw new ServletException("Thiếu tham số shipperId hoặc date!");
            }

            int shipperId = Integer.parseInt(shipperIdStr);
            java.sql.Date debtDate = java.sql.Date.valueOf(dateStr); // yyyy-MM-dd

            ShipperDebt debt = debtDAO.getDebtByShipperIdAndDate(shipperId, debtDate);

            if (debt == null) {
                throw new ServletException("Không tìm thấy công nợ của shipper!");
            }

            req.setAttribute("debt", debt);
            req.getRequestDispatcher("/view/admin/update-shipper-debt.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi lấy thông tin công nợ shipper", e);
        }
    }

    // Xử lý cập nhật công nợ
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int shipperId = Integer.parseInt(req.getParameter("shipperId"));
            String dateStr = req.getParameter("date");
            double newAmount = Double.parseDouble(req.getParameter("amount"));
            String reason = req.getParameter("reason"); // Nếu muốn lưu lý do, cần thêm cột reason vào DB

            java.sql.Date debtDate = java.sql.Date.valueOf(dateStr);

            // Cập nhật số tiền công nợ
            // Lấy adminId từ session (giả sử bạn lưu user đăng nhập trong session)
            int adminId = ((User) req.getSession().getAttribute("user")).getId();
            debtDAO.updateDebtWithHistory(shipperId, debtDate, newAmount, reason, adminId);

            // Sau khi cập nhật, redirect về danh sách công nợ chưa thanh toán
            resp.sendRedirect("shipper-debt");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi cập nhật công nợ shipper", e);
        }
    }
}
