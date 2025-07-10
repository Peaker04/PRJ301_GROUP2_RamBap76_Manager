<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/detail.css">

<c:set var="isEdit" value="${param.edit == '1'}"/>
<div class="container py-4" style="max-width:900px;">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold">Receipt #${receipt.id}</h4>
        <div>
            <a href="${pageContext.request.contextPath}/admin/receipts" class="btn btn-secondary me-2">
                <i class="bi bi-arrow-left"></i> Back
            </a>
            <c:if test="${!isEdit}">
                <a href="?id=${receipt.id}&edit=1" class="btn btn-warning me-2">
                    <i class="bi bi-pencil-square"></i> Edit
                </a>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/receipts/detail" method="post" class="d-inline" onsubmit="return confirm('Xác nhận xóa phiếu nhập này?');">
                <input type="hidden" name="id" value="${receipt.id}">
                <input type="hidden" name="action" value="delete">
                <button class="btn btn-danger"><i class="bi bi-trash"></i> Delete</button>
            </form>
        </div>
    </div>
    <form action="detail" method="post" id="receiptEditForm">
        <input type="hidden" name="id" value="${receipt.id}"/>
        <div class="detail-card p-4 mb-4">
            <div class="row mb-3">
                <div class="col-md-4 mb-2">
                    <label>Receipt Date</label>
                    <input type="text" class="form-control" value="<fmt:formatDate value='${receipt.receiptDate}' pattern='dd/MM/yyyy'/>" disabled>
                </div>
                <div class="col-md-4 mb-2">
                    <label>Station Name</label>
                    <input type="text" name="station_name" class="form-control" value="${receipt.stationName}" ${!isEdit ? 'disabled' : ''} required>
                </div>
                <div class="col-md-4 mb-2">
                    <label>Transport Fee</label>
                    <input type="number" min="0" step="0.01" name="transport_fee" class="form-control" value="${receipt.transportFee}" ${!isEdit ? 'disabled' : ''}>
                </div>
            </div>
        </div>
        <div class="detail-card p-4">
            <h5 class="mb-3">Product</h5>
            <c:if test="${isEdit}">
                <div id="receiptItemsEdit">
                    <c:choose>
                        <c:when test="${not empty details}">
                            <c:forEach var="d" items="${details}" varStatus="i">
                                <div class="row mb-2 align-items-center item-row">
                                    <div class="col-7">
                                        <select name="product_id_${i.index}" class="form-select" required>
                                            <c:forEach var="p" items="${products}">
                                                <option value="${p.id}" ${p.id == d.productId ? "selected" : ""}>${p.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-4">
                                        <input type="number" min="1" name="quantity_${i.index}" class="form-control" value="${d.quantity}" required>
                                    </div>
                                    <div class="col-1 d-flex align-items-center">
                                        <button type="button" class="btn btn-danger btn-sm remove-item" 
                                          ${fn:length(details) == 1 ? 'style="display:none;"' : ''}>&times;</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>

                            <div class="row mb-2 align-items-center item-row">
                                <div class="col-7">
                                    <select name="product_id_0" class="form-select" required>
                                        <option value="">-- Choose Product --</option>
                                        <c:forEach var="p" items="${products}">
                                            <option value="${p.id}">${p.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-4">
                                    <input type="number" min="1" name="quantity_0" class="form-control" required>
                                </div>
                                <div class="col-1 d-flex align-items-center">
                                    <button type="button" class="btn btn-danger btn-sm remove-item" style="display:none;">&times;</button>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <button type="button" class="btn btn-outline-primary btn-sm mt-2" id="addReceiptItemBtn">
                    <i class="bi bi-plus"></i> Add Product
                </button>
                <input type="hidden" id="itemCount" name="itemCount"
                    value="${not empty details ? fn:length(details) : 1}"/>
                <div class="mt-3">
                    <button class="btn btn-success px-4" type="submit"><i class="bi bi-save"></i> Save Changes</button>
                    <a href="?id=${receipt.id}" class="btn btn-secondary ms-2">Cancel</a>
                </div>
            </c:if>
            <c:if test="${!isEdit}">
                <table class="table table-bordered align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Remaining Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${details}" varStatus="i">
                            <tr>
                                <td>${i.index+1}</td>
                                <td>${d.productName}</td>
                                <td>${d.quantity}</td>
                                <td>${d.remainingQuantity}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </form>
</div>

