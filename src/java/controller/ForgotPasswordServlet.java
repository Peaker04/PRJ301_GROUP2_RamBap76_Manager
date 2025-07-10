package controller;

import dao.UserDAO;
import service.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Import HttpSession
import model.User;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hiển thị form quên mật khẩu
        request.getRequestDispatcher("/view/authentication/forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        User user = userDAO.findUserByUsername(username);

        // --- THAY ĐỔI LOGIC TẠI ĐÂY ---

        if (user != null) {
            // TRƯỜNG HỢP NHẬP ĐÚNG USERNAME
            try {
                UUID token = UUID.randomUUID();
                long expiryTime = System.currentTimeMillis() + (60 * 60 * 1000); // Token hết hạn sau 1 giờ
                Date expiryDate = new Date(expiryTime);

                userDAO.saveResetToken(username, token, expiryDate);

                String resetLink = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath() + "/reset-password?token=" + token.toString();

                emailService.sendPasswordResetEmail(user.getEmail(), resetLink);

                // Lưu thông báo vào Session để có thể đọc được sau khi redirect
                HttpSession session = request.getSession();
                session.setAttribute("successMessage", "Yêu cầu đã được gửi. Vui lòng kiểm tra email để đặt lại mật khẩu.");
                
                // Chuyển hướng đến trang thông báo thành công
                response.sendRedirect(request.getContextPath() + "/view/authentication/email_sended.jsp");

            } catch (Exception e) {
                e.printStackTrace();
                // Nếu có lỗi khi gửi mail, quay lại trang cũ với thông báo lỗi
                request.setAttribute("errorMessage", "Có lỗi xảy ra trong quá trình gửi email, vui lòng thử lại.");
                request.getRequestDispatcher("/view/authentication/forgot_password.jsp").forward(request, response);
            }
        } else {
            // TRƯỜNG HỢP NHẬP SAI USERNAME
            // Quay lại trang cũ với thông báo lỗi để người dùng nhập lại
            request.setAttribute("errorMessage", "Tên đăng nhập không tồn tại.");
            request.getRequestDispatcher("/view/authentication/forgot_password.jsp").forward(request, response);
        }
    }
}