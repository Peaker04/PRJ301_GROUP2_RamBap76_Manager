package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy phiên làm việc hiện tại của người dùng.
        // Tham số `false` rất quan trọng: nó sẽ lấy session đang tồn tại, 
        // và trả về null nếu không có session nào, thay vì tạo ra một session mới.
        HttpSession session = request.getSession(false);
        
        // 2. Kiểm tra xem session có tồn tại hay không
        if (session != null) {
            // Nếu có, hủy session. Hành động này sẽ xóa tất cả các thuộc tính 
            // đã được lưu trong session (ví dụ: đối tượng "user").
            session.invalidate();
        }
        
        // 3. Chuyển hướng người dùng về trang đăng nhập.
        // request.getContextPath() sẽ lấy đường dẫn gốc của ứng dụng (ví dụ: /RamBap76_Manager)
        // để đảm bảo link hoạt động chính xác.
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Trong trường hợp có yêu cầu POST đến /logout, chúng ta cũng xử lý tương tự như GET.
        doGet(request, response);
    }
}