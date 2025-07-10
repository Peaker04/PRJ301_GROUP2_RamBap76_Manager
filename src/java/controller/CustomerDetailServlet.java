package controller;

import connect.DBConnection;
import dao.CustomerDAO;
import dao.OrderDAO;
import model.Customer;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "CustomerDetailServlet", urlPatterns = {"/admin/customers/detail"})
public class CustomerDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/customers");
            return;
        }
        int customerId = Integer.parseInt(idStr);

        try (Connection conn = DBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(conn);
            OrderDAO orderDAO = new OrderDAO(conn);

            Customer customer = customerDAO.getCustomerById(customerId);
            if (customer == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/customers");
                return;
            }
            List<Order> orders = orderDAO.getOrdersByCustomer(customerId, "desc");

            req.setAttribute("customer", customer);
            req.setAttribute("customerOrders", orders);
            req.setAttribute("contentPage", "/view/admin/customer_detail.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/customers");
            return;
        }
        int customerId = Integer.parseInt(idStr);

        try (Connection conn = DBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(conn);

            if ("delete".equals(action)) {
                customerDAO.deleteCustomer(customerId);
                resp.sendRedirect(req.getContextPath() + "/admin/customers");
                return;
            }
            
            if ("edit".equals(action)){

                String name = req.getParameter("name");
                String phone = req.getParameter("phone");
                String address = req.getParameter("address");
                String notes = req.getParameter("notes");
                String latStr = req.getParameter("latitude");
                String lngStr = req.getParameter("longitude");

                Customer customer = new Customer();
                customer.setId(customerId);
                customer.setName(name);
                customer.setPhone(phone);
                customer.setAddress(address);
                customer.setNotes(notes);
                try {
                    customer.setLatitude(latStr != null && !latStr.isEmpty() ? Double.parseDouble(latStr) : null);
                } catch (Exception ex) {
                    customer.setLatitude(null);
                }
                try {
                    customer.setLongitude(lngStr != null && !lngStr.isEmpty() ? Double.parseDouble(lngStr) : null);
                } catch (Exception ex) {
                    customer.setLongitude(null);
                }

                customerDAO.updateCustomer(customer);
            }

            resp.sendRedirect(req.getContextPath() + "/admin/customers/detail?id=" + customerId);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
