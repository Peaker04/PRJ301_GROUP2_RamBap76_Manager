package controller;

import dao.ShipperDAO;
import dao.UserProfileDAO;
import model.Shipper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.UserProfile;

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
        if (action == null) {
            action = "list";
        }

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

        int userId = Integer.parseInt(request.getParameter("user_id")); // NEW
        String address = request.getParameter("area");

        try {
            String idParam = request.getParameter("id");
            int id = (idParam == null || idParam.isEmpty()) ? 0 : Integer.parseInt(idParam);

            String name = request.getParameter("name"); // chỉ hiển thị ở form, không insert DB
            String area = request.getParameter("area");
            int priorityLevel = Integer.parseInt(request.getParameter("priority_level"));
            double dailyIncome = Double.parseDouble(request.getParameter("daily_income"));

            Shipper shipper = new Shipper(userId, name, area, priorityLevel, dailyIncome);

            if (request.getParameter("mode").equals("create")) {
                shipperDAO.insertShipper(shipper);
            } else {
                shipperDAO.updateShipper(shipper);
            }

            UserProfileDAO userProfileDAO = new UserProfileDAO();
            UserProfile userProfile = userProfileDAO.getUserProfileByUserId(userId);

            if (userProfile == null) {
                // Tạo mới profile nếu chưa có
                userProfile = new UserProfile();
                userProfile.setUserId(userId);
                userProfile.setAddress(area);
                // Thiết lập các giá trị mặc định khác
                userProfile.setFirstName("");
                userProfile.setLastName("");
                userProfile.setEmail("");
                userProfile.setGender("");
                userProfile.setPhoneNumber("");
                userProfileDAO.insertUserProfile(userProfile);
            } else {
                // Cập nhật địa chỉ nếu đã có profile
                userProfile.setAddress(area);
                userProfileDAO.updateUserProfile(userProfile);
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
        request.setAttribute("contentPage", "/view//admin/shipper_list.jsp");
        request.getRequestDispatcher("/view/common/admin_layout.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("mode", "create");
        request.setAttribute("contentPage", "/view//admin/shipper_form.jsp");
        request.getRequestDispatcher("/view/common/admin_layout.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        Shipper shipper = shipperDAO.getShipperById(id);
        request.setAttribute("shipper", shipper);
        UserProfileDAO userProfileDAO = new UserProfileDAO();
        UserProfile userProfile = userProfileDAO.getUserProfileByUserId(shipper.getUserId());
        request.setAttribute("userProfile", userProfile);
        request.setAttribute("mode", "edit");
        request.setAttribute("contentPage", "/view//admin/shipper_form.jsp");
        request.getRequestDispatcher("/view/common/admin_layout.jsp").forward(request, response);
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
