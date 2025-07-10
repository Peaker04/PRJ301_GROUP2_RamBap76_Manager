package controller;

import dao.ShipperDebtDAO;
import model.ShipperDebt;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/shipper-debt")
public class AdminShipperDebtServlet extends HttpServlet {
    private final ShipperDebtDAO debtDAO = new ShipperDebtDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ShipperDebt> debts = debtDAO.getUnpaidDebts();
            
             // Tính toán trạng thái quá hạn
            for (ShipperDebt debt : debts) {
                if (debt.getPaymentDate() == null) {
                    // Nếu công nợ chưa thanh toán, kiểm tra xem có quá hạn hay không
                    Date currentDate = new Date(System.currentTimeMillis());
                    if (debt.getDate().before(currentDate)) {
                        debt.setOverdue(true);  // Đánh dấu quá hạn
                    } else {
                        debt.setOverdue(false); // Nếu chưa quá hạn
                    }
                }
            }
            
            req.setAttribute("debts", debts);
            req.getRequestDispatcher("/view/admin/shipper-debt.jsp").forward(req, resp);
       } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi lấy danh sách công nợ shipper", e);
        }
    }
}