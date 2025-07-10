<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">

<div class="order-form-card">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <div class="order-form-title mb-0">New Product</div>
    </div>
    <form action="${pageContext.request.contextPath}/admin/products/create" method="post" id="productForm">
        <div class="mb-3">
            <label class="order-form-label">Product Name</label>
            <input type="text" name="name" class="form-control" required maxlength="50">
        </div>
        <div class="mb-3">
            <label class="order-form-label">Initial Stock</label>
            <input type="number" class="form-control" value="0" disabled>
            <div class="form-text text-danger">Stock is always 0 on creation. Only increase when adding station receipts.</div>
        </div>
        <div class="mt-4">
            <button class="btn btn-success order-form-btn" type="submit">Create Product</button>
            <a href="${pageContext.request.contextPath}/admin/products" class="order-form-btn-cancel">Cancel</a>
        </div>
    </form>
</div>