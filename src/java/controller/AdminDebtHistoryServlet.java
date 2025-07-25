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
            String shipperIdParam = req.getParameter("shipperId");
            String dateParam = req.getParameter("date");

            java.sql.Date date;
            if (dateParam == null || dateParam.isEmpty()) {
                // Mặc định: ngày hiện tại
                date = new java.sql.Date(System.currentTimeMillis());
            } else {
                date = java.sql.Date.valueOf(dateParam);  // có thể ném IllegalArgumentException
            }

            List<DebtHistory> historyList;

            if (shipperIdParam != null && !shipperIdParam.isEmpty()) {
                int shipperId = Integer.parseInt(shipperIdParam);
                historyList = debtDAO.getDebtHistory(shipperId, date);
            } else {
                historyList = debtDAO.getDebtHistory(date); // overloaded method
            }

            req.setAttribute("historyList", historyList);
//            req.setAttribute("contentPage", "/view/admin/debt-history.jsp");
//            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi lấy lịch sử công nợ", e);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tham số shipperId không hợp lệ");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng ngày không hợp lệ (yyyy-MM-dd)");
        }
    }
}
