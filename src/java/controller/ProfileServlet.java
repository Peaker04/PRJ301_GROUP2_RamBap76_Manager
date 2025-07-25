package controller;

import dao.UserProfileDAO;
import model.User;
import model.UserProfile;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private UserProfileDAO userProfileDAO;

    @Override
    public void init() {
        userProfileDAO = new UserProfileDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user"); // Giả sử bạn lưu thông tin người dùng trong session với key "user"

        if (user == null) {
            response.sendRedirect("login.jsp"); // Nếu chưa đăng nhập, chuyển về trang login
            return;
        }

        System.out.println("DEBUG ProfileServlet: User ID from session: " + user.getId());

        UserProfile profile = userProfileDAO.getUserProfileByUserId(user.getId());

        if (profile != null) {
            System.out.println("DEBUG ProfileServlet: UserProfile found.");
            System.out.println("DEBUG ProfileServlet: First Name: " + profile.getFirstName());
            System.out.println("DEBUG ProfileServlet: Last Name: " + profile.getLastName());
            System.out.println("DEBUG ProfileServlet: Email: " + profile.getEmail());
            System.out.println("DEBUG ProfileServlet: Gender: " + profile.getGender());
            System.out.println("DEBUG ProfileServlet: Phone Number: " + profile.getPhoneNumber());
            System.out.println("DEBUG ProfileServlet: Address: " + profile.getAddress());
            request.setAttribute("profile", profile);
        } else {
            System.out.println("DEBUG ProfileServlet: UserProfile is NULL for user ID: " + user.getId());
        }
        
        session.setAttribute("userProfile", profile);
        request.setAttribute("contentPage", "/view/authentication/profile.jsp");
        request.getRequestDispatcher("/view/common/admin_layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy thông tin từ form
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String phoneNumber = request.getParameter("phone_number");
        String address = request.getParameter("address");

        System.out.println("DEBUG: firstName = " + firstName);
        System.out.println("DEBUG: lastName = " + lastName);
        System.out.println("DEBUG: email = " + email);
        System.out.println("DEBUG: gender = " + gender);
        System.out.println("DEBUG: phoneNumber = " + phoneNumber);
        System.out.println("DEBUG: address = " + address);
        System.out.println("DEBUG: userId from session = " + user.getId());
        // Tạo đối tượng UserProfile để cập nhật
        UserProfile profileToSave = new UserProfile(user.getId(), firstName, lastName, email, gender, phoneNumber, address);

        // Cập nhật vào DB
        boolean success;

        // =======================================================================
        // THAY THẾ LOGIC CẬP NHẬT TRONG doPost BẰNG PHẦN NÀY
        // =======================================================================
        // Kiểm tra xem profile đã tồn tại trong DB chưa
        UserProfile existingProfile = userProfileDAO.getUserProfileByUserId(user.getId());

        if (existingProfile == null) {
            // Nếu chưa tồn tại, thì chèn mới (INSERT)
            System.out.println("DEBUG ProfileServlet: Profile not found, attempting INSERT.");
            success = userProfileDAO.insertUserProfile(profileToSave);
        } else {
            // Nếu đã tồn tại, thì cập nhật (UPDATE)
            System.out.println("DEBUG ProfileServlet: Profile found, attempting UPDATE.");
            success = userProfileDAO.updateUserProfile(profileToSave);
        }

        if (success) {
            request.setAttribute("message", "Profile updated successfully!");
        } else {
            request.setAttribute("error", "Failed to update profile.");
        }

        // Sau khi cập nhật, tải lại trang với dữ liệu mới
        doGet(request, response);
    }
}
