package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();

        // Thêm kiểm tra để cho phép truy cập vào trang reset-password mà không yêu cầu login
        boolean isPublicResource = uri.equals(contextPath + "/login")
                || uri.equals(contextPath + "/forgot-password")
                || uri.startsWith(contextPath + "/assets/")
                || uri.startsWith(contextPath + "/css/")
                || uri.startsWith(contextPath + "/image/")
                || uri.equals(contextPath + "/signup")
                || uri.equals(contextPath + "/view/authentication/signup.jsp")
                || uri.equals(contextPath + "/view/authentication/email_sended.jsp")
                || uri.startsWith(contextPath + "/reset-password");
                

        if (uri.equals(contextPath + "/")) {
            isPublicResource = true;
        }

        if (isPublicResource) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect(contextPath + "/login");
            return;
        }

        String role = user.getRole();
        if (uri.startsWith(contextPath + "/admin") && !"ADMIN".equals(role)) {
            response.sendRedirect(contextPath + "/access_denied.jsp");
        } else if (uri.startsWith(contextPath + "/shipper") && !"SHIPPER".equals(role)) {
            response.sendRedirect(contextPath + "/access_denied.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }
}
