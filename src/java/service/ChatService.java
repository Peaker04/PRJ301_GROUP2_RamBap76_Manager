package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class ChatService {

    private static final String API_KEY = "AIzaSyBHya0jYSVX1xqu1ZuAw6xNpq00VrKEyFo";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash:generateContent";

    private List<ChatMessage> conversationHistory = new ArrayList<>();
    private Gson gson = new Gson(); // Tạo Gson instance

    public String generateResponse(String userMessage) {
        try {
            // Lưu tin nhắn người dùng vào lịch sử
            conversationHistory.add(new ChatMessage("user", userMessage));

            // Giới hạn lịch sử cuộc trò chuyện (10 tin nhắn gần nhất)
            if (conversationHistory.size() > 10) {
                conversationHistory = conversationHistory.subList(conversationHistory.size() - 10, conversationHistory.size());
            }

            // Tạo JSON request bằng Gson
            JsonObject messagePart = new JsonObject();
            messagePart.addProperty("text", "Bạn là RamBap76, một trợ lý AI thân thiện. "
                    + "Hãy trả lời một cách vui vẻ và gần gũi, ngắn gọn thôi đừng dài dòng quá. "
                    + ""
                    + ""
                    + userMessage);

            JsonArray partsArray = new JsonArray();
            partsArray.add(messagePart);

            JsonObject userMessageObject = new JsonObject();
            userMessageObject.addProperty("role", "user");
            userMessageObject.add("parts", partsArray);

            JsonArray contentsArray = new JsonArray();
            contentsArray.add(userMessageObject);

            JsonObject requestJson = new JsonObject();
            requestJson.add("contents", contentsArray);

            // Chuyển JSON request thành chuỗi
            String requestBody = gson.toJson(requestJson);
            System.out.println("Request body: " + requestBody);

            // Gửi request đến API
            URL url = new URL(API_URL + "?key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    System.out.println("API Response: " + response.toString());

                    // Trích xuất phản hồi từ JSON
                    String extractedText = extractTextFromResponse(response.toString());

                    if (extractedText != null && !extractedText.isEmpty()) {
                        conversationHistory.add(new ChatMessage("model", extractedText));
                        return extractedText;
                    }
                }
            } else {
                System.out.println("Error Response: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Xin chào! Tôi là trợ lý RamBap76. Tôi có thể giúp gì cho bạn?";
    }

    private String extractTextFromResponse(String jsonResponse) {
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray candidates = jsonObject.getAsJsonArray("candidates");

            if (candidates != null && candidates.size() > 0) {
                JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                JsonObject content = firstCandidate.getAsJsonObject("content");
                JsonArray parts = content.getAsJsonArray("parts");

                if (parts != null && parts.size() > 0) {
                    return parts.get(0).getAsJsonObject().get("text").getAsString().trim();
                }
            }
        } catch (Exception e) {
            System.out.println("JSON parsing error: " + e.getMessage());
        }
        return "Lỗi khi trích xuất phản hồi từ AI.";
    }

    private static class ChatMessage {

        private String role;
        private String text;

        public ChatMessage(String role, String text) {
            this.role = role;
            this.text = text;
        }
    }
}

