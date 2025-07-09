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

@WebServlet(name = "ReceiptDetailServlet", urlPatterns = {"/admin/receipts/detail"})
public class ReceiptDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean isEdit = "1".equals(req.getParameter("edit"));
        try (Connection conn = DBConnection.getConnection()) {
            StationReceiptDAO dao = new StationReceiptDAO(conn);
            ProductDAO pdao = new ProductDAO(conn);
            
            StationReceipt receipt = dao.getReceiptById(id);
            List<StationReceiptDetail> details = dao.getDetails(id);
            
            req.setAttribute("receipt", receipt);
            req.setAttribute("details", details);
            
            if (isEdit) {
                List<Product> products = pdao.getAllProducts();
                req.setAttribute("products", products);
            }

            req.setAttribute("contentPage", "/view/admin/receipt_detail.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection conn = DBConnection.getConnection()) {
            StationReceiptDAO dao = new StationReceiptDAO(conn);

            if ("delete".equals(action)) {
                dao.deleteReceipt(id);
                resp.sendRedirect(req.getContextPath() + "/admin/receipts?deleted=1");
                return;
            }

            StationReceipt receipt = dao.getReceiptById(id);
            receipt.setStationName(req.getParameter("station_name"));
            receipt.setTransportFee(Double.parseDouble(req.getParameter("transport_fee")));


            List<StationReceiptDetail> details = new ArrayList<>();
            int totalQuantity = 0;
            int idx = 0;
            while (true) {
                String pid = req.getParameter("product_id_" + idx);
                String qty = req.getParameter("quantity_" + idx);
                String remain = req.getParameter("remaining_" + idx); 
                if (pid == null || qty == null) break;
                pid = pid.trim(); qty = qty.trim();
                if (!pid.isEmpty() && !qty.isEmpty()) {
                    StationReceiptDetail d = new StationReceiptDetail();
                    d.setReceiptId(id);
                    d.setProductId(Integer.parseInt(pid));
                    int q = Integer.parseInt(qty);
                    d.setQuantity(q);
                    d.setCurrentStock(q);
                    if (remain != null && !remain.isEmpty()) {
                        d.setRemainingQuantity(Integer.parseInt(remain));
                    } else {
                        d.setRemainingQuantity(q);
                    }
                    details.add(d);
                    totalQuantity += q;
                }
                idx++;
            }
            receipt.setTotalQuantity(totalQuantity);

            dao.updateReceipt(receipt); 

            for (StationReceiptDetail d : details) {
                dao.updateDetail(id, d);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/receipts/detail?id=" + id + "&edit=0");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
