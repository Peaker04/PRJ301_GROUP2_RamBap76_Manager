<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">

<div class="order-form-card">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <div class="order-form-title mb-0">New Order</div>
        <a href="${pageContext.request.contextPath}/admin/customers/create" class="btn btn-outline-primary customer-add-btn">
            <i class="bi bi-person-plus"></i>New Customer
        </a>
    </div>
    <form action="${pageContext.request.contextPath}/admin/orders/create" method="post" id="orderForm">
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <div class="mb-3">
            <label class="order-form-label">Customer</label>
            <select name="customer_id" class="form-select" required>
                <option value="">-- Select Customer --</option>
                <c:forEach var="cus" items="${customers}">
                    <option value="${cus.id}">${cus.name} - ${cus.phone}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Status</label>
            <select name="status" class="form-select" required>
                <c:forEach var="s" items="${['NEW','URGENT','APPOINTMENT']}">
                    <option value="${s}">${s}</option>
                </c:forEach>
            </select>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="order-form-label">Appointment</label>
                <input type="datetime-local" name="appointment_time" class="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="order-form-label">Priority</label>
                <input type="date" name="priority_delivery_date" class="form-control"/>
            </div>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Notes</label>
            <textarea name="notes" class="form-control" rows="2" style="resize:none;"></textarea>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Choose Product & Quantity</label>
            <div id="productList">
                <div class="row mb-2 product-row align-items-center">
                    <div class="col-7">
                        <select name="product_id" class="form-select" required>
                            <option value="">-- Choose Product --</option>
                            <c:forEach var="pro" items="${products}">
                                <c:if test="${product.is_deleted == 0}">
                                    <option value="${pro.id}">${pro.name}</option> 
                                </c:if>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-4">
                        <input type="number" min="1" name="quantity" class="form-control" placeholder="Số lượng" required>
                    </div>
                    <div class="col-1 d-flex align-items-center justify-content-center">
                        <button type="button" class="remove-product" style="display:none;" title="Xóa dòng sản phẩm">&times;</button>
                    </div>
                </div>
            </div>
            <button type="button" class="btn btn-outline-primary add-product-btn mt-2" id="addProductBtn">
                <i class="bi bi-plus"></i>Add Product
            </button>
        </div>
        <div class="mt-4">
            <button class="btn btn-success order-form-btn" type="submit">Create Order</button>
            <a href="${pageContext.request.contextPath}/admin/orders" class="order-form-btn-cancel">Cancel</a>
        </div>
    </form>
</div>

<script>
document.getElementById('addProductBtn').onclick = function() {
    const row = document.querySelector('.product-row').cloneNode(true);
    row.querySelector('select').selectedIndex = 0;
    row.querySelector('input[name="quantity"]').value = "";
    row.querySelector('.remove-product').style.display = 'inline-block';
    document.getElementById('productList').appendChild(row);
};
document.getElementById('productList').addEventListener('click', function(e){
    if(e.target.classList.contains('remove-product')) {
        e.target.closest('.product-row').remove();
    }
});
// Đổi tên các input cho submit mảng
document.getElementById('orderForm').onsubmit = function() {
    let rows = document.querySelectorAll('#productList .product-row');
    rows.forEach((row, idx) => {
        row.querySelector('select').name = "product_id_" + idx;
        row.querySelector('input[name="quantity"]').name = "quantity_" + idx;
    });
};
</script>
