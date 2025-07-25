<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">

<div class="container py-4" style="max-width: 1200px;">
    <div class="mb-4">
        <h4 class="fw-bold">Danh sách Shipper</h4>
    </div>

    <div class="custom-card p-4 mb-3">
        <div class="d-flex justify-content-between mb-3">
            <div></div>
            <a href="shippers?action=create" class="btn btn-primary px-4 rounded-3">
                <i class="bi bi-plus-circle"></i> Thêm shipper
            </a>
        </div>

        <div class="table-responsive table-rounded">
            <table class="table custom-table align-middle mb-0">
                <thead class="table-light">
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
                            <td>${shipper.userId}</td>
                            <td>${shipper.name}</td>
                            <td>${shipper.area}</td>
                            <td>${shipper.priorityLevel}</td>
                            <td>${shipper.dailyIncome}</td>
                            <td>
                                <a href="shippers?action=edit&id=${shipper.userId}" class="icon-action text-warning" title="Chỉnh sửa">
                                    <i class="bi bi-pencil-square"></i>
                                </a>
                                <a href="shippers?action=delete&id=${shipper.userId}" class="icon-action text-danger" onclick="return confirm('Bạn có chắc chắn muốn xoá shipper này?');" title="Xoá">
                                    <i class="bi bi-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
