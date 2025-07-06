package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("view/authentication/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // NEW: Get the role selected by the user from the form.
        String selectedRole = request.getParameter("role");

        User account = userDAO.login(username, password);

        // Check if user credentials are valid first.
        if (account != null) {
            String actualRole = account.getRole();

            // NEW: Check if the selected role matches the user's actual role in the database.
            if (selectedRole.equals(actualRole)) {
                // SUCCESS: Role matches, proceed with login.
                HttpSession session = request.getSession();
                session.setAttribute("user", account);

                // Redirect based on the verified role.
                if ("ADMIN".equals(actualRole)) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else if ("SHIPPER".equals(actualRole)) {
                    response.sendRedirect(request.getContextPath() + "/shipper/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/access_denied.jsp");
                }
            } else {
                // ERROR 1: Credentials are correct, but the selected role is wrong.
                request.setAttribute("errorMessage", "Vui lòng chọn đúng vai trò (Admin/Shipper)");
                // Retain the username and selected role to display on the form again.
                request.setAttribute("username", username);
                request.getRequestDispatcher("view/authentication/login.jsp").forward(request, response);
            }
        } else {
            // ERROR 2: Invalid username or password.
            request.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng.");
            // Retain the username to display on the form again.
            request.setAttribute("username", username);
            request.getRequestDispatcher("view/authentication/login.jsp").forward(request, response);
        }
    }
}