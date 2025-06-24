<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head><title>Admin Dashboard</title></head>
    <body>
        <h1>Chào Mừng Admin, ${sessionScope.user.fullName}!</h1>
        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
    </body>
</html>