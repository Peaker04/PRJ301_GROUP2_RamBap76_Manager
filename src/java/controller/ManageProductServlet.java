package controller;

import connect.DBConnection;
import dao.ProductDAO;
import model.Product;
import java.sql.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ManageProductServlet", urlPatterns = {"/admin/products"})
public class ManageProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String search = req.getParameter("search");
        String sort = req.getParameter("sort");
        if (sort == null) sort = "az";
        int size = 10;
        int page = 1;
        try {
            page = Integer.parseInt(req.getParameter("page"));
            if (page < 1) page = 1;
        } catch (Exception ex) {
            page = 1;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO dao = new ProductDAO(conn);
            int totalProducts = dao.countProducts(search);
            int totalPages = (int) Math.ceil((double) totalProducts / size);
            if (page > totalPages && totalPages > 0) page = totalPages;
            
            List<Product> products = dao.getProductsByPage(search, sort, page, size);
            req.setAttribute("products", products);
            req.setAttribute("search", search);
            req.setAttribute("sort", sort);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("contentPage", "/view/admin/product_list.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String action = req.getParameter("action");
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO dao = new ProductDAO(conn);
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                dao.deleteProduct(id);
                resp.sendRedirect(req.getContextPath() + "/admin/products?deleted=1");
                return;
            }
            if ("multiDelete".equals(action)) {
                String idsStr = req.getParameter("ids");
                if (idsStr != null && !idsStr.isEmpty()) {
                    String[] idArr = idsStr.split(",");
                    List<Integer> ids = new ArrayList<>();
                    for (String s : idArr) ids.add(Integer.parseInt(s));
                    dao.deleteProducts(ids);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/products?deleted=1");
                return;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        doGet(req, resp);
    }
}
