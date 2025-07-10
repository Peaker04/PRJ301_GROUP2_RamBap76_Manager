package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.UserDAO;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.EmailService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import utils.AppConfig;

@WebServlet("/google-callback")
public class GoogleCallbackServlet extends HttpServlet {

    private String googleClientId;
    private String googleClientSecret;
    private String googleRedirectUri;

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Đọc cấu hình từ AppConfig (config.properties)
        googleClientId = AppConfig.getGoogleClientId();
        googleClientSecret = AppConfig.getGoogleClientSecret();
        googleRedirectUri = AppConfig.getGoogleRedirectUri();

        if (googleClientId == null || googleClientSecret == null || googleRedirectUri == null) {
            throw new ServletException("Missing Google API configuration. Check config.properties.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        String error = req.getParameter("error");

        if (error != null) {
            resp.sendRedirect(req.getContextPath() + "/authentication/login.jsp?error=" + error);
            return;
        }

        if (code == null || code.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/authentication/login.jsp?error=Google-login-failed");
            return;
        }

        try {
            String accessToken = getAccessToken(code);
            JsonObject userInfo = getUserInfo(accessToken);

            String email = userInfo.get("email").getAsString();
            String name = userInfo.has("name") ? userInfo.get("name").getAsString() : "N/A";

            UserDAO userDAO = new UserDAO();
            User user = userDAO.findByEmail(email);

            if (user == null) {
                System.out.println("Không tìm thấy người dùng với email " + email + ". Đang tạo người dùng mới.");
                user = userDAO.createGoogleUser(email, name);
                if (user != null) {
                    String scheme = req.getScheme();
                    String serverName = req.getServerName();
                    int serverPort = req.getServerPort();
                    String contextPath = req.getContextPath();
                    String loginUrl = scheme + "://" + serverName + ":" + serverPort + contextPath + "/login";

                    EmailService emailService = new EmailService();
                    emailService.sendWelcomeEmail(user.getEmail(), user.getFullName(), loginUrl);
                }
            } else {
                System.out.println("Đã tìm thấy người dùng tồn tại: " + user.getUsername());
            }

            if (user != null) {
                req.getSession().setAttribute("user", user);
                System.out.println("Người dùng đăng nhập thành công: " + user.getUsername());
                resp.sendRedirect(req.getContextPath() + "/shipper/dashboard");
            } else {
                throw new ServletException("Đăng nhập bằng Google thất bại: đối tượng người dùng bị null sau các thao tác CSDL.");
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/authentication/login.jsp?error=An-internal-error-occurred");
        }
    }

    private String getAccessToken(String code) throws IOException {
        URL url = new URL(GOOGLE_TOKEN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String params = "code=" + code
                + "&client_id=" + googleClientId
                + "&client_secret=" + googleClientSecret
                + "&redirect_uri=" + googleRedirectUri
                + "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        if (responseCode != 200) {
            System.err.println("Lỗi xác thực Google: " + response.toString());
            throw new IOException("Không thể lấy access token từ Google. Phản hồi: " + response.toString());
        }

        JsonObject jsonObject = new Gson().fromJson(response.toString(), JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    private JsonObject getUserInfo(String accessToken) throws IOException {
        URL url = new URL(GOOGLE_USERINFO_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return new Gson().fromJson(response.toString(), JsonObject.class);
    }
}