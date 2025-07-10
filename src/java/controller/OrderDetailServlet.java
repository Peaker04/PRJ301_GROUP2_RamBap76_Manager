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
import java.sql.*;
import java.util.*;


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
        int orderId = Integer.parseInt(req.getParameter("id"));

        try (Connection conn = DBConnection.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            CustomerDAO customerDAO = new CustomerDAO(conn);

            if ("delete".equals(action)) {
                // Rollback stock
                List<OrderDetail> oldOrderDetails = orderDAO.getOrderDetails(orderId);
                for (OrderDetail od : oldOrderDetails) {
                    productDAO.deductStockByBatch(od.getProductId(), od.getQuantity());
                }
                orderDAO.deleteOrder(orderId);
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
                return;
            }

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
            List<Integer> productIds = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
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
                reloadFormWithError(req, resp, orderDAO, productDAO, customerDAO, orderId, "Đơn hàng phải có ít nhất 1 sản phẩm.");
                return;
            }

            // 1. Lấy chi tiết đơn hàng cũ để rollback stock về trạng thái trước khi update
            List<OrderDetail> oldOrderDetails = orderDAO.getOrderDetails(orderId);
            Map<Integer, Integer> oldProductQuantity = new HashMap<>();
            for (OrderDetail od : oldOrderDetails) {
                oldProductQuantity.put(od.getProductId(), od.getQuantity());
            }

            // 2. Tính tồn kho thực tế sau khi rollback
            boolean enoughStock = true;
            StringBuilder errorMsg = new StringBuilder();

            for (int i = 0; i < productIds.size(); i++) {
                int pid = productIds.get(i);
                int newQty = quantities.get(i);

                int oldQty = oldProductQuantity.getOrDefault(pid, 0);
                int stockNow = productDAO.getCurrentStock(pid);

                int effectiveStock = stockNow + oldQty;
                if (newQty > effectiveStock) {
                    enoughStock = false;
                    errorMsg.append("Sản phẩm ")
                            .append(productDAO.getProductName(pid))
                            .append(" chỉ còn ")
                            .append(effectiveStock)
                            .append(" sản phẩm trong kho.<br>");
                }
            }

            if (!enoughStock) {
                reloadFormWithError(req, resp, orderDAO, productDAO, customerDAO, orderId, errorMsg.toString());
                return;
            }

            // 3. Rollback stock của toàn bộ order cũ
            for (OrderDetail od : oldOrderDetails) {
                productDAO.deductStockByBatch(od.getProductId(), od.getQuantity()); // Cộng lại stock
            }

            // 4. Trừ stock với số lượng mới
            for (int i = 0; i < productIds.size(); i++) {
                productDAO.deductStockByBatch(productIds.get(i), quantities.get(i)); // Trừ stock
            }

            // 5. Update order
            orderDAO.updateOrder(orderId, customerId, status, appointmentTime, notes, priorityDate);
            orderDAO.updateOrderItems(orderId, productIds, quantities);

            resp.sendRedirect("detail?id=" + orderId + "&edit=1"); 
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // Helper để load lại form kèm error message
    private void reloadFormWithError(HttpServletRequest req, HttpServletResponse resp,
                                     OrderDAO orderDAO, ProductDAO productDAO, CustomerDAO customerDAO,
                                     int orderId, String errorMsg)
            throws ServletException, IOException, SQLException {
        Order order = orderDAO.getOrderById(orderId);
        List<OrderDetail> orderDetails = orderDAO.getOrderDetails(orderId);
        List<Product> products = productDAO.getAllProducts();
        List<Customer> customers = customerDAO.getAllCustomers();
        req.setAttribute("order", order);
        req.setAttribute("orderDetails", orderDetails);
        req.setAttribute("products", products);
        req.setAttribute("customers", customers);
        req.setAttribute("error", errorMsg);
        req.setAttribute("contentPage", "/view/admin/order_detail.jsp");
        req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
    }
}

