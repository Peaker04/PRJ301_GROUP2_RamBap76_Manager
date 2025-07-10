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
        
        int size = 10;
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
            if (page < 1) page = 1;
        } catch (Exception ex) {
            page = 1;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            
            int totalOrders = orderDAO.countOrders(status, search);
            int totalPages = (int)Math.ceil((double)totalOrders / size);
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            List<Order> orders = orderDAO.getOrdersByPage(status, search, sort, page, size);
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
            request.setAttribute("page", page);
            request.setAttribute("size", size);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("sort", sort);
            request.setAttribute("search", search);
            request.setAttribute("contentPage", "/view/admin/order_list.jsp");
            request.getRequestDispatcher("/view/common/admin_layout.jsp").forward(request, response);
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
                        OrderDAO orderDAO = new OrderDAO(conn);
                        for (String idStr : idArr) {
                            try {
                                int orderId = Integer.parseInt(idStr.trim());
                                orderDAO.deleteOrder(orderId);
                            } catch (NumberFormatException ignore) {}
                        }
                    } catch (Exception e) {
                        throw new ServletException(e);
                    }
                }
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }
        doGet(request, response);
    }
}

