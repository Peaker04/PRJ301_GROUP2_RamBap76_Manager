package controller;

import connect.DBConnection;
import dao.ProductDAO;
import dao.StationReceiptDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Product;
import model.StationReceipt;
import model.StationReceiptDetail;

@WebServlet(name = "CreateReceiptServlet", urlPatterns = {"/admin/receipts/create"})
public class CreateReceiptServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBConnection.getConnection()) {
            // Lấy danh sách sản phẩm cho form nhập hàng
            ProductDAO productDAO = new ProductDAO(conn);
            List<Product> products = productDAO.getAllProducts();
            req.setAttribute("products", products);
            req.setAttribute("contentPage", "/view/admin/receipt_form.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            StationReceiptDAO dao = new StationReceiptDAO(conn);

            StationReceipt receipt = new StationReceipt();
            receipt.setReceiptDate(Date.valueOf(req.getParameter("receipt_date")));
            receipt.setStationName(req.getParameter("station_name"));
            receipt.setTransportFee(Double.parseDouble(req.getParameter("transport_fee")));

            int totalQuantity = 0;
            List<StationReceiptDetail> details = new ArrayList<>();
            int idx = 0;
            while (true) {
                String pid = req.getParameter("product_id_" + idx);
                String qty = req.getParameter("quantity_" + idx);
                if (pid == null || qty == null) break;
                pid = pid.trim(); qty = qty.trim();
                if (!pid.isEmpty() && !qty.isEmpty()) {
                    int productId = Integer.parseInt(pid);
                    int quantity = Integer.parseInt(qty);

                    String error = dao.checkImportLimit(productId, quantity, receipt.getReceiptDate());
                    if (error != null) {
                        conn.rollback();
                        req.setAttribute("error", error);
                        doGet(req, resp);
                        return;
                    }

                    StationReceiptDetail d = new StationReceiptDetail();
                    d.setProductId(productId);
                    d.setQuantity(quantity);
                    d.setCurrentStock(quantity);
                    d.setRemainingQuantity(quantity);
                    details.add(d);
                    totalQuantity += quantity;
                }
                idx++;
            }

            receipt.setTotalQuantity(totalQuantity);
            dao.createReceipt(receipt, details);
            conn.commit();
            resp.sendRedirect(req.getContextPath() + "/admin/receipts");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
