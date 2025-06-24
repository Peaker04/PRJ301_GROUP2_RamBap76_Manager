package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import service.EmailService; // Service do Tuấn tạo

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    // Giả sử EmailService đã được Tuấn tạo và có thể inject/khởi tạo
//    private final EmailService emailService = new EmailService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        // String userEmail = userDAO.getEmailByUsername(username); // Cần có phương thức này trong DAO

        // if (userEmail != null) {
        try {
            UUID token = UUID.randomUUID();
            long expiryTime = System.currentTimeMillis() + (60 * 60 * 1000); // 1 giờ
            Date expiryDate = new Date(expiryTime);

            userDAO.saveResetToken(username, token, expiryDate);

            // Tạo link reset
            String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/reset_password?token=" + token.toString();

            // Gọi service của Tuấn
            // emailService.sendPasswordResetEmail(userEmail, resetLink);
            request.setAttribute("message", "Yêu cầu đã được gửi. Vui lòng kiểm tra email để đặt lại mật khẩu.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại.");
        }
        // } else {
        //     request.setAttribute("errorMessage", "Tên đăng nhập không tồn tại.");
        // }
        request.getRequestDispatcher("view/authentication/forgot_password.jsp").forward(request, response);
    }
}
