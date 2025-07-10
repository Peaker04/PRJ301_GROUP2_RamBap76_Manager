package controller;

import connect.DBConnection;
import dao.StationReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import model.StationReceipt;

@WebServlet(name = "ManageReceiptServlet", urlPatterns = {"/admin/receipts"})
public class ManageReceiptServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String sort = req.getParameter("sort");
        if (sort == null) sort = "desc";

        int size = 10;
        int page = 1;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            if (page < 1) page = 1;
        } catch (Exception ex) {
            page = 1;
        }

        try (Connection conn = DBConnection.getConnection()) {
            StationReceiptDAO dao = new StationReceiptDAO(conn);

            int totalReceipts = dao.countReceipts(search);
            int totalPages = (int)Math.ceil((double)totalReceipts / size);
            if (page > totalPages && totalPages > 0) page = totalPages;

            List<StationReceipt> receipts = dao.getReceiptsByPage(search, sort, page, size);

            req.setAttribute("receipts", receipts);
            req.setAttribute("search", search);
            req.setAttribute("sort", sort);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalReceipts", totalReceipts);
            req.setAttribute("contentPage", "/view/admin/receipt_list.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
            if ("multiDelete".equals(action)) {
                String ids = request.getParameter("ids");
                if (ids != null && !ids.trim().isEmpty()) {
                    String[] idArr = ids.split(",");
                    try (Connection conn = DBConnection.getConnection()) {
                        StationReceiptDAO receiptDAO = new StationReceiptDAO(conn);
                        for (String idStr : idArr) {
                            try {
                                int receipt = Integer.parseInt(idStr.trim());
                                receiptDAO.deleteReceipt(receipt);
                            } catch (NumberFormatException ignore) {}
                        }
                    } catch (Exception e) {
                        throw new ServletException(e);
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/receipts");
                return;
            }
        doGet(request, response);
    }
}
