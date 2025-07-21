<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Include Bootstrap Icons & Google Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_layout.css">


<!-- Gán URI hiện tại -->
<c:set var="uri" value="${pageContext.request.requestURI}" />

<div class="admin-layout">
    <!-- Sidebar -->
    <div class="sidebar">
        <div style="font-size: 16px; font-weight: bold; margin-bottom: 32px; color: #3f51b5;">
            <img src="${pageContext.request.contextPath}/image/Login_logo.jpg" alt="Logo" style="height: 30px; vertical-align: middle;"/>RamBap76_Manager
        </div>

        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="${fn:contains(uri, '/admin/dashboard') ? 'active' : ''}">
            <i class="bi bi-speedometer2"></i> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/admin/orders"
           class="${fn:contains(uri, '/admin/orders') ? 'active' : ''}">
            <i class="bi bi-box-seam"></i> Orders
        </a>
        <a href="${pageContext.request.contextPath}/admin/customers"
           class="${fn:contains(uri, '/admin/customers') ? 'active' : ''}">
            <i class="bi bi-people"></i> Customers
        </a>
        <a href="${pageContext.request.contextPath}/admin/receipts"
           class="${fn:contains(uri, '/admin/receipt') ? 'active' : ''}">
            <i class="bi bi-receipt"></i> Receipt
        </a>
        <a href="${pageContext.request.contextPath}/admin/products"
           class="${fn:contains(uri, '/admin/products') ? 'active' : ''}">
            <i class="bi bi-box2-heart"></i> Product
        </a>
        <a href="${pageContext.request.contextPath}/profile"
           class="${fn:contains(uri, '/profile') ? 'active' : ''}">
            <i class="bi bi-speedometer2"></i> Account & Settings
        </a>
    </div>

    <!-- Main content -->
    <div class="main-content">
        <!-- Header -->
        <div class="header">
            <input type="text" placeholder="Search..."
                   style="padding: 8px 12px; width: 300px; border: 1px solid #ddd; border-radius: 6px;">
            <div class="user-info">
                <img src="${pageContext.request.contextPath}/image/avatar-default.png" alt="Avatar"> <%--Chỉnh profile để get được avt người dùng--%>
                <div>
                    <strong>${sessionScope.userProfile.last_name}</strong><br/>
                    <small>Admin</small>
                </div>
                <a href="${pageContext.request.contextPath}/logout" style="margin-left: 10px; color: red;">Log out</a>
            </div>
        </div>

        <!-- Content Section -->
        <div class="content-area">
            <jsp:include page="${contentPage}" />
        </div>
    </div>
</div>
