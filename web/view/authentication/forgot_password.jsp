<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Quên Mật Khẩu</title>
    </head>
    <body>
        <div class="container">
            <h2>Quên Mật Khẩu</h2>
            <p>Nhập tên đăng nhập của bạn. Nếu tồn tại, một đường dẫn đặt lại mật khẩu sẽ được gửi (trong thực tế sẽ gửi qua email).</p>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-info">${message}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/forgot-password" method="post">
                <div class="form-group">
                    <label for="username">Tên đăng nhập:</label>
                    <input type="text" id="username" name="username" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary">Gửi Yêu Cầu</button>
                <a href="${pageContext.request.contextPath}/login">Quay lại Đăng nhập</a>
            </form>
        </div>
    </body>
</html>