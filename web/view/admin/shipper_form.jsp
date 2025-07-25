<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Shipper</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">
    <!-- Optional: Add Bootstrap if desired -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">${mode == 'create' ? 'Thêm Shipper Mới' : 'Chỉnh sửa Shipper'}</h2>

    <form method="post" action="shippers" class="border p-4 rounded-3 bg-light shadow-sm">
        <!-- Hidden fields -->
        <input type="hidden" name="userId" value="${shipper != null ? shipper.userId : ''}">
        <input type="hidden" name="mode" value="${mode}">

        <div class="mb-3">
            <label for="userId" class="form-label">User ID:</label>
            <input type="number" class="form-control" name="user_id" id="userId"
                   value="${shipper != null ? shipper.userId : ''}" required ${mode == 'create' ? '' : 'readonly'}/>
        </div>

        <div class="mb-3">
            <label class="form-label">Họ tên:</label>
            <input type="text" name="name" class="form-control"
                   value="${shipper != null ? shipper.name : ''}" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Khu vực:</label>
            <input type="text" name="area" class="form-control"
                   value="${shipper != null ? shipper.area : ''}" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Ưu tiên (1-3):</label>
            <input type="number" name="priority_level" min="1" max="3" class="form-control"
                   value="${shipper != null ? shipper.priorityLevel : 1}" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Thu nhập/ngày:</label>
            <input type="number" name="daily_income" step="0.01" class="form-control"
                   value="${shipper != null ? shipper.dailyIncome : 0}" required readonly />
        </div>

        <button type="submit" class="btn btn-primary">
            ${mode == 'create' ? 'Thêm mới' : 'Cập nhật'}
        </button>
        <a href="${pageContext.request.contextPath}/admin/shippers" class="btn btn-secondary ms-2">Quay lại</a>
    </form>
</div>
</body>
</html>
