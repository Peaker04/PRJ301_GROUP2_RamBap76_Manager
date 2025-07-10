package controller;

import dao.ShipperDebtDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/delete-shipper-debt")
public class AdminDeleteShipperDebtServlet extends HttpServlet {
    private final ShipperDebtDAO debtDAO = new ShipperDebtDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy ID công nợ từ tham số request
            int debtId = Integer.parseInt(req.getParameter("debtId"));
            
            // Xóa công nợ khỏi cơ sở dữ liệu
            debtDAO.deleteDebt(debtId);

            // Chuyển hướng về trang danh sách công nợ
            resp.sendRedirect("shipper-debt");

        } catch (SQLException e) {
            // In thông báo lỗi nếu có sự cố khi xóa công nợ
            e.printStackTrace();
            throw new ServletException("Lỗi khi xóa công nợ shipper", e);
        } catch (NumberFormatException e) {
            // Xử lý lỗi nếu không thể chuyển đổi debtId thành số nguyên
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID công nợ không hợp lệ");
        }
    }
}
