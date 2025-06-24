package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    // Hiển thị trang đặt lại mật khẩu nếu token hợp lệ
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tokenStr = request.getParameter("token");
        try {
            UUID token = UUID.fromString(tokenStr);
            User user = userDAO.findUserByResetToken(token);

            if (user != null) {
                // Token hợp lệ, chuyển đến trang reset
                request.setAttribute("token", tokenStr);
                request.getRequestDispatcher("/view/authentication/reset_password.jsp").forward(request, response);
            } else {
                // Token không hợp lệ hoặc đã hết hạn
                request.setAttribute("errorMessage", "Đường dẫn không hợp lệ hoặc đã hết hạn.");
                request.getRequestDispatcher("/view/authentication/login.jsp").forward(request, response);
            }
        } catch (IllegalArgumentException e) {
            // Chuỗi token không phải là UUID
            request.setAttribute("errorMessage", "Đường dẫn không hợp lệ.");
            request.getRequestDispatcher("/view/authentication/login.jsp").forward(request, response);
        }
    }

    // Xử lý việc submit mật khẩu mới
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tokenStr = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Kiểm tra mật khẩu có khớp không
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("token", tokenStr);
            request.setAttribute("errorMessage", "Mật khẩu không khớp.");
            request.getRequestDispatcher("/view/authentication/reset_password.jsp").forward(request, response);
            return;
        }

        // 2. Kiểm tra lại token
        User user = userDAO.findUserByResetToken(UUID.fromString(tokenStr));
        if (user == null) {
            request.setAttribute("errorMessage", "Yêu cầu không hợp lệ hoặc đã hết hạn. Vui lòng thử lại.");
            request.getRequestDispatcher("/view/authentication/login.jsp").forward(request, response);
            return;
        }

        // 3. Cập nhật mật khẩu và xóa token
        boolean success = userDAO.updatePasswordById(user.getId(), newPassword);
        if (success) {
            userDAO.clearResetToken(user.getId()); // Xóa token để không dùng lại được
            response.sendRedirect(request.getContextPath() + "/login?message=reset_success");
        } else {
            request.setAttribute("token", tokenStr);
            request.setAttribute("errorMessage", "Có lỗi xảy ra, không thể đặt lại mật khẩu.");
            request.getRequestDispatcher("/view/authentication/reset_password.jsp").forward(request, response);
        }
    }
}
