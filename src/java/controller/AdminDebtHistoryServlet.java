package controller;

import dao.ShipperDebtDAO;
import model.DebtHistory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/debt-history")
public class AdminDebtHistoryServlet extends HttpServlet {

    private final ShipperDebtDAO debtDAO = new ShipperDebtDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int shipperId = Integer.parseInt(req.getParameter("shipperId"));
            java.sql.Date date = java.sql.Date.valueOf(req.getParameter("date"));

            List<DebtHistory> historyList = debtDAO.getDebtHistory(shipperId, date);

            req.setAttribute("historyList", historyList);
            req.getRequestDispatcher("/view/admin/debt-history.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi lấy lịch sử thay đổi công nợ", e);
        }
    }
}
