<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">

   <div class="page-content">
        <div class="profile-content">
            <form action="${pageContext.request.contextPath}/ProfileShipperServlet" method="post">

                <section class="card">
                    <h2 class="card-title">Profile Information</h2>



                    <div class="form-row">
                        <div class="form-group">
                            <label for="first-name">First Name</label>
                            <input type="text" id="first-name" name="first_name" class="form-control form-control-sm"
                                   value="${profile.firstName}" required>
                        </div>
                        <div class="form-group">
                            <label for="last-name">Last Name</label>
                            <input type="text" id="last-name" name="last_name" class="form-control form-control-sm"
                                   value="${profile.lastName}" required>
                        </div>
                        <div class="form-group form-group-full">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-control form-control-sm"
                                   pattern="[a-zA-Z0-9._%+-]+@gmail\.com" value="${profile.email}"
                                   title="Email phải kết thúc bằng @gmail.com" required>
                        </div>
                        <div class="form-group">
                            <label for="gender">Gender</label>
                            <select id="gender" name="gender" class="form-control form-control-sm" required>
                                <option value="">-- Select Gender --</option>
                                <option value="Male" ${profile.gender == 'Male' ? 'selected' : ''}>Male</option>
                                <option value="Female" ${profile.gender == 'Female' ? 'selected' : ''}>Female</option>
                            </select>
                        </div>
                    </div>
                </section>

                <section class="card">
                    <div class="card-header">
                        <h2 class="card-title">Contact Detail</h2>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="phone">Phone Number</label>
                            <div class="phone-wrapper">
                                <span class="prefix">+84</span>
                                <input type="tel" id="phone" name="phone_number"
                                       class="form-control form-control-sm phone-input"
                                       pattern="[0-9]{9}"
                                       maxlength="9"
                                       value="${profile.phoneNumber != null ? profile.phoneNumber.replace('+84','') : ''}"
                                       title="Nhập 9 số phía sau +84" required>
                            </div>
                        </div>
                        <div class="form-group form-group-full">
                            <label for="address">Address</label>
                            <input type="text" id="address" name="address" class="form-control form-control-sm"
                                   value="${profile.address}" required>
                        </div>
                    </div>
                </section>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Update</button>
                    <a href="${pageContext.request.contextPath}/shipper/profile" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
    <c:if test="${not empty message || not empty error}">
        <div class="popup-overlay" id="popupOverlay">
            <div class="popup-box ${not empty message ? 'success' : 'error'}">
                <span class="popup-close" onclick="closePopup()">×</span>
                <p>${not empty message ? message : error}</p>
            </div>
        </div>
    </c:if>
    <script>
        document.querySelector('form').addEventListener('submit', function (e) {
            const email = document.getElementById('email').value.trim();
            const phone = document.getElementById('phone').value.trim(); // CHỈ chứa 9 số
            const address = document.getElementById('address').value.trim();

            const emailPattern = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
            const phonePattern = /^[0-9]{9}$/; // chỉ 9 chữ số, không có +84

            let errors = [];

            if (!emailPattern.test(email)) {
                errors.push("Email phải đúng định dạng và kết thúc bằng @gmail.com");
            }

            if (!phonePattern.test(phone)) {
                errors.push("Số điện thoại phải bao gồm đúng 9 chữ số.");
            }

            if (address === "") {
                errors.push("Địa chỉ không được để trống.");
            }

            if (errors.length > 0) {
                e.preventDefault();
                alert(errors.join("\n"));
            }
        });

         function closePopup() {
        const popup = document.getElementById("popupOverlay");
        if (popup) popup.style.display = "none";
    }

    // Auto close after 5s
    window.addEventListener("DOMContentLoaded", () => {
        setTimeout(closePopup, 5000);
    });
    </script>
