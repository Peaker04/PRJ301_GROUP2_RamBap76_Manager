package controller;

import connect.DBConnection;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Map;
import model.Customer;
import com.google.gson.Gson;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
            HttpSession session = req.getSession(false);
            
            try (Connection conn = DBConnection.getConnection()) {
                OrderDAO orderDAO = new OrderDAO(conn);
                ProductDAO productDAO = new ProductDAO(conn);
                CustomerDAO customerDAO = new CustomerDAO(conn);
                Gson gson = new Gson();

                Map<String, Integer> orderStats = orderDAO.getOrderCountByMonth(4);
                List<String> ordersByMonthLabels = new java.util.ArrayList<>(orderStats.keySet());
                List<Integer> ordersByMonthData = new java.util.ArrayList<>(orderStats.values());
                
                List<Customer> topUsers = customerDAO.getTopUsersByOrders(10);
                req.setAttribute("topUsers", topUsers);

                Map<String, Integer> soldThisMonth = productDAO.getSoldProductsThisMonth();
                List<String> soldLabels = new java.util.ArrayList<>(soldThisMonth.keySet());
                List<Integer> soldData = new java.util.ArrayList<>(soldThisMonth.values());
                
                Map<String, Integer> totalStock = productDAO.getCurrentStockAllProducts();
                List<String> invLabels = new java.util.ArrayList<>(totalStock.keySet());
                List<Integer> invData = new java.util.ArrayList<>(totalStock.values());
                
                String username = session != null && session.getAttribute("username") != null
                                    ? (String)session.getAttribute("username")
                                    : "Admin";
                req.setAttribute("username", username);
                
                req.setAttribute("ordersByMonthLabelsJson", gson.toJson(ordersByMonthLabels));
                req.setAttribute("ordersByMonthDataJson", gson.toJson(ordersByMonthData));
                req.setAttribute("soldLabelsJson", gson.toJson(soldLabels));
                req.setAttribute("soldDataJson", gson.toJson(soldData));
                req.setAttribute("invLabelsJson", gson.toJson(invLabels));
                req.setAttribute("invDataJson", gson.toJson(invData));
                
                req.setAttribute("contentPage", "/view/admin/dashboard.jsp");
                req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
            } catch (Exception ex) {
                throw new ServletException(ex);
        }
    }
}
