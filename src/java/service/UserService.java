package service;

import dao.UserDAO;
import model.User;
import utils.PasswordUtil;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    // Phương thức chứa logic nghiệp vụ đăng ký
    public boolean register(User user) {
        try {
            // Logic 1: Kiểm tra username tồn tại
            if (userDAO.checkUsernameExists(user.getUsername())) {
                System.out.println("Username already exists.");
                return false;
            }

            // Logic 2: Mã hóa mật khẩu
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            // Logic 3: Gọi DAO để thực hiện ghi vào DB
            return userDAO.createUser(user, hashedPassword);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}