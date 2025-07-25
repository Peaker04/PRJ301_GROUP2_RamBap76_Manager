<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/form.css">

<div class="order-form-card">
    <div class="d-flex align-items-center justify-content-between mb-3">
        <div class="order-form-title mb-0">Create Receipt</div>
    </div>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/admin/receipts/create" method="post" id="receiptForm">
        <div class="mb-3">
            <label class="order-form-label">Receipt Date</label>
            <input type="date" name="receipt_date" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Station Name</label>
            <input type="text" name="station_name" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="order-form-label">Transport Fee</label>
            <input type="number" min="0" step="0.01" name="transport_fee" class="form-control" value="0">
        </div>
        <div class="mb-3">
            <label class="order-form-label">Choose Products & Quantity</label>
            <div id="productList">
                <div class="row mb-2 product-row align-items-center">
                    <div class="col-7">
                        <select name="product_id" class="form-select" required>
                            <option value="">-- Choose Products --</option>
                            <c:forEach var="pro" items="${products}">
                                <option value="${pro.id}">${pro.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-4">
                        <input type="number" min="1" name="quantity" class="form-control" placeholder="Quantity" required>
                    </div>
                    <div class="col-1 d-flex align-items-center justify-content-center">
                        <button type="button" class="remove-product" style="display:none;" title="Delete Product">&times;</button>
                    </div>
                </div>
            </div>
            <button type="button" class="btn btn-outline-primary add-product-btn mt-2" id="addProductBtn">
                <i class="bi bi-plus"></i>Add Products
            </button>
        </div>
        <div class="mt-4">
            <button class="btn btn-success order-form-btn" type="submit">Create Receipt</button>
            <a href="${pageContext.request.contextPath}/admin/receipts" class="order-form-btn-cancel">Cancel</a>
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

    document.getElementById('receiptForm').onsubmit = function() {
        let rows = document.querySelectorAll('#productList .product-row');
        rows.forEach((row, idx) => {
            row.querySelector('select').name = "product_id_" + idx;
            row.querySelector('input[name="quantity"]').name = "quantity_" + idx;
        });
    };
    
    document.getElementById('receiptForm').onsubmit = function(e) {
        let valid = true;
        let rows = document.querySelectorAll('#productList .product-row');
        rows.forEach((row, idx) => {
            let qtyInput = row.querySelector('input[name="quantity"]');
            let quantity = parseInt(qtyInput.value || "0");
            row.querySelector('select').name = "product_id_" + idx;
            qtyInput.name = "quantity_" + idx;
            if (quantity > 30) {
                valid = false;
                qtyInput.classList.add("is-invalid");
            } else {
                qtyInput.classList.remove("is-invalid");
            }
        });
        if (!valid) {
            alert("Số lượng tối đa cho mỗi sản phẩm là 30!");
            e.preventDefault();
        }
    };
</script>

