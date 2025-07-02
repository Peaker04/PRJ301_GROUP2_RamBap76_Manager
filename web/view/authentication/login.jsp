<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Culters - Sign In</title>
        <%-- Đường dẫn đến CSS (Đúng) --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    </head>
    <body>
        <main class="login-container">
            <section class="form-column">
                <div class="form-content">

                    <header class="logo">
                        <%-- Đường dẫn đến ảnh (Đúng) --%>
                        <img src="${pageContext.request.contextPath}/image/Login_logo.jpg" alt="Culters Logo" style="height: 56px; vertical-align: middle;">
                        <span class="logo-text">RamBap76</span>
                    </header>

                    <div class="form-body">
                        <div class="form-header">
                            <h1>Sign In</h1>
                        </div>

                        <c:if test="${not empty errorMessage}">
                            <div class="error-message">${errorMessage}</div>
                        </c:if>

                        <%-- Form action trỏ đến LoginServlet (Đúng) --%>
                        <form action="${pageContext.request.contextPath}/login" method="POST" class="login-form">

                            <button type="button" class="btn btn-google">
                                <span class="icon">
                                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M21.818 10.045h-9.363v4.364h5.455c-.273 1.455-1.182 2.636-2.545 3.545v2.818h3.545c2.091-1.909 3.273-4.636 3.273-7.727 0-.727-.091-1.364-.182-2z" fill="#4285F4"></path><path d="M12.455 22c2.727 0 5-1 6.545-2.636l-3.545-2.818c-.818.545-2 1-3 1-2.273 0-4.182-1.545-4.818-3.636H4.273v2.909C5.91 20.273 8.91 22 12.455 22z" fill="#34A853"></path><path d="M7.636 13.091c-.182-.545-.273-1.182-.273-1.818s.091-1.273.273-1.818V6.545H4.273C3.455 8.182 3 10.091 3 12s.455 3.818 1.273 5.455l3.363-2.909z" fill="#FBBC05"></path><path d="M12.455 4.545c1.455 0 2.636.545 3.636 1.455l3.182-3.182C17.455 1.182 15.182 0 12.455 0 8.91 0 5.91 1.727 4.273 4.455l3.363 2.909C8.273 5.545 10.182 4.545 12.455 4.545z" fill="#EA4335"></path></svg>
                                </span>
                                Sign In with Google
                            </button>

                            <div class="separator">
                                <hr>
                                <span>OR</span>
                                <hr>
                            </div>

                            <div class="form-group">
                                <label for="username">Username</label>
                                <div class="input-wrapper">
                                    <input type="text" id="username" name="username" class="input-field" placeholder="Username" required value="${param.username}">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password">Password</label>
                                <div class="input-wrapper">
                                    <input type="password" id="password" name="password" class="input-field" placeholder="Password" required>
                                    <span class="input-icon toggle-password">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>
                                    </span>
                                </div>
                            </div>

                            <div class="form-actions">
                                <div class="remember-me">
                                    <input type="checkbox" id="remember-me" name="remember">
                                    <label for="remember-me">Remember me ?</label>
                                </div>
                                <a href="${pageContext.request.contextPath}/forgot-password" class="forgot-password">Forgot Password</a>
                            </div>

                            <div class="form-cta">
                                <button type="submit" class="btn btn-primary">Sign In</button>
                                <%-- ĐƯỜNG DẪN QUAN TRỌNG: Đã được sửa để trỏ đúng vị trí file signup.jsp --%>
                                <p class="signup-link">Do not have an account? <a href="${pageContext.request.contextPath}/view/authentication/signup.jsp">Sign Up</a></p>
                            </div>

                        </form>
                    </div>

                </div>
            </section>

            <section class="promo-column">
                <%-- Đường dẫn đến ảnh (Đúng) --%>
                <img src="${pageContext.request.contextPath}/image/dashboard.png" alt="Dashboard Preview" class="dashboard-image">
                <div class="promo-text">
                    <h2>Easy-to-Use Dashboard for Managing Your Business.</h2>
                    <p>Streamline Your Business Management with Our User-Friendly Dashboard. Simplify complex tasks, track key metrics, and make informed decisions effortlessly</p>
                </div>
                <div class="pagination-dots">
                    <div class="dot active"></div>
                    <div class="dot"></div>
                    <div class="dot"></div>
                </div>  
            </section>
        </main>


        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const eyeIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>`;
                const eyeOffIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9.88 9.88a3 3 0 1 0 4.24 4.24"/><path d="M10.73 5.08A10.43 10.43 0 0 1 12 5c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68"/><path d="M6.61 6.61A13.526 13.526 0 0 0 2 12s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61"/><line x1="2" x2="22" y1="2" y2="22"/></svg>`;

                const togglePasswordIcons = document.querySelectorAll('.toggle-password');

                togglePasswordIcons.forEach(icon => {
                    icon.addEventListener('click', function () {
                        const passwordInput = this.previousElementSibling;
                        if (passwordInput.type === 'password') {
                            passwordInput.type = 'text';
                            this.innerHTML = eyeOffIcon;
                        } else {
                            passwordInput.type = 'password';
                            this.innerHTML = eyeIcon;
                        }
                    });
                });
            });
        </script>
    </body>
</html>