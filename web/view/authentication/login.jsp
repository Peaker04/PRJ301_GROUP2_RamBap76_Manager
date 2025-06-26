<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng Nhập</title>

        <%-- 
            CÁCH 1: DÙNG CDN (NHANH VÀ DỄ NHẤT)
            Thêm link CSS của Bootstrap 5 qua CDN. Cách này yêu cầu có kết nối internet khi chạy.
        --%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

        <%-- 
            Thêm link đến file CSS tùy chỉnh của dự án.
            File này nên được đặt sau file Bootstrap để có thể ghi đè các style mặc định.
            Sử dụng ${pageContext.request.contextPath} để đảm bảo đường dẫn luôn đúng.
        --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">

        <style>
            /* Bạn có thể thêm một vài style tùy chỉnh nhanh ngay tại đây nếu muốn */
            body {
                display: flex;
                align-items: center;
                justify-content: center;
                height: 100vh;
                background-color: #f8f9fa;
            }
            .container {
                max-width: 400px;
                padding: 2rem;
                border: 1px solid #dee2e6;
                border-radius: 0.5rem;
                background-color: #fff;
                box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15);
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2 class="text-center mb-4">Đăng Nhập Hệ Thống</h2>

            <%-- Hiển thị thông báo lỗi --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <%-- Hiển thị thông báo thành công --%>
            <c:if test="${param.message == 'reset_success'}">
                <div class="alert alert-success">Đặt lại mật khẩu thành công! Vui lòng đăng nhập.</div>
            </c:if>
            <c:if test="${param.message == 'changepass_success'}">
                <div class="alert alert-success">Đổi mật khẩu thành công! Vui lòng đăng nhập lại.</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Tên đăng nhập:</label>
                    <input type="text" id="username" name="username" class="form-control" required value="${param.username}"> <%-- Giữ lại tên đăng nhập nếu đăng nhập sai --%>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Mật khẩu:</label>
                    <input type="password" id="password" name="password" class="form-control" required>
                </div>
                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Đăng Nhập</button>
                </div>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
                </div>
            </form>
        </div>

        <%-- 
            Thêm link JS của Bootstrap 5 (bao gồm Popper.js).
            Đặt ở cuối trang để tăng tốc độ tải trang.
        --%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>