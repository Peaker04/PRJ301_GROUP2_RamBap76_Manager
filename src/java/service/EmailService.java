package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.UnsupportedEncodingException;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import java.util.Properties;

public class EmailService {

    // Thông tin máy chủ email
    private static final String SMTP_HOST = "smtp.gmail.com";  // Địa chỉ máy chủ của Gmail
    private static final String SMTP_PORT = "587";  // Cổng SMTP (587 cho TLS)
    private static final String SMTP_USER = "de180211tatuanky@gmail.com"; //email của bạn
    private static final String SMTP_PASSWORD = "htmh fgjn vbxt dmgd";  // mật khẩu ứng dụng của bạn

    public boolean sendPasswordResetEmail(String toEmail, String resetLink) throws UnsupportedEncodingException {
        String subject = "Đặt lại mật khẩu của bạn";
        String content = "Nhấp vào <a href='" + resetLink + "'>đây</a> để đặt lại mật khẩu của bạn.";

        // Cấu hình SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");  // Sử dụng TLS

        // Tạo session để gửi email
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            // Tạo một đối tượng MimeMessage để gửi email
            Message message = new MimeMessage(session);

            String fromEmail = "no-reply@RamBap76.com";
            message.setFrom(new InternetAddress(fromEmail, "RamBap76"));

            //     message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);

            // Cấu hình nội dung email để hỗ trợ HTML
            message.setContent(content, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);
            System.out.println("Email da duoc gui thanh cong.");
            return true;  // Gửi thành công
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Gui email that bai.");
            return false;  // Gửi thất bại
        }
    }
}
