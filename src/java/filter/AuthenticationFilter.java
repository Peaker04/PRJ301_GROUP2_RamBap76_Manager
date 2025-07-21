package filter;

import dao.UserProfileDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import model.UserProfile;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private Set<String> publicUris;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Danh sách các URL của SERVLET và tài nguyên công khai không cần đăng nhập
        publicUris = new HashSet<>(Arrays.asList(
                "/",
                "/login",
                "/login-google",
                "/google-callback",
                "/signup",
                "/forgot-password",
                "/reset-password",
                "/email-sended",
                "/profile",
                "/ProfileServlet",
                "/ProfileShipperServlet",
                "/chat",
                "/chatbot"
        ));
        System.out.println("DEBUG Filter: Public URIs initialized: " + publicUris);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI().substring(contextPath.length());

        System.out.println("\n--- DEBUG Filter: Processing URI: " + uri + " ---");

        // Cho phép truy cập các tài nguyên tĩnh (css, js, images, etc.)
        boolean isPublicResource = publicUris.contains(uri)
                || uri.startsWith("/assets/")
                || uri.startsWith("/css/")
                || uri.startsWith("/js/")
                || uri.startsWith("/image/")
                || uri.startsWith("/vendor/")
                || uri.startsWith("/WEB-INF/tags/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".gif")
                || uri.endsWith(".ico")
                || uri.endsWith(".woff")
                || uri.endsWith(".woff2")
                || uri.endsWith(".ttf");

        if (isPublicResource) {
            System.out.println("DEBUG Filter: URI is a public resource or static file. Allowing access.");
            chain.doFilter(request, response);
            return;
        }
        boolean isJspPage = uri.endsWith(".jsp")
                && (uri.contains("authentication") || uri.contains("email_sended"));

        if (publicUris.contains(uri) || isJspPage) {
            chain.doFilter(request, res);
            return;
        }

        // Bắt đầu kiểm tra xác thực
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user != null && session.getAttribute("userProfile") == null) {
            UserProfile profile = UserProfileDAO.getUserProfileByUserId(user.getId());
            session.setAttribute("userProfile", profile);
        }
        // Nếu người dùng chưa đăng nhập, chuyển hướng đến trang login
        if (user == null) {
            System.out.println("DEBUG Filter: User not logged in. Redirecting to login.");
            response.sendRedirect(contextPath + "/login");
            return;
        }

        // Người dùng đã đăng nhập, tiến hành kiểm tra quyền truy cập (Authorization)
        String role = user.getRole();
        String accessDeniedUrl = contextPath + "/view/authentication/access_denied.jsp";

        System.out.println("DEBUG Filter: User " + user.getUsername() + " (Role: " + role + ") is logged in.");

        // 1. Kiểm tra các đường dẫn chỉ dành cho ADMIN
        if (uri.startsWith("/admin")) {
            if (!"ADMIN".equals(role)) {
                System.out.println("DEBUG Filter: Access DENIED for URI " + uri + ". User role is " + role + ", not ADMIN.");
                response.sendRedirect(accessDeniedUrl);
                return;
            }
        } // 2. Kiểm tra các đường dẫn dành cho SHIPPER
        else if (uri.startsWith("/shipper")) {
            // Cho phép cả ADMIN và SHIPPER truy cập khu vực shipper
            if (!"SHIPPER".equals(role) && !"ADMIN".equals(role)) {
                System.out.println("DEBUG Filter: Access DENIED for URI " + uri + ". User role is " + role + ", not SHIPPER or ADMIN.");
                response.sendRedirect(accessDeniedUrl);
                return;
            }
        }

        // 3. Các URL chung cho tất cả người dùng đã đăng nhập
        // (Ví dụ: /profile, /logout, /change-password, etc.)
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}
