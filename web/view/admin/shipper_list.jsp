<%-- 
    Document   : shipper_list
    Created on : Jun 26, 2025, 11:52:05 PM
    Author     : Minh Tuan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh sách Shipper</title>
    <style>
        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
        }

        th, td {
            padding: 10px;
            border: 1px solid #ccc;
            text-align: center;
        }

        a.button {
            padding: 6px 12px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        a.button.delete {
            background-color: #f44336;
        }

        .top-actions {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>

    <h2 style="text-align: center;">DANH SÁCH SHIPPER</h2>

    <div class="top-actions">
        <a href="shippers?action=create" class="button">➕ Thêm shipper</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Họ tên</th>
                <th>Khu vực</th>
                <th>Ưu tiên</th>
                <th>Thu nhập/ngày</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="shipper" items="${shippers}">
                <tr>
                    <td>${shipper.id}</td>
                    <td>${shipper.name}</td>
                    <td>${shipper.area}</td>
                    <td>${shipper.priorityLevel}</td>
                    <td>${shipper.dailyIncome}</td>
                    <td>
                        <a href="shippers?action=edit&id=${shipper.id}" class="button">✏️ Sửa</a>
                        <a href="shippers?action=delete&id=${shipper.id}" class="button delete" onclick="return confirm('Bạn có chắc chắn muốn xoá shipper này?');">🗑️ Xoá</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
