<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="container-xxl px-3 py-3" style="max-width:1200px; max-height: calc(100vh - 120px);">
    <!-- Welcome -->
    <div class="mb-2">
        <h3 class="fw-bold welcome">Xin chào, ${sessionScope.userProfile.last_name}!</h3>
    </div>

    <div class="row g-3">
        <!-- Left row -->
        <div class="col-lg-9 col-md-12 d-flex flex-column gap-3">
            <!-- Chart -->
            <div class="dash-card chart-line flex-grow-1 p-3">
                <div class="mb-1 text-center fw-semibold fs-5">Number of Orders by Month</div>
                <canvas id="ordersByMonthChart" style="height:350px;max-height:500px;"></canvas>
            </div>
            <!-- 2 Donut charts -->
            <div class="row g-3"> 
                <div class="col-6">
                    <div class="dash-card p-2">
                        <div class="text-center fw-semibold">Products sold this month</div>
                        <canvas id="productsSoldDonut" style="height:120px;max-height:200px;"></canvas>
                    </div>
                </div>
                <div class="col-6">
                    <div class="dash-card p-2">
                        <div class="text-center fw-semibold">Inventory</div>
                        <canvas id="inventoryDonut" style="height:120px;max-height:200px;"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <!-- Top Users -->
        <div class="col-lg-3 col-md-12">
            <div class="dash-card h-100 p-3">
                <div class="text-center fw-semibold fs-5 mb-2">Top Users</div>
                <ol class="top-users-list ps-0 mb-0">
                    <c:forEach var="u" items="${topUsers}">
                        <li class="d-flex align-items-center mb-2 gap-2">
                            <img src="${pageContext.request.contextPath}/image/avatar-default.png" class="top-user-avatar"/>
                            <div>
                                <span class="top-user-name">${u.name}</span><br> 
                                <span class="top-user-orders text-muted">${u.totalOrders} orders</span>
                            </div>
                        </li>
                    </c:forEach>
                </ol>
            </div>
        </div>
    </div>
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

<script>
    // Orders by Month
    const ordersMonthLabels = JSON.parse('${ordersByMonthLabelsJson}');
    const ordersMonthData = JSON.parse('${ordersByMonthDataJson}');

    new Chart(document.getElementById('ordersByMonthChart'), {
        type: 'line',
        data: {
            labels: ordersMonthLabels,
            datasets: [{
                    label: 'Orders',
                    data: ordersMonthData,
                    borderColor: '#9ff7fa',
                    backgroundColor: 'rgba(159,247,250,0.2)',
                    fill: true,
                    tension: 0.2
                }]
        },
        options: {
            plugins: {legend: {display: false}},
            scales: {
                y: {beginAtZero: true, ticks: {stepSize: 50}}
            }
        }
    });

    // Products Sold This Month Donut
    const soldLabels = JSON.parse('${soldLabelsJson}');
    const soldData = JSON.parse('${soldDataJson}');
    new Chart(document.getElementById('productsSoldDonut'), {
        type: 'doughnut',
        data: {
            labels: soldLabels,
            datasets: [{data: soldData}]
        }
    });

    // Inventory Donut
    const invLabels = JSON.parse('${invLabelsJson}');
    const invData = JSON.parse('${invDataJson}');
    new Chart(document.getElementById('inventoryDonut'), {
        type: 'doughnut',
        data: {
            labels: invLabels,
            datasets: [{data: invData}]
        }
    });
</script>