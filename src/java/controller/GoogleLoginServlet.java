package controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import utils.AppConfig;

@WebServlet("/login-google")
public class GoogleLoginServlet extends HttpServlet {
    private String googleClientId;
    private String googleRedirectUri;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Đọc cấu hình từ AppConfig (config.properties)
        googleClientId = AppConfig.getGoogleClientId();
        googleRedirectUri = AppConfig.getGoogleRedirectUri();

        if (googleClientId == null || googleRedirectUri == null) {
            throw new ServletException("Missing Google API configuration. Check config.properties.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sử dụng các giá trị đã được đọc từ AppConfig
        String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=email%20profile"
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=code"
                + "&client_id=" + googleClientId;

        resp.sendRedirect(oauthUrl);
    }
}