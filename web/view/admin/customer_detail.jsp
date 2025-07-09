<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/detail.css">

<c:set var="isEdit" value="${param.edit == '1'}"/>
<div class="container py-4" style="max-width:900px;">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold">Customer Detail: ${customer.name}</h4>
        <div>
            <a href="${pageContext.request.contextPath}/admin/customers" class="btn btn-secondary me-2">
                <i class="bi bi-arrow-left"></i> Back
            </a>
            <c:if test="${!isEdit}">
                <a href="?id=${customer.id}&edit=1" class="btn btn-warning me-2">
                    <i class="bi bi-pencil-square"></i> Edit
                </a>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/customers/detail" method="post" class="d-inline" onsubmit="return confirm('Bạn chắc chắn muốn xóa khách hàng này?');">
                <input type="hidden" name="id" value="${customer.id}">
                <input type="hidden" name="action" value="delete">
                <button class="btn btn-danger"><i class="bi bi-trash"></i> Delete</button>
            </form>
        </div>
    </div>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <form action="detail" method="post" id="customerEditForm">
        <input type="hidden" name="id" value="${customer.id}"/>
        <div class="detail-card p-4 mb-4">
            <div class="row mb-3">
                <div class="col-md-6 mb-2">
                    <label>Name</label>
                    <input type="text" name="name" class="form-control" value="${customer.name}" ${!isEdit ? 'disabled' : ''} required>
                </div>
                <div class="col-md-6 mb-2">
                    <label>Phone</label>
                    <input type="text" name="phone" class="form-control" value="${customer.phone}" ${!isEdit ? 'disabled' : ''} required>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <label>Address</label>
                    <input type="text" name="address" class="form-control" value="${customer.address}" ${!isEdit ? 'disabled' : ''}>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <label>Notes</label>
                    <textarea name="notes" class="form-control" rows="2" ${!isEdit ? 'disabled' : ''}>${customer.notes}</textarea>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <label>Latitude</label>
                    <input type="text" name="latitude" class="form-control" value="${customer.latitude}" ${!isEdit ? 'disabled' : ''}>
                </div>
                <div class="col">
                    <label>Longitude</label>
                    <input type="text" name="longitude" class="form-control" value="${customer.longitude}" ${!isEdit ? 'disabled' : ''}>
                </div>
            </div>
            <c:if test="${isEdit}">
                <div class="mt-3">
                    <input type="hidden" name="action" value="edit"/>
                    <button class="btn btn-success px-4" type="submit"><i class="bi bi-save"></i>Save Changes</button>
                    <a href="?id=${customer.id}" class="btn btn-secondary ms-2">Cancel</a>
                </div>
            </c:if>
        </div>
    </form>
    <div class="detail-card p-4">
        <h5 class="mb-3">Recently Orders</h5>
        <table class="table table-bordered align-middle mb-0">
            <thead class="table-light">
                <tr>
                    <th>#</th>
                    <th>Status</th>
                    <th>Order Date</th>
                    <th>Note</th>
                    <th>View</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${customerOrders}" varStatus="i">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.status}</td>
                        <td>
                            <c:if test="${order.orderDate != null}">
                                <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/>
                            </c:if>
                        </td>
                        <td>${order.notes}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${order.id}" class="btn btn-outline-info btn-sm"><i class="bi bi-eye"></i></a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty customerOrders}">
                    <tr>
                        <td colspan="5" class="text-center text-muted">No Orders Yet</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>
