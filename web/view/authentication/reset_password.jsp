<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Your Password</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/resetpassword.css">
    </head>
    <body>

        <div class="main-container">
            <header class="header">
                <img src="https://i.imgur.com/your-logo-image-code.png" alt="SocialRepeat Logo" class="logo">
            </header>

            <div class="form-container">
                <h1 class="title">Reset your password</h1>
                <p class="subtitle">Type in your new password</p>

                <%
                    String successMessage = (String) request.getAttribute("successMessage");
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (successMessage != null) {
                %>
                <div class="message success"><%= successMessage%></div>
                <%
                    }
                    if (errorMessage != null) {
                %>
                <div class="message error"><%= errorMessage%></div>
                <%
                    }
                %>
                <form action="${pageContext.request.contextPath}/reset-password" method="post" onsubmit="return validatePassword()">
                    <input type="hidden" name="token" value="<%= request.getParameter("token")%>">

                    <div class="input-group">
                        <input type="password" id="newPassword" name="newPassword" class="input-field" placeholder="New password *" required>
                    </div>

                    <div class="input-group">
                        <input type="password" id="confirmPassword" name="confirmPassword" class="input-field" placeholder="Retry new password *" required>
                    </div>

                    <button type="submit" class="button btn-reset">RESET</button>
                </form>

                <a href="login.jsp" class="button btn-back">BACK TO LOGIN</a>

            </div>

            <footer class="footer">
                <a href="#">Terms and conditions</a>
                <span class="separator">•</span>
                <a href="#">Privacy policy</a>
            </footer>
        </div>

        <script>
            function validatePassword() {
                var newPassword = document.getElementById("newPassword").value;
                var confirmPassword = document.getElementById("confirmPassword").value;
                if (newPassword !== confirmPassword) {
                    // Bạn có thể hiển thị một thông báo lỗi tinh tế hơn ở đây
                    alert("Passwords do not match!");
                    return false; // Ngăn form được gửi đi
                }
                return true; // Cho phép gửi form
            }
        </script>

    </body>
</html>