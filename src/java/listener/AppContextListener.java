package listener;

import connect.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Phương thức này được gọi khi web application được deploy hoặc server khởi động
        System.out.println("Application starting up... Initializing DB Connection.");
        DBConnection.initialize(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Phương thức này được gọi khi web application bị undeploy hoặc server tắt
        System.out.println("Application shutting down.");
    }
}
