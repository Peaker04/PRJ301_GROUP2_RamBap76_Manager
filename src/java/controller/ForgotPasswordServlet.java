package controller;

import dao.UserDAO;
import service.EmailService; // Đảm bảo bạn đã tạo EmailService
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final EmailService emailService = new EmailService(); // Khởi tạo EmailService

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị form quên mật khẩu
        request.getRequestDispatcher("/view/authentication/forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");

        // Kiểm tra tên người dùng có tồn tại trong cơ sở dữ liệu không
        User user = userDAO.findUserByUsername(username);

        if (user != null) {
            try {
                UUID token = UUID.randomUUID(); // Tạo token mới
                long expiryTime = System.currentTimeMillis() + (60 * 60 * 1000); // Token hết hạn sau 1 giờ
                Date expiryDate = new Date(expiryTime);

                // Lưu token và thời gian hết hạn vào cơ sở dữ liệu
                userDAO.saveResetToken(username, token, expiryDate);

                // Tạo liên kết đặt lại mật khẩu
                String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath() + "/reset-password?token=" + token.toString();

                // Gửi email cho người dùng
                emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

                request.setAttribute("message", "Yêu cầu đã được gửi. Vui lòng kiểm tra email để đặt lại mật khẩu.");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại.");
            }
        } else {
            request.setAttribute("errorMessage", "Tên đăng nhập không tồn tại.");
        }

        // Chuyển hướng lại trang forgot_password.jsp với thông báo
        request.getRequestDispatcher("view/authentication/forgot_password.jsp").forward(request, response);
    }
}
