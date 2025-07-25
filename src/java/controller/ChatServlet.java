package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ChatService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ChatServlet", urlPatterns = {"/chat"})
public class ChatServlet extends HttpServlet {

    private ChatService chatService;

    @Override
    public void init() {
        chatService = new ChatService();
        System.out.println("ChatServlet: Initialized successfully"); // Debug log
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ChatServlet: GET request received"); // Debug log
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().write("Method GET is not allowed for this endpoint");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== Request Info ===");
        System.out.println("Context Path: " + request.getContextPath());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Servlet Path: " + request.getServletPath());

        try {
            System.out.println("ChatServlet: POST request received"); // Debug log
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String userMessage = request.getParameter("message");
            System.out.println("ChatServlet: Message parameter: " + userMessage); // Debug log

            if (userMessage != null && !userMessage.trim().isEmpty()) {
                String aiResponse = chatService.generateResponse(userMessage);
                System.out.println("ChatServlet: AI Response: " + aiResponse); // Debug log

                response.setContentType("application/json; charset=UTF-8");

                PrintWriter out = response.getWriter();
                String jsonResponse = "{\"response\":\"" + escapeJson(aiResponse) + "\"}";
                System.out.println("ChatServlet: JSON Response: " + jsonResponse); // Debug log

                out.print(jsonResponse);
                out.flush();
            } else {
                System.out.println("ChatServlet: Empty message received"); // Debug log
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Message is required\"}");
            }
        } catch (Exception e) {
            System.out.println("ChatServlet Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Ollama service unavailable\"}");
        }
    }

    private String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
