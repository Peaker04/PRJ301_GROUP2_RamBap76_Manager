<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Form Quản lý Shipper</title>
</head>
<body>
    <h2>${mode == 'create' ? 'Thêm Shipper Mới' : 'Chỉnh sửa Shipper'}</h2>

    <form method="post" action="shippers">
        <input type="hidden" name="id" value="${shipper != null ? shipper.id : ''}">
        <input type="hidden" name="mode" value="${mode}">

        <label>Họ tên :</label><br>
        <input type="text" name="name" value="${shipper != null ? shipper.name : ''}" required><br><br>

        <label>Khu vực:</label><br>
        <input type="text" name="area" value="${shipper != null ? shipper.area : ''}" required><br><br>

        <label>Ưu tiên (1-3):</label><br>
        <input type="number" name="priority_level" min="1" max="3" value="${shipper != null ? shipper.priorityLevel : 1}" required><br><br>

        <label>Thu nhập/ngày:</label><br>
        <input type="number" name="daily_income" step="0.01" value="${shipper != null ? shipper.dailyIncome : 0}" required><br><br>

        <button type="submit">${mode == 'create' ? 'Thêm mới' : 'Cập nhật'}</button>
    </form>
</body>
</html>
