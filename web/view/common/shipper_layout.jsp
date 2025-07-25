<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Include Bootstrap Icons & Google Fonts -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/shipper_layout.css">


<!-- Gán URI hiện tại -->
<c:set var="uri" value="${pageContext.request.requestURI}" />

<div class="shipper-layout">
    <!-- Sidebar -->
    <div class="sidebar">
        <div style="font-size: 16px; font-weight: bold; margin-bottom: 32px; color: #3f51b5;">
            <img src="${pageContext.request.contextPath}/image/Login_logo.jpg" alt="Logo" style="height: 30px; vertical-align: middle;"/>RamBap76_Manager
        </div>

        <a href="${pageContext.request.contextPath}/shipper/dashboard"
           class="${fn:contains(uri, '/shipper/dashboard') ? 'active' : ''}">
            <i class="bi bi-speedometer2"></i> Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/shipper/notifications"
           class="${fn:contains(uri, '/shipper/notifications') ? 'active' : ''}">
            <i class="bi bi-box-seam"></i> Notifications
        </a>
        <a href="${pageContext.request.contextPath}/ProfileShipperServlet"
           class="${fn:contains(uri, '/ProfileShipperServlet') ? 'active' : ''}">
            <i class="bi bi-gear"></i> Account & Settings
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
                    <strong>${sessionScope.userProfile.first_name}</strong><br/>
                    <small>Shipper</small>
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
