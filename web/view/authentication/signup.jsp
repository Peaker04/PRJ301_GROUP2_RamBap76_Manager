<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Culters - Create Your Account</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    </head>
    <body>
        <main class="signup-container">
            <section class="form-column">
                <div class="form-content">
                    <header class="logo">
                        <img src="${pageContext.request.contextPath}/image/Login_logo.jpg" alt="Culters Logo" style="height: 56px; vertical-align: middle;">
                        <span class="logo-text">RamBap76</span>
                    </header>
                    <div class="form-body">
                        <div class="form-header">
                            <h1>Create Your Account</h1>
                            <p class="login-prompt">Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
                        </div>
                        <c:if test="${not empty errorMessage}">
                            <div class="error-message">${errorMessage}</div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/signup" method="POST" class="signup-form">

                            <%-- ... các form-group khác không thay đổi ... --%>
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
                                <label for="email">Email</label>
                                <div class="input-wrapper">
                                    <input type="email" id="email" name="email" class="input-field" placeholder="Enter your email" required value="${param.email}">
                                </div>
                            </div>

                            <!-- Password -->
                            <div class="form-group">
                                <label for="password">Password</label>
                                <div class="input-wrapper">
                                    <input type="password" id="password" name="password" class="input-field" placeholder="Create your password" required>
                                    <span class="input-icon toggle-password">
                                        <!-- Đặt SVG mặc định là "mắt mở" -->
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>
                                    </span>
                                </div>
                            </div>

                            <!-- Confirm Password -->
                            <div class="form-group">
                                <label for="confirmPassword">Confirm Password</label>
                                <div class="input-wrapper">
                                    <input type="password" id="confirmPassword" name="confirmPassword" class="input-field" placeholder="Confirm password" required>
                                    <span class="input-icon toggle-password">
                                        <!-- SVG giống như ở trên -->
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/><circle cx="12" cy="12" r="3"/></svg>
                                    </span>
                                </div>
                            </div>

                            <div class="form-group terms-agreement">
                                <%-- 1. Bỏ thuộc tính 'checked' để checkbox không được chọn sẵn --%>
                                <input type="checkbox" id="terms" name="terms" required>
                                <label for="terms">By creating an account, I agree to our <a href="#">Terms of use</a> and <a href="#">Privacy Policy</a></label>
                            </div>

                            <div class="form-cta">
                                <%-- 2. Thêm id="createAccountBtn" vào cho nút --%>
                                <button type="submit" id="createAccountBtn" class="btn btn-primary">Create Account</button>
                            </div>                           
                        </form>
                    </div>
                </div>
            </section>

            <section class="promo-column">
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
            // Biểu tượng mắt mở
            const eyeIcon = `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z"/>
                <circle cx="12" cy="12" r="3"/>
            </svg>`;
            // Biểu tượng mắt đóng
            const eyeOffIcon = `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24"
                viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M9.88 9.88a3 3 0 1 0 4.24 4.24"/>
                <path d="M10.73 5.08A10.43 10.43 0 0 1 12 5
                        c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68"/>
                <path d="M6.61 6.61A13.526 13.526 0 0 0 2 12
                        s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61"/>
                <line x1="2" y1="2" x2="22" y2="22"/>
            </svg>`;
            // Lấy tất cả các phần tử có class toggle-password
            const togglePasswordIcons = document.querySelectorAll('.toggle-password');
            togglePasswordIcons.forEach(icon => {
            icon.addEventListener('click', function () {
            const input = this.previousElementSibling;
            if (input.type === 'password') {
            input.type = 'text';
            this.innerHTML = eyeOffIcon;
            } else {
            input.type = 'password';
            this.innerHTML = eyeIcon;
            }
            });
            });
            // --- XỬ LÝ CHECKBOX CHẤP NHẬN ĐIỀU KHOẢN ---
            const termsCheckbox = document.getElementById('terms');
            const createButton = document.getElementById('createAccountBtn');
            function updateButtonState() {
            createButton.disabled = !termsCheckbox.checked;
            }

            termsCheckbox.addEventListener('change', updateButtonState);
            updateButtonState();
            });
        </script>
    </body>
</html>