package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;

@WebServlet(name = "SignupServlet", urlPatterns = {"/signup"})
public class SignupServlet extends HttpServlet {
    
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String username = request.getParameter("username");
        String pass = request.getParameter("password");
        String confirmPass = request.getParameter("confirmPassword");
        String accountType = request.getParameter("accountType");

        if (!pass.equals(confirmPass)) {
            request.setAttribute("errorMessage", "Mật khẩu không khớp!");
            request.getRequestDispatcher("/view/authentication/signup.jsp").forward(request, response);
            return;
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setUsername(username);
        newUser.setPassword(pass); // Truyền mật khẩu gốc
        newUser.setRole(accountType.toUpperCase());

        boolean isRegistered = userService.register(newUser);

        if (isRegistered) {
            response.sendRedirect(request.getContextPath() + "/login?register=success");
        } else {
            // Lỗi có thể do username tồn tại hoặc lỗi DB
            // Service đã ghi log, ở đây chỉ cần thông báo chung
            request.setAttribute("errorMessage", "Tên đăng nhập đã tồn tại hoặc có lỗi xảy ra. Vui lòng thử lại.");
            request.getRequestDispatcher("/view/authentication/signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/view/authentication/signup.jsp").forward(request, response);
    }
}