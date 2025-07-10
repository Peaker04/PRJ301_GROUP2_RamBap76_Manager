package controller;
import connect.DBConnection;
import dao.ProductDAO;
import model.Product;
import model.StationReceiptDetail;
import java.sql.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/admin/products/detail"})
public class ProductDetailServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String edit = req.getParameter("edit");
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO dao = new ProductDAO(conn);
            Product product = dao.getProductById(id);
            List<StationReceiptDetail> importHistory = dao.getImportHistory(id);
            req.setAttribute("product", product);
            req.setAttribute("importHistory", importHistory);
            req.setAttribute("isEdit", "1".equals(edit));
            req.setAttribute("contentPage", "/view/admin/product_detail.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String action = req.getParameter("action");
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection conn = DBConnection.getConnection()) {
            ProductDAO dao = new ProductDAO(conn);
            if ("edit".equals(action)) {
                String name = req.getParameter("name");
                dao.updateProductName(id, name);
                resp.sendRedirect(req.getContextPath() + "/admin/products/detail?id=" + id);
                return;
            } else if ("delete".equals(action)) {
                dao.deleteProduct(id);
                resp.sendRedirect(req.getContextPath() + "/admin/products?deleted=1");
                return;
            }
            doGet(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}