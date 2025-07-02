<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

<style>
    body {
        background: #f5f6fa;
    }
    .order-form-card {
        border-radius: 22px;
        background: #fff;
        box-shadow: 0 4px 24px 0 rgba(80,80,150,0.10);
        padding: 24px 36px 24px 36px;
        margin: 36px auto;
        max-width: 940px;
        min-width: 340px;
    }
    .order-form-title {
        font-weight: bold;
        font-size: 2.1rem;
        color: #21243a;
        margin-bottom: 18px;
        letter-spacing: -1px;
    }
    .order-form-label {
        color: #4b5563;
        font-weight: 500;
        font-size: 1rem;
        margin-bottom: 6px;
    }
    .form-control, .form-select {
        border-radius: 10px !important;
        background: #f8fafc !important;
        border: 1px solid #e2e8f0 !important;
        color: #20223b;
        font-size: 1rem;
        padding: 9px 15px;
    }
    .form-control:focus, .form-select:focus {
        border-color: #6495ed;
        box-shadow: 0 0 0 2px #bcd6fa50;
    }
    .order-form-btn {
        border-radius: 12px;
        font-weight: 500;
        padding: 9px 30px;
        font-size: 1.08rem;
    }
    .order-form-btn-cancel {
        background: #e5e7eb;
        color: #222;
        border-radius: 12px;
        font-weight: 500;
        padding: 9px 22px;
        margin-left: 14px;
        border: none;
    }
    .add-product-btn {
        font-size: 0.99rem;
        border-radius: 8px;
        padding: 5px 17px;
        font-weight: 500;
    }
    .remove-product {
        font-size: 1.3em;
        color: #ef4444;
        background: transparent;
        border: none;
        margin-left: 5px;
        padding: 0;
        margin-top: 4px;
    }
    .customer-add-btn {
        white-space: nowrap;
        font-size: 0.99rem;
        border-radius: 9px;
        font-weight: 500;
        padding: 7px 14px;
        margin-left: 10px;
        transition: background 0.2s;
    }
    .customer-add-btn i { margin-right: 3px; }
    @media (max-width: 575px) {
        .order-form-card { padding: 16px 6px; }
    }
</style>

<div class="order-form-card">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <div class="order-form-title mb-0">Tạo Đơn Hàng Mới</div>
        <a href="${pageContext.request.contextPath}/admin/customers/create" class="btn btn-outline-primary customer-add-btn">
            <i class="bi bi-person-plus"></i>Thêm khách mới
        </a>
    </div>
    <form action="${pageContext.request.contextPath}/admin/orders/create" method="post" id="orderForm">
        <div class="mb-3">
            <label class="order-form-label">Khách hàng</label>
            <select name="customer_id" class="form-select" required>
                <option value="">-- Chọn khách hàng --</option>
                <c:forEach var="cus" items="${customers}">
                    <option value="${cus.id}">${cus.name} - ${cus.phone}</option>
                </c:forEach>
            </select>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Trạng thái</label>
            <select name="status" class="form-select" required>
                <c:forEach var="s" items="${['NEW','DELIVERING','DELIVERED','URGENT','APPOINTMENT']}">
                    <option value="${s}">${s}</option>
                </c:forEach>
            </select>
        </div>
        <div class="row mb-3">
            <div class="col-md-6">
                <label class="order-form-label">Ngày hẹn giao</label>
                <input type="datetime-local" name="appointment_time" class="form-control"/>
            </div>
            <div class="col-md-6">
                <label class="order-form-label">Ngày giao ưu tiên</label>
                <input type="date" name="priority_delivery_date" class="form-control"/>
            </div>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Ghi chú</label>
            <textarea name="notes" class="form-control" rows="2" style="resize:none;"></textarea>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Chọn sản phẩm & số lượng</label>
            <div id="productList">
                <div class="row mb-2 product-row align-items-center">
                    <div class="col-7">
                        <select name="product_id" class="form-select" required>
                            <option value="">-- Chọn sản phẩm --</option>
                            <c:forEach var="pro" items="${products}">
                                <option value="${pro.id}">${pro.name}</option>
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
                <i class="bi bi-plus"></i>Thêm sản phẩm
            </button>
        </div>
        <div class="mt-4">
            <button class="btn btn-success order-form-btn" type="submit">Tạo đơn hàng</button>
            <a href="${pageContext.request.contextPath}/admin/orders" class="order-form-btn-cancel">Hủy</a>
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
