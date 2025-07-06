<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Dữ liệu giả lập cho JSP
    String companyName = "Kanky Store";
    String userName = "Guy Hawkins";
    String userRole = "Admin";
    int mailCount = 2;
    int notificationCount = 8;
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Culters Dashboard</title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700&display=swap" rel="stylesheet">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>
    <div class="dashboard-container">
        <aside class="sidebar">
            <div class="sidebar-top">
                <div class="logo">
                    <img src="https://i.imgur.com/rS6yv2x.png" alt="Culters Logo">
                </div>
                
                <div class="company-card">
                    <img src="https://i.imgur.com/gK2xGZ1.png" alt="Company Logo" class="company-logo">
                    <div class="company-info">
                        <span class="company-label">Company</span>
                        <span class="company-name"><%= companyName %></span>
                    </div>
                </div>

                <nav class="navigation">
                    <div class="nav-group">
                        <h3 class="nav-title">GENERAL</h3>
                        <ul class="nav-list">
                            <li><a href="#" class="nav-item active"><i class="fas fa-home"></i> Dashboard</a></li>
                            <li class="nav-item-dropdown">
                                <a href="#" class="nav-item"><i class="fas fa-box-archive"></i> Product (119) <i class="fas fa-chevron-up dropdown-arrow"></i></a>
                                <ul class="sub-menu">
                                    <li><a href="#">Sneakers</a></li>
                                    <li><a href="#">Jacket</a></li>
                                    <li><a href="#">T-Shirt</a></li>
                                    <li><a href="#">Bag</a></li>
                                </ul>
                            </li>
                            <li><a href="#" class="nav-item"><i class="fas fa-exchange-alt"></i> Transaction (441)</a></li>
                            <li><a href="#" class="nav-item"><i class="fas fa-users"></i> Customers</a></li>
                            <li><a href="#" class="nav-item"><i class="fas fa-chart-line"></i> Sales Report</a></li>
                        </ul>
                    </div>

                    <div class="nav-group">
                        <h3 class="nav-title">TOOLS</h3>
                        <ul class="nav-list">
                            <li><a href="${pageContext.request.contextPath}/view/authentication/profile.jsp" class="nav-item"><i class="fas fa-cog"></i> Account & Settings</a></li>
                            <li><a href="#" class="nav-item"><i class="fas fa-question-circle"></i> Help</a></li>
                            <li class="nav-item-toggle">
                                <div class="nav-item-label">
                                    <i class="fas fa-moon"></i>
                                    <span>Dark Mode</span>
                                </div>
                                <label class="switch">
                                    <input type="checkbox">
                                    <span class="slider"></span>
                                </label>
                            </li>
                        </ul>
                    </div>
                </nav>
            </div>
            
            <div class="sidebar-bottom">
                <div class="user-profile">
                    <img src="https://i.imgur.com/Xce8p35.png" alt="User Avatar" class="user-avatar">
                    <div class="user-details">
                        <span class="user-name"><%= userName %></span>
                        <span class="user-role"><%= userRole %></span>
                    </div>
                    <i class="fas fa-chevron-down"></i>
                </div>
            </div>
        </aside>

        <main class="main-content">
            <header class="navbar">
                <div class="search-bar">
                    <input type="text" placeholder="Search product">
                    <i class="fas fa-search"></i>
                </div>
                
                <div class="navbar-right">
                    <button class="icon-button">
                        <i class="fas fa-envelope"></i>
                        <span class="notification-badge"><%= mailCount %></span>
                    </button>
                    <button class="icon-button">
                        <i class="fas fa-bell"></i>
                        <span class="notification-badge"><%= notificationCount %></span>
                    </button>
                    
                    <div class="nav-user-profile">
                        <div class="avatar-container">
                           <img src="https://i.imgur.com/Xce8p35.png" alt="User Avatar" class="user-avatar">
                           <span class="status-indicator"></span>
                        </div>
                        <div class="user-details">
                            <span class="user-name"><%= userName %></span>
                            <span class="user-role"><%= userRole %></span>
                        </div>
                    </div>
                </div>
            </header>
            
            <div class="page-content">
                </div>
        </main>
    </div>

    <button id="chatbot-toggle" class="chatbot-toggle-button">
        <i class="fas fa-comment-dots"></i>
    </button>

    <div id="chatbot-window" class="chatbot-window hidden">
        <div class="chatbot-header">
            <button class="back-button"><i class="fas fa-arrow-left"></i></button>
            <div class="assistant-info">
                <h3><i class="fas fa-robot"></i> RamBap76 Assistant</h3>
                <p>Trợ lý AI thông minh cho quản lý RamBap76</p>
            </div>
            <div class="status">
                <span class="status-dot"></span> Online
            </div>
        </div>

        <div class="chat-messages">
            <div class="chat-message bot">
                <div class="message-content">
                    <div class="suggestion">Đánh giá khách hàng</div>
                    <p>Bạn cần hỗ trợ gì?</p>
                </div>
            </div>

            <div class="chat-message user">
                <div class="message-content">
                    <p>Trạng thái đơn hàng</p>
                </div>
            </div>
             <div class="chat-message bot">
                <div class="message-content">
                    <p>Chào bạn, RamBap76 đây! Bạn muốn biết trạng thái đơn hàng nào nè? Cho mình xin mã đơn hàng hoặc thông tin liên quan để mình kiểm tra giúp bạn nhé! 😊</p>
                </div>
            </div>
             <div class="chat-message user">
                <div class="message-content">
                    <p>Tạ Tuấn Kỳ hẹ hẹ</p>
                </div>
            </div>
            <div class="chat-message bot">
                <div class="message-content">
                   <p>Chào bạn! RamBap76 đây, rất vui được làm quen. 😊</p>
                </div>
            </div>
        </div>

        <div class="chatbot-footer">
            <div class="quick-replies">
                <button>Trạng thái đơn hàng</button>
                <button>Thông tin shipper</button>
                <button>Công nợ</button>
                <button>Vị trí giao hàng</button>
            </div>
            <div class="chat-input-area">
                <input type="text" placeholder="Nhập tin nhắn của bạn...">
                <button class="send-button"><i class="fas fa-paper-plane"></i></button>
            </div>
        </div>
    </div>
    <script>
        const chatbotToggle = document.getElementById('chatbot-toggle');
        const chatbotWindow = document.getElementById('chatbot-window');

        chatbotToggle.addEventListener('click', () => {
            chatbotWindow.classList.toggle('hidden');
        });
    </script>
</body>
</html>