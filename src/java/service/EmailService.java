package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "de180211tatuanky@gmail.com";
    private static final String SMTP_PASSWORD = "htmh fgjn vbxt dmgd";

    public boolean sendPasswordResetEmail(String toEmail, String resetLink) {
        String subject = "Yêu cầu đặt lại mật khẩu cho tài khoản RamBap76";
        // Gọi lớp EmailTemplate để lấy nội dung HTML
        String content = EmailTemplate.getPasswordResetEmail(resetLink);
        return sendEmail(toEmail, subject, content);
    }

    public boolean sendWelcomeEmail(String toEmail, String fullName,String loginUrl) {
        String subject = "Chào mừng bạn đến với RamBap76!";
        // Gọi lớp EmailTemplate để lấy nội dung HTML
        String content = EmailTemplate.getWelcomeEmail(fullName, loginUrl);
        return sendEmail(toEmail, subject, content);
    }

    private boolean sendEmail(String toEmail, String subject, String content) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@RamBap76.com", "RamBap76"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("Email đã được gửi thành công đến " + toEmail);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("Gửi email đến " + toEmail + " thất bại.");
            return false;
        }
    }
}
