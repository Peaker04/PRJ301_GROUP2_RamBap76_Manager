<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<style>
    .chatbot-toggle-button {
        position: fixed;
        bottom: 20px;
        right: 20px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 50%;
        width: 50px;
        height: 50px;
        font-size: 20px;
        cursor: pointer;
        z-index: 1000;
    }

    .chatbot-window {
        position: fixed;
        bottom: 80px;
        right: 20px;
        width: 350px;
        max-height: 500px;
        background-color: white;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0,0,0,0.2);
        display: flex;
        flex-direction: column;
        overflow: hidden;
        z-index: 1000;
    }

    .chatbot-window.hidden {
        display: none;
    }

    .chatbot-header {
        display: flex;
        align-items: center;
        padding: 10px;
        background-color: #007bff;
        color: white;
    }

    .chatbot-header .assistant-info {
        flex-grow: 1;
        margin-left: 10px;
    }

    .status {
        font-size: 12px;
    }

    .status-dot {
        display: inline-block;
        width: 8px;
        height: 8px;
        background-color: #0f0;
        border-radius: 50%;
        margin-right: 5px;
    }

    .chat-messages {
        flex-grow: 1;
        padding: 10px;
        overflow-y: auto;
        background: #f9f9f9;
    }

    .message {
        margin-bottom: 10px;
        max-width: 80%;
        padding: 8px 12px;
        border-radius: 15px;
        word-wrap: break-word;
    }

    .user-message {
        background-color: #e3f2fd;
        margin-left: auto;
        border-bottom-right-radius: 5px;
    }

    .bot-message {
        background-color: #ffffff;
        margin-right: auto;
        border-bottom-left-radius: 5px;
        border: 1px solid #e0e0e0;
    }

    .chatbot-footer {
        border-top: 1px solid #ddd;
        padding: 10px;
        background-color: #fff;
    }

    .quick-replies {
        display: flex;
        flex-wrap: wrap;
        gap: 5px;
        margin-bottom: 8px;
    }

    .quick-replies button {
        flex: 1 1 auto;
        padding: 5px 10px;
        font-size: 12px;
        background-color: #f0f0f0;
        border: 1px solid #ccc;
        border-radius: 4px;
        cursor: pointer;
    }

    .chat-input-area {
        display: flex;
    }

    .chat-input-area input {
        flex-grow: 1;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 4px 0 0 4px;
    }

    .chat-input-area .send-button {
        padding: 8px 12px;
        border: 1px solid #007bff;
        background-color: #007bff;
        color: white;
        border-radius: 0 4px 4px 0;
        cursor: pointer;
    }

    .typing-indicator {
        display: inline-block;
        padding: 8px 12px;
        background-color: #f0f0f0;
        border-radius: 15px;
        margin-bottom: 10px;
    }

    .typing-dot {
        display: inline-block;
        width: 8px;
        height: 8px;
        background-color: #666;
        border-radius: 50%;
        margin-right: 3px;
        animation: typingAnimation 1.4s infinite ease-in-out;
    }

    .typing-dot:nth-child(1) {
        animation-delay: 0s;
    }
    .typing-dot:nth-child(2) {
        animation-delay: 0.2s;
    }
    .typing-dot:nth-child(3) {
        animation-delay: 0.4s;
    }

    @keyframes typingAnimation {
        0%, 60%, 100% {
            transform: translateY(0);
        }
        30% {
            transform: translateY(-5px);
        }
    }
</style>

<button id="chatbot-toggle" class="chatbot-toggle-button">
    <i class="fas fa-comment-dots"></i>
</button>

<div id="chatbot-window" class="chatbot-window hidden">
    <div class="chatbot-header">
        <div class="assistant-info">
            <h3><i class="fas fa-robot"></i> RamBap76 Assistant</h3>
            <p>Trợ lý AI thông minh cho quản lý RamBap76</p>
        </div>
        <div class="status">
            <span class="status-dot"></span> Online
        </div>
    </div>

    <div class="chat-messages" id="chat-messages">
        <div class="message bot-message">
            <p>Xin chào! Tôi là RamBap76, trợ lý ảo của bạn. Tôi có thể giúp gì cho bạn hôm nay?</p>
        </div>
    </div>

    <div class="chatbot-footer">
        <div class="quick-replies">
            <button onclick="sendQuickReply('Trạng thái đơn hàng')">Trạng thái đơn hàng</button>
            <button onclick="sendQuickReply('Thông tin shipper')">Thông tin shipper</button>
            <button onclick="sendQuickReply('Công nợ')">Công nợ</button>
            <button onclick="sendQuickReply('Vị trí giao hàng')">Vị trí giao hàng</button>
        </div>
        <div class="chat-input-area">
            <input type="text" placeholder="Nhập tin nhắn của bạn..." id="chat-input">
            <button class="send-button" id="send-button"><i class="fas fa-paper-plane"></i></button>
        </div>
    </div>
</div>

<script>
    // Script cho chatbot
    const chatbotToggle = document.getElementById('chatbot-toggle');
    const chatbotWindow = document.getElementById('chatbot-window');
    const sendButton = document.getElementById('send-button');
    const chatInput = document.getElementById('chat-input');
    const chatMessages = document.getElementById('chat-messages');

    chatbotToggle.addEventListener('click', () => {
        chatbotWindow.classList.toggle('hidden');
    });

    sendButton.addEventListener('click', sendMessage);
    chatInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter')
            sendMessage();
    });

    function sendQuickReply(text) {
        chatInput.value = text;
        sendMessage();
    }

    function addBotMessage(text) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message bot-message';
        messageDiv.innerHTML = "<p>" + escapeHtml(text) + "</p>";
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function showTypingIndicator() {
        const typingDiv = document.createElement('div');
        typingDiv.id = 'typing-indicator';
        typingDiv.className = 'typing-indicator';
        typingDiv.innerHTML = `
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
        `;
        chatMessages.appendChild(typingDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function hideTypingIndicator() {
        const typingDiv = document.getElementById('typing-indicator');
        if (typingDiv) {
            typingDiv.remove();
        }
    }

    function sendMessage() {
        const text = chatInput.value.trim();
        if (text === '')
            return;

        addUserMessage(text);
        chatInput.value = '';

        showTypingIndicator();

        fetch('${pageContext.request.contextPath}/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: "message=" + encodeURIComponent(text)
        })
                .then(response => response.json())
                .then(data => {
                    hideTypingIndicator();
                    if (data.response) {
                        addBotMessage(data.response);
                    } else if (data.error) {
                        addBotMessage("Xin lỗi, có lỗi xảy ra: " + data.error);
                    }
                })
                .catch(error => {
                    hideTypingIndicator();
                    addBotMessage("Không thể kết nối đến AI. Vui lòng thử lại sau.");
                    console.error('Error:', error);
                });
    }
    function clientEscapeHtml(unsafeText) {
        const div = document.createElement('div');
        div.textContent = unsafeText;
        return div.innerHTML;
    }
    // Hàm escape HTML bằng JavaScript thuần
    function escapeHtml(unsafeText) {
        if (!unsafeText)
            return '';
        return unsafeText.toString()
                .replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
    }

    function addUserMessage(text) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message user-message';
        messageDiv.innerHTML = '<p>' + escapeHtml(text) + '</p>';
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

</script>