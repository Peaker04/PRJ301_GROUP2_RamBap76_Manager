package controller;

import dao.ShipperDebtDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/admin/pay-shipper-debt-reason")
public class AdminPayDebtReasonServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int shipperId = Integer.parseInt(req.getParameter("shipperId"));
        String date = req.getParameter("date");

        // Lấy tên shipper từ DB
        String shipperName = new ShipperDebtDAO().getShipperNameById(shipperId);

        // Truyền sang JSP
        req.setAttribute("shipperId", shipperId);
        req.setAttribute("date", date);
        req.setAttribute("shipperName", shipperName);

        req.getRequestDispatcher("/view/admin/pay-debt-reason.jsp").forward(req, resp);
    }
}