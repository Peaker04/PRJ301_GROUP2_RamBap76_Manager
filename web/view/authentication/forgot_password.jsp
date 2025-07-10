<%-- File: view/authentication/forgot_password.jsp --%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<!DOCTYPE html>
<html lang="vi">
<head>
    <%-- (Phần head giữ nguyên như cũ) --%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Your Password</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgotpassword.css">
    <style>
        .message.error {
            background-color: #f8d7da; color: #721c24;
            padding: 1rem; margin-bottom: 1rem; border-radius: 4px;
        }
    </style>
</head>
<body>

    <header class="page-header">
        <div class="logo-icon"></div>
        <span>RamBap76</span>
    </header>

    <main class="reset-container">
        <h1>Reset your password</h1>
        <p>Type in your registered username to reset password</p>

        <%-- Chỉ hiển thị thông báo lỗi ở đây --%>
        <c:if test="${not empty requestScope.errorMessage}">
            <div class="message error">
                <c:out value="${requestScope.errorMessage}" />
            </div>
        </c:if>

        <form action="<c:url value='/forgot-password' />" method="post" class="reset-form">
            <div class="form-group">
                <input type="text" class="form-input" name="username" placeholder="Enter username" required>
            </div>
            
            <button type="submit" class="btn btn-primary">
                <span>Next</span>
                <span class="arrow">&rarr;</span>
            </button>
            
            <a href="<c:url value='/login.jsp' />" class="btn btn-dark">Back to login</a>
        </form>
    </main>

    <footer class="page-footer">
        <a href="#">Terms and conditions</a>
        <span class="separator">&bull;</span>
        <a href="#">Privacy policy</a>
    </footer>

</body>
</html>