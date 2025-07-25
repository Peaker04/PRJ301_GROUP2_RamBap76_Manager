<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">

<div class="container py-4" style="max-width: 960px">
    <div class="mb-4">
        <h4 class="fw-bold">Shipper Profile</h4>
    </div>
    <div class="custom-card p-4">
        <form action="${pageContext.request.contextPath}/ProfileShipperServlet" method="post">
            <div class="row g-3">
                <div class="col-md-6">
                    <label for="first-name" class="form-label">First Name</label>
                    <input type="text" id="first-name" name="first_name" class="form-control" value="${profile.firstName}" required>
                </div>
                <div class="col-md-6">
                    <label for="last-name" class="form-label">Last Name</label>
                    <input type="text" id="last-name" name="last_name" class="form-control" value="${profile.lastName}" required>
                </div>
                <div class="col-md-12">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" id="email" name="email" class="form-control"
                           pattern="[a-zA-Z0-9._%+-]+@gmail\.com"
                           value="${profile.email}" title="Email phải kết thúc bằng @gmail.com" required>
                </div>
                <div class="col-md-6">
                    <label for="gender" class="form-label">Gender</label>
                    <select id="gender" name="gender" class="form-select" required>
                        <option value="">-- Select Gender --</option>
                        <option value="Male" ${profile.gender == 'Male' ? 'selected' : ''}>Male</option>
                        <option value="Female" ${profile.gender == 'Female' ? 'selected' : ''}>Female</option>
                    </select>
                </div>
                <div class="col-md-6">
                    <label for="phone" class="form-label">Phone Number</label>
                    <div class="input-group">
                        <span class="input-group-text">+84</span>
                        <input type="tel" id="phone" name="phone_number" class="form-control"
                               pattern="[0-9]{9}" maxlength="9"
                               value="${profile.phoneNumber != null ? profile.phoneNumber.replace('+84','') : ''}"
                               title="Nhập 9 số phía sau +84" required>
                    </div>
                </div>
                <div class="col-md-12">
                    <label for="address" class="form-label">Address</label>
                    <input type="text" id="address" name="address" class="form-control" value="${profile.address}" required>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="submit" class="btn btn-primary"><i class="bi bi-save"></i> Update</button>
                <a href="${pageContext.request.contextPath}/shipper/profile" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>

    <c:if test="${not empty message || not empty error}">
        <div class="popup-overlay" id="popupOverlay">
            <div class="popup-box ${not empty message ? 'success' : 'error'}">
                <span class="popup-close" onclick="closePopup()">&times;</span>
                <p>${not empty message ? message : error}</p>
            </div>
        </div>
    </c:if>
</div>

<script>
    document.querySelector('form').addEventListener('submit', function (e) {
        const email = document.getElementById('email').value.trim();
        const phone = document.getElementById('phone').value.trim(); // CHỈ chứa 9 số
        const address = document.getElementById('address').value.trim();

        const emailPattern = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
        const phonePattern = /^[0-9]{9}$/;

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

    window.addEventListener("DOMContentLoaded", () => {
        setTimeout(closePopup, 5000);
    });
</script>
