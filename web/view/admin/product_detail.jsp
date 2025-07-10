<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/detail.css">

<c:set var="isEdit" value="${param.edit == '1'}"/>
<div class="container py-4" style="max-width:900px;">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold">Product Detail: ${product.name}</h4>
        <div>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary me-2">
                <i class="bi bi-arrow-left"></i> Back
            </a>
            <c:if test="${!isEdit}">
                <a href="?id=${product.id}&edit=1" class="btn btn-warning me-2">
                    <i class="bi bi-pencil-square"></i> Edit
                </a>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/products/detail" method="post" class="d-inline" onsubmit="return confirm('Xác nhận xóa sản phẩm này?');">
                <input type="hidden" name="id" value="${product.id}">
                <input type="hidden" name="action" value="delete">
                <button class="btn btn-danger"><i class="bi bi-trash"></i> Delete</button>
            </form>
        </div>
    </div>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <form action="detail" method="post" id="productEditForm">
        <input type="hidden" name="id" value="${product.id}"/>
        <div class="detail-card p-4 mb-4">
            <div class="row mb-3">
                <div class="col-md-6 mb-2">
                    <label>Product ID</label>
                    <input type="text" class="form-control" value="${product.id}" disabled>
                </div>
                <div class="col-md-6 mb-2">
                    <label>Product Name</label>
                    <c:choose>
                        <c:when test="${isEdit}">
                            <input type="text" name="name" class="form-control" value="${product.name}" required maxlength="50">
                        </c:when>
                        <c:otherwise>
                            <input type="text" class="form-control" value="${product.name}" disabled>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="mb-2">
                <label>Total Stock</label>
                <input type="text" class="form-control" value="${product.stock}" disabled>
            </div>
            <c:if test="${isEdit}">
                <div class="mt-3">
                    <button class="btn btn-success px-4" type="submit"><i class="bi bi-save"></i> Save Changes</button>
                    <a href="?id=${product.id}" class="btn btn-secondary ms-2">Cancel</a>
                </div>
            </c:if>
        </div>
        <c:if test="${!isEdit}">
            <!-- History of warehouse receipts for this product -->
            <div class="detail-card p-4">
                <h5 class="mb-3">Station Receipt History</h5>
                <table class="table table-bordered align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>Batch ID</th>
                            <th>Quantity</th>
                            <th>Station</th>
                            <th>Receipt Date</th>
                            <th>Expiration Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="batch" items="${importHistory}">
                            <tr>
                                <td>${batch.receiptId}</td>
                                <td>${batch.quantity}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty batch.stationReceipt.stationName}">
                                            ${batch.stationReceipt.stationName}
                                        </c:when>
                                        <c:otherwise>None</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty batch.stationReceipt && not empty batch.stationReceipt.receiptDate}">
                                            <fmt:formatDate value="${batch.stationReceipt.receiptDate}" pattern="dd/MM/yyyy"/>
                                        </c:when>
                                        <c:otherwise>None</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty batch.stationReceipt && not empty batch.stationReceipt.expirationDate}">
                                            <fmt:formatDate value="${batch.stationReceipt.expirationDate}" pattern="dd/MM/yyyy"/>                                        </c:when>
                                        <c:otherwise>None</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty receipts}">
                            <tr>
                                <td colspan="5" class="text-center text-muted">No station receipt history for this product.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </c:if>
    </form>
</div>