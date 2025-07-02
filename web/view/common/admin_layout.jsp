<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Include Bootstrap Icons & Google Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

<style>
    body {
        margin: 0;
        font-family: 'Inter', sans-serif;
        background-color: #f9f9f9;
    }

    .admin-layout {
        display: flex;
        height: 100vh;
        overflow: hidden;
    }

    .sidebar {
        width: 260px;
        background-color: #fff;
        border-right: 1px solid #e0e0e0;
        padding: 30px 20px;
        display: flex;
        flex-direction: column;
    }

    .sidebar a {
        text-decoration: none;
        color: #555;
        font-weight: 500;
        display: flex;
        align-items: center;
        padding: 12px 10px;
        border-radius: 8px;
        transition: all 0.2s;
        margin-bottom: 6px;
    }

    .sidebar a.active,
    .sidebar a:hover {
        background-color: #eef2ff;
        color: #3f51b5;
    }

    .sidebar i {
        margin-right: 12px;
        font-size: 18px;
    }

    .main-content {
        flex: 1;
        display: flex;
        flex-direction: column;
    }

    .header {
        height: 80px;
        background-color: #fff;
        border-bottom: 1px solid #e0e0e0;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 24px;
    }

    .header .user-info {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .header img {
        width: 38px;
        height: 38px;
        border-radius: 50%;
        object-fit: cover;
    }

    .content-area {
        padding: 24px;
        overflow-y: auto;
        flex-grow: 1;
    }
</style>

<!-- Gán URI hiện tại -->
<c:set var="uri" value="${pageContext.request.requestURI}" />

<div class="admin-layout">
    <!-- Sidebar -->
    <div class="sidebar">
        <div style="font-size: 16px; font-weight: bold; margin-bottom: 32px; color: #3f51b5;">
            <img src="image/Login_logo.jsp" alt="Logo" style="height: 30px; vertical-align: middle;"/>RamBap76_Manager
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
        <a href="${pageContext.request.contextPath}/admin/receipt"
           class="${fn:contains(uri, '/admin/receipt') ? 'active' : ''}">
            <i class="bi bi-receipt"></i> Receipt
        </a>
        <a href="${pageContext.request.contextPath}/admin/product_stock"
           class="${fn:contains(uri, '/admin/product_stock') ? 'active' : ''}">
            <i class="bi bi-box2-heart"></i> Product Stock
        </a>
        <a href="${pageContext.request.contextPath}/admin/reports"
           class="${fn:contains(uri, '/admin/reports') ? 'active' : ''}">
            <i class="bi bi-graph-up-arrow"></i> Reports
        </a>
        <a href="${pageContext.request.contextPath}/admin/support"
           class="${fn:contains(uri, '/admin/support') ? 'active' : ''}">
            <i class="bi bi-headset"></i> Support
        </a>
        <a href="${pageContext.request.contextPath}/admin/settings"
           class="${fn:contains(uri, '/admin/settings') ? 'active' : ''}">
            <i class="bi bi-gear"></i> Settings
        </a>
    </div>

    <!-- Main content -->
    <div class="main-content">
        <!-- Header -->
        <div class="header">
            <input type="text" placeholder="Search..."
                   style="padding: 8px 12px; width: 300px; border: 1px solid #ddd; border-radius: 6px;">
            <div class="user-info">
                <i class="bi bi-bell"></i>
                <img src="${pageContext.request.contextPath}/image/Login_logo.png" alt="Avatar"> <%--Chỉnh profile để get được avt người dùng--%>
                <div>
                    <strong>${sessionScope.user.fullName}</strong><br/>
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
