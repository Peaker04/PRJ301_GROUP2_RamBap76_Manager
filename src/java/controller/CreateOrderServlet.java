/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import connect.DBConnection;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;
import model.Product;

@WebServlet(name = "CreateOrderServlet", urlPatterns = {"/admin/orders/create"})
public class CreateOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);
            List<Customer> customers = customerDAO.getAllCustomers();
            List<Product> products = productDAO.getAllProducts();
            req.setAttribute("customers", customers);
            req.setAttribute("products", products);
            req.setAttribute("contentPage", "/view/admin/order_form.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBConnection.getConnection()) { 
            
            OrderDAO orderDAO = new OrderDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn); 

            int customerId = Integer.parseInt(req.getParameter("customer_id"));
            String status = req.getParameter("status");
            String notes = req.getParameter("notes");
            String appointmentTimeStr = req.getParameter("appointment_time");
            String priorityDeliveryDateStr = req.getParameter("priority_delivery_date");
            Timestamp appointmentTime = null;
            Date priorityDeliveryDate = null;
            if (appointmentTimeStr != null && !appointmentTimeStr.isEmpty())
                appointmentTime = Timestamp.valueOf(appointmentTimeStr.replace("T", " ") + ":00");
            if (priorityDeliveryDateStr != null && !priorityDeliveryDateStr.isEmpty())
                priorityDeliveryDate = Date.valueOf(priorityDeliveryDateStr);

            List<Integer> productIds = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();
            int idx = 0;
            while (true) {
                String pid = req.getParameter("product_id_" + idx);
                String qty = req.getParameter("quantity_" + idx);
                if (pid == null || qty == null) break;
                if (!pid.isEmpty() && !qty.isEmpty()) {
                    productIds.add(Integer.parseInt(pid));
                    quantities.add(Integer.parseInt(qty));
                }
                idx++;
            }

            boolean enoughStock = true;
            StringBuilder errorMsg = new StringBuilder();
            for (int i = 0; i < productIds.size(); i++) {
                int productId = productIds.get(i);
                int quantity = quantities.get(i);
                int stock = productDAO.getCurrentStock(productId);
                String productName = productDAO.getProductName(productId);
                if (quantity > stock) {
                    enoughStock = false;
                    errorMsg.append("Sản phẩm ").append(productName)
                            .append(" chỉ còn ").append(stock)
                            .append(" sản phẩm trong kho.<br>");
                }
            }
            if (!enoughStock) {
                
                CustomerDAO customerDAO = new CustomerDAO(conn);
                List<Customer> customers = customerDAO.getAllCustomers();
                List<Product> products = productDAO.getAllProducts();
                req.setAttribute("customers", customers);
                req.setAttribute("products", products);
                req.setAttribute("error", errorMsg.toString());
                req.setAttribute("contentPage", "/view/admin/order_form.jsp");
                req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
                return;
            }

            conn.setAutoCommit(false);
            try {
                orderDAO.createOrder(customerId, status, appointmentTime, notes, priorityDeliveryDate, productIds, quantities);
                for (int i = 0; i < productIds.size(); i++) {
                    int productId = productIds.get(i);
                    int quantity = quantities.get(i);
                    productDAO.deductStockByBatch(productId, quantity);
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/orders?created=1");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}