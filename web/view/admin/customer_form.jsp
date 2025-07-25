<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">

<div class="order-form-card">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <div class="order-form-title mb-0">${customer != null ? 'Sửa khách hàng' : 'Thêm khách hàng mới'}</div>
        <a href="${pageContext.request.contextPath}/admin/customers" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Customer List
        </a>
    </div>
    <form action="${pageContext.request.contextPath}/admin/customers/${customer != null ? 'edit' : 'create'}" method="post" id="customerForm">
        <c:if test="${customer != null}">
            <input type="hidden" name="id" value="${customer.id}"/>
        </c:if>
        <div class="mb-3">
            <label class="order-form-label">Name</label>
            <input type="text" name="name" class="form-control" value="${customer.name}" required/>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Phone</label>
            <input type="tel" name="phone" class="form-control" pattern="[0-9]{10}" value="${customer.phone}" required/>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Address</label>
            <input type="text" name="address" class="form-control" value="${customer.address}" required/>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Notes</label>
            <textarea name="notes" class="form-control" rows="2" style="resize:none;">${customer.notes}</textarea>
        </div>
        <div class="mb-3">
            <div class="row">
                <div class="col">
                    <label class="order-form-label">Latitude</label>
                    <input type="text" name="latitude" class="form-control" value="${customer.latitude != null ? customer.latitude : ''}" />
                </div>
                <div class="col">
                    <label class="order-form-label">Longitude</label>
                    <input type="text" name="longitude" class="form-control" value="${customer.longitude != null ? customer.longitude : ''}" />
                </div>
            </div>
        </div>
        <div class="mt-4">
            <button class="btn btn-success order-form-btn" type="submit">${customer != null ? 'Cập nhật' : 'Tạo mới'}</button>
            <a href="${pageContext.request.contextPath}/admin/customers" class="order-form-btn-cancel">Cancel</a>
        </div>
    </form>
</div>
