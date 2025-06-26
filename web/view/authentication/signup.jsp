<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Culters - Create Your Account</title>
        <%-- Đường dẫn đến CSS (Đúng) --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    </head>
    <body>
        <main class="signup-container">
            <section class="form-column">
                <div class="form-content">
                    <header class="logo">
                        <%-- Đường dẫn đến ảnh (Đúng) --%>
                        <img src="${pageContext.request.contextPath}/image/Login_logo.jpg" alt="Culters Logo" style="height: 56px; vertical-align: middle;">
                        <span class="logo-text">RamBap76</span>
                    </header>
                    <div class="form-body">
                        <div class="form-header">
                            <h1>Create Your Account</h1>
                            <%-- ĐƯỜNG DẪN QUAN TRỌNG: Trỏ về LoginServlet tại URL /login (Đúng) --%>
                            <p class="login-prompt">Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
                        </div>
                        <c:if test="${not empty errorMessage}">
                            <div class="error-message">${errorMessage}</div>
                        </c:if>

                        <%-- ACTION QUAN TRỌNG: Gửi dữ liệu đến SignupServlet tại URL /signup (Đúng) --%>
                        <form action="${pageContext.request.contextPath}/signup" method="POST" class="signup-form">

                            <div class="form-group">
                                <label>You're creating an account as?</label>
                                <div class="radio-group">
                                    <div class="radio-option">
                                        <input type="radio" id="admin" name="accountType" value="admin" checked>
                                        <label for="admin">Admin</label>
                                    </div>
                                    <div class="radio-option">
                                        <input type="radio" id="shipper" name="accountType" value="shipper">
                                        <label for="shipper">Shipper</label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="fullName">Full Name</label>
                                <div class="input-wrapper">
                                    <input type="text" id="fullName" name="fullName" class="input-field" placeholder="Enter your fullname" required value="${param.fullName}">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="username">Username</label>
                                <div class="input-wrapper">
                                    <input type="text" id="username" name="username" class="input-field" placeholder="Enter your username" required value="${param.username}">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="password">Password</label>
                                <div class="input-wrapper">
                                    <input type="password" id="password" name="password" class="input-field" placeholder="Create your password" required>
                                    <span class="input-icon toggle-password">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>
                                    </span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="confirmPassword">Confirm Password</label>
                                <div class="input-wrapper">
                                    <input type="password" id="confirmPassword" name="confirmPassword" class="input-field" placeholder="Confirm Password" required>
                                    <span class="input-icon toggle-password">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>
                                    </span>
                                </div>
                            </div>

                            <div class="form-group terms-agreement">
                                <input type="checkbox" id="terms" name="terms" checked required>
                                <label for="terms">By creating an account, I agree to our <a href="#">Terms of use</a> and <a href="#">Privacy Policy</a></label>
                            </div>

                            <div class="form-cta">
                                <button type="submit" class="btn btn-primary">Create Account</button>
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