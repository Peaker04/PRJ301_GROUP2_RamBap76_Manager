package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        // Bắt buộc người dùng phải đăng nhập
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Kiểm tra mật khẩu cũ có đúng không
        if (!BCrypt.checkpw(oldPassword, currentUser.getPassword())) {
            request.setAttribute("errorMessage", "Mật khẩu cũ không chính xác.");
            // Tùy vào luồng front-end, có thể forward đến trang change_password.jsp của Shipper hoặc Admin
            // Ví dụ: request.getRequestDispatcher("/view/shipper/change_password.jsp").forward(request, response);
            response.getWriter().println("Mật khẩu cũ không chính xác."); // Tạm thời
            return;
        }

        // 2. Kiểm tra mật khẩu mới có khớp không
        if (newPassword == null || newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Mật khẩu mới không hợp lệ hoặc không khớp.");
            // forward...
            response.getWriter().println("Mật khẩu mới không hợp lệ hoặc không khớp."); // Tạm thời
            return;
        }

        // 3. Cập nhật mật khẩu mới
        boolean success = userDAO.updatePasswordById(currentUser.getId(), newPassword);

        if (success) {
            request.setAttribute("successMessage", "Đổi mật khẩu thành công!");
            // Đăng xuất để người dùng đăng nhập lại với mật khẩu mới
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login?message=changepass_success");
        } else {
            request.setAttribute("errorMessage", "Có lỗi xảy ra, không thể đổi mật khẩu.");
            // forward...
            response.getWriter().println("Có lỗi xảy ra, không thể đổi mật khẩu."); // Tạm thời
        }
    }
}
