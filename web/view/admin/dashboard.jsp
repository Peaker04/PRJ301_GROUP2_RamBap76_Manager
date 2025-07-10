<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="dashboard-container" style="max-width: 1200px;">
    <h2 class="welcome">Chào Mừng Admin, ${sessionScope.user.fullName}!</h2>
    <div class="dashboard-content">
        <div class="dashboard-main">
            <div class="dashboard-chart-box">
                <h4>NUMBER OF ORDERS BY MONTH</h4>
                <canvas id="ordersByMonthChart"></canvas>
            </div>
            <div class="dashboard-charts-row">
                <div class="dashboard-donut-box">
                    <h4>Products sold this month</h4>
                    <canvas id="productsSoldDonut"></canvas>
                </div>
                <div class="dashboard-donut-box">
                    <h4>Inventory</h4>
                    <canvas id="inventoryDonut"></canvas>
                </div>
            </div>
        </div>
        <div class="dashboard-side">
            <h4>Top Users</h4>
            <ol class="top-users-list">
                <c:forEach var="u" items="${topUsers}" varStatus="i">
                    <li>
                        <img src="${pageContext.request.contextPath}/images/avatar-default.png" class="top-user-avatar"/>
                        <div>
                            <span class="top-user-name">${u.name}</span><br>
                            <span class="top-user-orders">${u.totalOrders} orders</span>
                        </div>
                    </li>
                </c:forEach>
            </ol>
        </div>
    </div>
</div>

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
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, ticks: { stepSize: 50 } }
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
            datasets: [{ data: soldData }]
        }
    });

    // Inventory Donut
    const invLabels = JSON.parse('${invLabelsJson}');
    const invData = JSON.parse('${invDataJson}');
    new Chart(document.getElementById('inventoryDonut'), {
        type: 'doughnut',
        data: {
            labels: invLabels,
            datasets: [{ data: invData }]
        }
    });
</script>