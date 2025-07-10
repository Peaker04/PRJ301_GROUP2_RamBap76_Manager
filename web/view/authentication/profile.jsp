<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Dữ liệu giả lập cho JSP - Giữ nguyên để cấu trúc trang
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
    <title>Profile - Culters Dashboard</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body>
    <div class="dashboard-container">
        <aside class="sidebar">
            <div class="sidebar-top">
                <div class="logo">
                     <h1 style="font-size: 24px; font-weight: 700; color: #333;">Culters</h1>
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
                            <li><a href="#" class="nav-item"><i class="fas fa-home"></i> Dashboard</a></li>
                            <li class="nav-item-dropdown">
                                <a href="#" class="nav-item"><i class="fas fa-box-archive"></i> Product (119) <i class="fas fa-chevron-up dropdown-arrow"></i></a>
                                <ul class="sub-menu" style="display: block;">
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
                            <%-- Đặt class 'active' cho mục Account & Settings --%>
                            <li><a href="#" class="nav-item active"><i class="fas fa-cog"></i> Account & Settings</a></li>
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
             <div class="page-content">
                <div class="profile-content">
                    <%-- Form sẽ gửi dữ liệu đến ProfileServlet qua phương thức POST --%>
                    <form action="${pageContext.request.contextPath}/profile" method="post">
                        <section class="card">
                            <h2 class="card-title">Profile Information</h2>
                            
                            <%-- Thông báo cập nhật --%>
                            <c:if test="${not empty message}">
                                <p style="color: green;">${message}</p>
                            </c:if>
                            <c:if test="${not empty error}">
                                <p style="color: red;">${error}</p>
                            </c:if>

                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="first-name">First Name</label>
                                    <input type="text" id="first-name" name="first_name" class="form-control" value="${profile.firstName}">
                                </div>
                                <div class="form-group">
                                    <label for="last-name">Last Name</label>
                                    <input type="text" id="last-name" name="last_name" class="form-control" value="${profile.lastName}">
                                </div>
                                <div class="form-group form-group-full">
                                    <label for="email">Email</label>
                                    <input type="email" id="email" name="email" class="form-control" value="${profile.email}">
                                </div>
                                <div class="form-group">
                                    <label for="gender">Gender</label>
                                    <input type="text" id="gender" name="gender" class="form-control" value="${profile.gender}">
                                </div>
                            </div>
                        </section>

                        <section class="card">
                             <div class="card-header">
                                <h2 class="card-title">Contact Detail</h2>
                             </div>
                            <div class="form-grid-contact">
                                <div class="form-group">
                                    <label for="phone">Phone Number</label>
                                    <%-- Dùng thẻ input để hiển thị, có thể cho phép sửa ở đây --%>
                                    <input type="text" id="phone" name="phone_number" class="form-control" value="${profile.phoneNumber}">
                                </div>
                                <div class="form-group form-group-full">
                                    <label for="address">Address</label>
                                    <input type="text" id="address" name="address" class="form-control" value="${profile.address}">
                                </div>
                            </div>
                        </section>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Update</button>
                            <button type="button" class="btn btn-secondary">Cancel</button>
                        </div>
                    </form>
                </div>
            </div>
        </main>

    </div>
</body>
</html>