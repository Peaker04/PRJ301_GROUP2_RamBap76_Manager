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
            // Lấy danh sách khách hàng, sản phẩm cho form
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
            // Thêm đơn hàng mới
            orderDAO.createOrder(customerId, status, appointmentTime, notes, priorityDeliveryDate, productIds, quantities);
            resp.sendRedirect(req.getContextPath() + "/admin/orders?created=1");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

