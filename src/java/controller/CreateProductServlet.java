package controller;

import connect.DBConnection;
import dao.ProductDAO;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "CreateProductServlet", urlPatterns = {"/admin/products/create"})
public class CreateProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setAttribute("contentPage", "/view/admin/product_form.jsp");
        req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String name = req.getParameter("name");
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO dao = new ProductDAO(conn);
            dao.createProduct(name);
            resp.sendRedirect(req.getContextPath() + "/admin/products?created=1");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}