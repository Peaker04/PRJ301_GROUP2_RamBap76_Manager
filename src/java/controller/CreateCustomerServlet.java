package controller;

import connect.DBConnection;
import dao.CustomerDAO;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Customer;

@WebServlet(name = "CreateCustomerServlet", urlPatterns = {"/admin/customers/create"})
public class CreateCustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("contentPage", "/view/admin/customer_form.jsp");
        req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String notes = req.getParameter("notes");

        try (Connection conn = DBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(conn);
            Customer c = new Customer();
            c.setName(name);
            c.setPhone(phone);
            c.setAddress(address);
            c.setNotes(notes);
            customerDAO.createCustomer(c);
            resp.sendRedirect(req.getContextPath() + "/admin/customers");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
