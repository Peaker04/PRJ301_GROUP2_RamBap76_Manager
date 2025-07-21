<%-- File: view/authentication/email_sended.jsp --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recovery Email Sent</title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    
    <%-- Sử dụng lại file CSS cũ --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgotpassword.css">
    
    <style>
        /* Style cho phần thông báo thành công */
        .success-message {
            font-size: 16px;
            color: #333; /* Màu chữ dễ đọc */
            margin-bottom: 2rem;
            line-height: 1.5;
        }
    </style>
</head>
<body>

    <header class="page-header">
        <div class="logo-icon"></div>
        <span>RamBap76</span>
    </header>

    <main class="reset-container">
        <h1>Recovery Email Sent!</h1>

        <%-- Lấy thông báo từ session và hiển thị ra --%>
        <c:if test="${not empty sessionScope.successMessage}">
            <p class="success-message">
                <c:out value="${sessionScope.successMessage}" />
            </p>
            <%-- Xóa attribute khỏi session sau khi hiển thị để không bị hiện lại --%>
            <% session.removeAttribute("successMessage"); %>
        </c:if>

        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">CONTACT SUPPORT</a>
        <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-dark">BACK TO LOGIN</a>
    </main>

    <footer class="page-footer">
        <a href="#">Terms and conditions</a>
        <span class="separator">&bull;</span>
        <a href="#">Privacy policy</a>
    </footer>

</body>
</html>