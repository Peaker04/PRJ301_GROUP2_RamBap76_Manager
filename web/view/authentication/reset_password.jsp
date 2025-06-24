<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Đặt Lại Mật Khẩu</title>
    </head>
    <body>
        <div class="container">
            <h2>Đặt Lại Mật Khẩu Mới</h2>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reset-password" method="post">
                <%-- Trường ẩn để gửi lại token --%>
                <input type="hidden" name="token" value="${token}">

                <div class="form-group">
                    <label for="newPassword">Mật khẩu mới:</label>
                    <input type="password" id="newPassword" name="newPassword" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary">Đặt Lại Mật Khẩu</button>
            </form>
        </div>
    </body>
</html>