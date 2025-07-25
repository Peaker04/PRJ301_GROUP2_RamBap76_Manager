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
import java.util.ArrayList;
import java.util.List;
import model.Customer;

@WebServlet(name = "ManageCustomerServlet", urlPatterns = {"/admin/customers"})
public class ManageCustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            CustomerDAO customerDAO = new CustomerDAO(conn);

            int totalCustomers = customerDAO.countCustomers(search);
            int totalPages = (int)Math.ceil((double)totalCustomers / size);
            if (page > totalPages && totalPages > 0) page = totalPages;

            List<Customer> customers = customerDAO.getCustomersByPage(search, sort, page, size);

            req.setAttribute("customers", customers);
            req.setAttribute("search", search);
            req.setAttribute("sort", sort);
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("totalCustomers", totalCustomers);
            req.setAttribute("contentPage", "/view/admin/customer_list.jsp");
            req.getRequestDispatcher("/view/common/admin_layout.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try (Connection conn = DBConnection.getConnection()) {
            CustomerDAO customerDAO = new CustomerDAO(conn);
            if ("edit".equals(action)){
                int id = Integer.parseInt(req.getParameter("id"));
                    Customer customer = customerDAO.getCustomerById(id);
                    req.setAttribute("customer", customer);
                    req.getRequestDispatcher("/admin/customers/detail").forward(req, resp);
                    return;
            }
            if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                customerDAO.softDeleteCustomer(id);
                resp.sendRedirect(req.getContextPath() + "/admin/customers?deleted=1");
                return;
            }
            if ("multiDelete".equals(action)) {
                String idsStr = req.getParameter("ids");
                if (idsStr != null && !idsStr.isEmpty()) {
                    String[] idArr = idsStr.split(",");
                    List<Integer> ids = new ArrayList<>();
                    for (String s : idArr) ids.add(Integer.parseInt(s));
                    for (int i : ids) {
                        
                    }
                    customerDAO.softDeleteCustomers(ids);
                }
                resp.sendRedirect(req.getContextPath() + "/admin/customers?deleted=1");
                return;
            }
            // Các action thêm/sửa/xóa khác nếu muốn
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

