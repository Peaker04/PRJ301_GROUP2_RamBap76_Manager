<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Dữ liệu giả lập cho JSP
    String companyName = "Kanky Store";
    String userName = "Guy Hawkins";
    String userRole = "Admin";
    int mailCount = 2;
    int notificationCount = 8;

    // Dữ liệu giả lập cho trang Profile
    String profileFirstName = "Cameron";
    String profileLastName = "Williamson";
    String profileEmail = "cameron@example.com";
    String profileGender = "Male";
    String profileBirthday = "23 Desember 2003";
    String profilePhone = "+62 847 1723 1123";
    String profileAddress = "Parungkuda, Kab. Sukabumi";
    String profileCountry = "Indonesia";
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

        <%-- =================================== --%>
        <%--       MAIN CONTENT (Thay đổi)       --%>
        <%-- =================================== --%>
        <main class="main-content">
            <header class="navbar">
                <div class="search-bar">
                    <input type="text" placeholder="Search product">
                    <i class="fas fa-search"></i>
                </div>
                
                <div class="navbar-right">
                    <button class="icon-button">
                        <i class="fas fa-envelope"></i>
                        <% if (mailCount > 0) { %>
                            <span class="notification-badge"><%= mailCount %></span>
                        <% } %>
                    </button>
                    <button class="icon-button">
                        <i class="fas fa-bell"></i>
                        <% if (notificationCount > 0) { %>
                             <span class="notification-badge"><%= notificationCount %></span>
                        <% } %>
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
            
            <%-- PHẦN NỘI DUNG CHÍNH CỦA TRANG PROFILE --%>
            <div class="page-content">
                 <div class="page-header">
                    <h1>Account & Settings</h1>
                    <p class="breadcrumb">Dashboard &gt; <span class="active-breadcrumb">Profile</span></p>
                </div>
                
                <nav class="tabs-nav">
                    <a href="#" class="tab-link active">Account</a>
                    <a href="#" class="tab-link">Security</a>
                    <a href="#" class="tab-link">Notification</a>
                </nav>

                <div class="profile-content">
                    <section class="card">
                        <h2 class="card-title">Profile Information</h2>
                        <form>
                            <div class="profile-picture-section">
                                <img src="https://i.imgur.com/sC4p1xJ.png" alt="Profile Avatar" class="profile-avatar">
                                <button type="button" class="btn btn-icon">
                                    Change Pictures
                                    <i class="fas fa-pencil-alt"></i>
                                </button>
                            </div>
                            <div class="form-grid">
                                <div class="form-group">
                                    <label for="first-name">First Name</label>
                                    <input type="text" id="first-name" class="form-control" value="<%= profileFirstName %>">
                                </div>
                                <div class="form-group">
                                    <label for="last-name">Last Name</label>
                                    <input type="text" id="last-name" class="form-control" value="<%= profileLastName %>">
                                </div>
                                <div class="form-group form-group-full">
                                    <label for="email">Email</label>
                                    <input type="email" id="email" class="form-control" value="<%= profileEmail %>">
                                </div>
                                <div class="form-group">
                                    <label for="gender">Gender</label>
                                    <input type="text" id="gender" class="form-control" value="<%= profileGender %>">
                                </div>
                                <div class="form-group">
                                    <label for="birthday">Date Birthday</label>
                                    <input type="text" id="birthday" class="form-control" value="<%= profileBirthday %>">
                                </div>
                            </div>
                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary">Update</button>
                                <button type="button" class="btn btn-secondary">Cancel</button>
                            </div>
                        </form>
                    </section>

                    <section class="card">
                         <div class="card-header">
                            <h2 class="card-title">Contact Detail</h2>
                            <button type="button" class="btn btn-icon">
                                Edit
                                <i class="fas fa-pencil-alt"></i>
                            </button>
                        </div>
                        <div class="form-grid-contact">
                            <div class="form-group">
                                <label for="phone">Phone Number</label>
                                <input type="text" id="phone" class="form-control" value="<%= profilePhone %>" disabled>
                            </div>
                            <div class="form-group">
                                <label for="country">Country</label>
                                <input type="text" id="country" class="form-control" value="<%= profileCountry %>" disabled>
                            </div>
                             <div class="form-group form-group-full">
                                <label for="address">Address</label>
                                <input type="text" id="address" class="form-control" value="<%= profileAddress %>" disabled>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </main>
    </div>
</body>
</html>