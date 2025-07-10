package service;

public class EmailTemplate {

    // CSS chung cho tất cả các email, được định nghĩa một lần
    private static final String CSS_STYLES = "<style>"
        + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }"
        + ".container { width: 100%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }"
        + ".header { background-color: #007bff; color: #ffffff; padding: 20px; text-align: center; }"
        + ".header.welcome { background-color: #28a745; }" // Style riêng cho header welcome
        + ".header h1 { margin: 0; }"
        + ".body-content { padding: 30px; color: #333333; line-height: 1.6; }"
        + ".button { display: inline-block; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; color: #ffffff !important; }" // Thêm !important để đảm bảo màu chữ
        + ".button.primary { background-color: #007bff; }" // Style cho nút chính
        + ".button.success { background-color: #28a745; }" // Style cho nút welcome
        + ".footer { background-color: #f4f4f4; color: #888888; padding: 15px; text-align: center; font-size: 12px; }"
        + "</style>";

    /**
     * Tạo nội dung HTML cho email chào mừng
     */
    public static String getWelcomeEmail(String fullName,String loginUrl) {
        String body = "<h2>Chào mừng, " + fullName + "!</h2>"
            + "<p>Cảm ơn bạn đã đăng ký tài khoản tại hệ thống quản lý của RamBap76.</p>"
            + "<p>Tài khoản của bạn đã được tạo thành công và sẵn sàng để sử dụng.</p>"
            + "<p style='text-align: center; margin: 30px 0;'>"
        + "<a href='" + loginUrl + "' class='button success'>Đi đến trang đăng nhập</a>"
            + "</p>";
        return buildHtml("welcome", "Chào mừng bạn đến với RamBap76!", body);
    }

    /**
     * Tạo nội dung HTML cho email đặt lại mật khẩu
     */
    public static String getPasswordResetEmail(String resetLink) {
        String body = "<h2>Yêu cầu đặt lại mật khẩu</h2>"
            + "<p>Chúng tôi đã nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng nhấp vào nút bên dưới để tạo mật khẩu mới.</p>"
            + "<p style='text-align: center; margin: 30px 0;'>"
            + "<a href='" + resetLink + "' class='button primary'>Đặt lại mật khẩu</a>"
            + "</p>"
            + "<p>Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này. Liên kết này sẽ hết hạn sau 60 phút.</p>";
        return buildHtml("default", "Yêu cầu đặt lại mật khẩu", body);
    }

    /**
     * Phương thức private để xây dựng khung HTML chung
     * @param type Loại header ('welcome' hoặc 'default')
     * @param title Tiêu đề trang
     * @param body Nội dung chính của email
     * @return Chuỗi HTML hoàn chỉnh
     */
    private static String buildHtml(String type, String title, String body) {
        String headerClass = type.equals("welcome") ? "header welcome" : "header";
        return "<!DOCTYPE html><html><head><title>" + title + "</title>" + CSS_STYLES + "</head>"
            + "<body>"
            + "<div class='container'>"
            + "<div class='" + headerClass + "'><h1>RamBap76</h1></div>"
            + "<div class='body-content'>" + body + "<p>Trân trọng,<br>Đội ngũ RamBap76</p></div>"
            + "<div class='footer'>&copy; " + java.time.Year.now().getValue() + " RamBap76. All rights reserved.</div>"
            + "</div>"
            + "</body></html>";
    }
}