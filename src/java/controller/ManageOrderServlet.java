package controller;

import connect.DBConnection;
import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Order;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "ManageOrderServlet", urlPatterns = {"/admin/orders"})
public class ManageOrderServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        if (sort == null || (!sort.equals("asc") && !sort.equals("desc"))) sort = "desc";
        
        try (Connection conn = DBConnection.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            List<Order> orders = orderDAO.getOrders(status, search, sort);
            
            List<Order> allOrders = orderDAO.getOrders(null, search, sort);

            Map<String, Integer> statusCounts = new HashMap<>();
            statusCounts.put("ALL", allOrders.size());
            for (String s : List.of("NEW", "DELIVERING", "DELIVERED", "URGENT", "APPOINTMENT")) {
                int count = (int) allOrders.stream().filter(o -> s.equals(o.getStatus())).count();
                statusCounts.put(s, count);
            }

            request.setAttribute("orders", orders);
            request.setAttribute("status", status);
            request.setAttribute("statusCounts", statusCounts);
            request.setAttribute("contentPage", "/view/admin/order_list.jsp");
            request.getRequestDispatcher("/view/common/admin_layout.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

