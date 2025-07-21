<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>RamBap76 Assistant</title>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* CSS từ đoạn mã HTML của bạn */
        .hidden { display: none; }
        .chatbot-toggle-button {
            position: fixed;
            bottom: 20px;
            right: 20px;
            background: #007bff;
            color: white;
            border: none;
            border-radius: 50%;
            width: 60px;
            height: 60px;
            font-size: 24px;
            cursor: pointer;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            z-index: 1000;
        }
        .chatbot-window {
            position: fixed;
            bottom: 90px;
            right: 20px;
            width: 350px;
            height: 500px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.2);
            display: flex;
            flex-direction: column;
            z-index: 1000;
        }
        /* ... (Thêm toàn bộ CSS từ đoạn HTML của bạn tại đây) ... */
    </style>
</head>
<body>
    <!-- Nút toggle -->
    <button id="chatbot-toggle" class="chatbot-toggle-button">
        <i class="fas fa-comment-dots"></i>
    </button>

    <!-- Cửa sổ chatbot -->
    <div id="chatbot-window" class="chatbot-window hidden">
        <!-- ... (Toàn bộ nội dung HTML chatbot của bạn) ... -->
    </div>

    <script>
        // JavaScript từ đoạn mã HTML
        const chatbotToggle = document.getElementById('chatbot-toggle');
        const chatbotWindow = document.getElementById('chatbot-window');

        chatbotToggle.addEventListener('click', () => {
            chatbotWindow.classList.toggle('hidden');
        });

        // Xử lý gửi tin nhắn
        document.querySelector('.send-button').addEventListener('click', sendMessage);
        document.querySelector('.chat-input-area input').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') sendMessage();
        });

        function sendMessage() {
            const input = document.querySelector('.chat-input-area input');
            const message = input.value.trim();
            
            if (message) {
                // Thêm tin nhắn người dùng
                addUserMessage(message);
                input.value = '';
                
                // Gửi request đến ChatServlet
                fetch('/your-app-context/chat', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'message=' + encodeURIComponent(message)
                })
                .then(response => response.json())
                .then(data => {
                    // Thêm phản hồi AI
                    addBotMessage(data.response);
                })
                .catch(error => {
                    console.error('Error:', error);
                    addBotMessage('Đã xảy ra lỗi kết nối');
                });
            }
        }

        function addUserMessage(message) {
            const chatMessages = document.querySelector('.chat-messages');
            const messageDiv = document.createElement('div');
            messageDiv.className = 'chat-message user';
            messageDiv.innerHTML = `
                <div class="message-content">
                    <p>${escapeHtml(message)}</p>
                </div>
            `;
            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        function addBotMessage(message) {
            const chatMessages = document.querySelector('.chat-messages');
            const messageDiv = document.createElement('div');
            messageDiv.className = 'chat-message bot';
            messageDiv.innerHTML = `
                <div class="message-content">
                    <p>${escapeHtml(message)}</p>
                </div>
            `;
            chatMessages.appendChild(messageDiv);
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        function escapeHtml(text) {
            return text
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
        }
    </script>
</body>
</html>