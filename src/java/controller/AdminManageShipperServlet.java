package controller;

import dao.ShipperDAO;
import model.Shipper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/shippers")
public class AdminManageShipperServlet extends HttpServlet {
    private ShipperDAO shipperDAO;

    @Override
    public void init() throws ServletException {
        shipperDAO = new ShipperDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "create":
                    showCreateForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteShipper(request, response);
                    break;
                default:
                    listShippers(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            int id = (idParam == null || idParam.isEmpty()) ? 0 : Integer.parseInt(idParam);

            String name = request.getParameter("name"); // chỉ hiển thị ở form, không insert DB
            String area = request.getParameter("area");
            int priorityLevel = Integer.parseInt(request.getParameter("priority_level"));
            double dailyIncome = Double.parseDouble(request.getParameter("daily_income"));

            Shipper shipper = new Shipper(id, name, area, priorityLevel, dailyIncome);

            if (request.getParameter("mode").equals("create")) {
                shipperDAO.insertShipper(shipper);
            } else {
                shipperDAO.updateShipper(shipper);
            }

            response.sendRedirect("shippers");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void listShippers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Shipper> shippers = shipperDAO.getAllShippers();
        request.setAttribute("shippers", shippers);
        request.getRequestDispatcher("/view/admin/shipper_list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.getRequestDispatcher("/view/admin/shipper_form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Shipper shipper = shipperDAO.getShipperById(id);
        request.setAttribute("shipper", shipper);
        request.setAttribute("mode", "edit");
        request.getRequestDispatcher("/view/admin/shipper_form.jsp").forward(request, response);
    }

    private void deleteShipper(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            shipperDAO.deleteShipper(id);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        response.sendRedirect("shippers");
    }
}
