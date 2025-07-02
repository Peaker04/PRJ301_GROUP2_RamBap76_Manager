package controller;

import connect.DBConnection;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.CustomerDAO;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "OrderDetailServlet", urlPatterns = {"/admin/orders/detail"})
public class OrderDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("id"));
        try (Connection conn = DBConnection.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            CustomerDAO customerDAO = new CustomerDAO(conn);

            Order order = orderDAO.getOrderById(orderId);
            List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
            List<Product> products = productDAO.getAllProducts();
            List<Customer> customers = customerDAO.getAllCustomers();

            req.setAttribute("order", order);
            req.setAttribute("orderDetails", orderDetails);
            req.setAttribute("products", products);
            req.setAttribute("customers", customers);
            req.setAttribute("contentPage", "/view/admin/order_detail.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
        throws ServletException, IOException {
        String action = req.getParameter("action");
        try (Connection conn = DBConnection.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);

            if ("delete".equals(action)) {
                int orderId = Integer.parseInt(req.getParameter("id"));
                orderDAO.deleteOrder(orderId);
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
                return;
            }

            int orderId = Integer.parseInt(req.getParameter("id"));
            int customerId = Integer.parseInt(req.getParameter("customer_id"));
            String status = req.getParameter("status");
            String notes = req.getParameter("notes");
            java.sql.Timestamp appointmentTime = null;
            if (req.getParameter("appointment_time") != null && !req.getParameter("appointment_time").isEmpty())
                appointmentTime = java.sql.Timestamp.valueOf(req.getParameter("appointment_time").replace("T", " ") + ":00");
            java.sql.Date priorityDate = null;
            if (req.getParameter("priority_delivery_date") != null && !req.getParameter("priority_delivery_date").isEmpty())
                priorityDate = java.sql.Date.valueOf(req.getParameter("priority_delivery_date"));

            int itemCount = 0;
            try {
                itemCount = Integer.parseInt(req.getParameter("itemCount"));
            } catch (Exception ex) { itemCount = 0; }

            List<Integer> productIds = new java.util.ArrayList<>();
            List<Integer> quantities = new java.util.ArrayList<>();
            for (int i = 0; i < itemCount; i++) {
                String productIdParam = req.getParameter("product_id_" + i);
                String quantityParam = req.getParameter("quantity_" + i);
                if (productIdParam != null && !productIdParam.isEmpty() 
                    && quantityParam != null && !quantityParam.isEmpty()) {
                    try {
                        productIds.add(Integer.parseInt(productIdParam));
                        quantities.add(Integer.parseInt(quantityParam));
                    } catch (NumberFormatException ex) {
                        throw new ServletException(ex);
                    }
                }
            }


            if (productIds.isEmpty() || quantities.isEmpty()) {
                req.setAttribute("error", "Đơn hàng phải có ít nhất 1 sản phẩm.");

                Order order = orderDAO.getOrderById(orderId);
                ProductDAO productDAO = new ProductDAO(conn);
                CustomerDAO customerDAO = new CustomerDAO(conn);
                List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
                List<Product> products = productDAO.getAllProducts();
                List<Customer> customers = customerDAO.getAllCustomers();
                req.setAttribute("order", order);
                req.setAttribute("orderDetails", orderDetails);
                req.setAttribute("products", products);
                req.setAttribute("customers", customers);
                req.setAttribute("contentPage", "/view/admin/order_detail.jsp");
                req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
                return;
            }

            orderDAO.updateOrder(orderId, customerId, status, appointmentTime, notes, priorityDate);
            orderDAO.updateOrderItems(orderId, productIds, quantities);
            resp.sendRedirect("detail?id=" + orderId + "&edit=1"); 
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

