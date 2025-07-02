<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

<style>
    body { background: #f5f6fa; }
    .detail-card {
        border-radius: 22px;
        box-shadow: 0 2px 16px rgba(120,120,180,0.08);
        background: #fff;
        border: none;}
    .field-label { color: #60667b; font-size: 1.06em; }
    .badge-status { font-size: 1em; padding: 6px 18px; border-radius: 12px; font-weight: 600; }
</style>

<c:set var="isEdit" value="${param.edit == '1'}"/>
<div class="container py-4" style="max-width:900px;">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold">Order Detail #${order.id}</h4>
        <div>
            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary me-2">
                <i class="bi bi-arrow-left"></i> Back
            </a>
            <c:if test="${!isEdit}">
                <a href="?id=${order.id}&edit=1" class="btn btn-warning me-2">
                    <i class="bi bi-pencil-square"></i> Edit
                </a>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/orders/detail" method="post" class="d-inline" onsubmit="return confirm('Xác nhận xóa đơn hàng này?');">
                <input type="hidden" name="id" value="${order.id}">
                <input type="hidden" name="action" value="delete">
                <button class="btn btn-danger"><i class="bi bi-trash"></i> Delete</button>
            </form>
        </div>
    </div>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="detail" method="post" id="orderEditForm">
        <input type="hidden" name="id" value="${order.id}"/>
        <div class="detail-card p-4 mb-4">
            <div class="row mb-3">
                <div class="col-md-6 mb-2">
                    <label>Customer</label>
                    <select name="customer_id" class="form-select" ${!isEdit ? 'disabled' : ''} required>
                        <c:forEach var="c" items="${customers}">
                            <option value="${c.id}" ${c.id == order.customer.id ? "selected" : ""}>
                                ${c.name} - ${c.phone}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-3 mb-2">
                    <label>Status</label>
                    <select name="status" class="form-select" ${!isEdit ? 'disabled' : ''} required>
                        <c:forEach var="s" items="${['NEW','DELIVERING','DELIVERED','URGENT','APPOINTMENT']}">
                            <option value="${s}" ${s == order.status ? "selected" : ""}>${s}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="row mb-3">
                <div class="col">
                    <label>Order Date</label>
                    <input type="text" class="form-control" value="<fmt:formatDate value='${order.orderDate}' pattern='dd/MM/yyyy'/>" disabled>
                </div>
                <div class="col">
                    <label>Appointment</label>
                    <input type="datetime-local" name="appointment_time" class="form-control"
                        value="<fmt:formatDate value='${order.appointmentTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>"
                        ${!isEdit ? 'disabled' : ''}/>
                </div>
                <div class="col">
                    <label>Priority Delivery</label>
                    <input type="date" name="priority_delivery_date" class="form-control"
                        value="<fmt:formatDate value='${order.priorityDeliveryDate}' pattern='yyyy-MM-dd'/>"
                        ${!isEdit ? 'disabled' : ''}/>
                </div>
            </div>
            <div class="mb-3">
                <label>Notes</label>
                <textarea name="notes" class="form-control" rows="2" ${!isEdit ? 'disabled' : ''}>${order.notes}</textarea>
            </div>
        </div>

        <div class="detail-card p-4">
            <h5 class="mb-3">Order Items</h5>
            <c:if test="${isEdit}">
                <div id="orderItemsEdit">
                    <c:choose>
                        <c:when test="${not empty orderDetails}">
                            <c:forEach var="d" items="${orderDetails}" varStatus="i">
                                <div class="row mb-2 align-items-center item-row">
                                    <div class="col-7">
                                        <select name="product_id_${i.index}" class="form-select" required>
                                            <c:forEach var="p" items="${products}">
                                                <option value="${p.id}" ${p.id == d.productId ? 'selected' : ''}>${p.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-4">
                                        <input type="number" min="1" name="quantity_${i.index}" class="form-control" value="${d.quantity}" required>
                                    </div>
                                    <div class="col-1 d-flex align-items-center">
                                        <button type="button" class="btn btn-danger btn-sm remove-item" ${fn:length(orderDetails) == 1 ? 'style="display:none;"' : ''}>&times;</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="row mb-2 align-items-center item-row">
                                <div class="col-7">
                                    <select name="product_id_0" class="form-select" required>
                                        <option value="">-- Chọn sản phẩm --</option>
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
                <button type="button" class="btn btn-outline-primary btn-sm mt-2" id="addOrderItemBtn">
                    <i class="bi bi-plus"></i> Thêm sản phẩm
                </button>
                <input type="hidden" id="itemCount" name="itemCount"
                       value="${not empty orderDetails ? fn:length(orderDetails) : 1}"/>
                <div class="mt-3">
                    <button class="btn btn-success px-4" type="submit"><i class="bi bi-save"></i> Save Changes</button>
                    <a href="?id=${order.id}" class="btn btn-secondary ms-2">Cancel</a>
                </div>
            </c:if>
            <c:if test="${!isEdit}">
                <table class="table table-bordered align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Product</th>
                            <th>Quantity</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="d" items="${orderDetails}" varStatus="i">
                            <tr>
                                <td>${i.index+1}</td>
                                <td>${d.productName}</td>
                                <td>${d.quantity}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </form>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    let orderItemsEdit = document.getElementById('orderItemsEdit');
    let addBtn = document.getElementById('addOrderItemBtn');
    let itemCount = document.getElementById('itemCount');
    if (addBtn && orderItemsEdit) {
        addBtn.onclick = function() {
            let rows = orderItemsEdit.getElementsByClassName('item-row');
            let count = rows.length;
            let newRow = rows[rows.length - 1].cloneNode(true);

            // Reset select & input
            let select = newRow.querySelector('select');
            select.name = 'product_id_' + count;
            select.selectedIndex = 0;

            let inputQuantity = newRow.querySelector('input[type="number"]');
            inputQuantity.name = 'quantity_' + count;
            inputQuantity.value = '';

            newRow.querySelector('.remove-item').style.display = 'inline-block';
            orderItemsEdit.appendChild(newRow);
            itemCount.value = count + 1;
        };

        orderItemsEdit.addEventListener('click', function(e) {
            if (e.target.classList.contains('remove-item')) {
                let rows = orderItemsEdit.getElementsByClassName('item-row');
                if (rows.length > 1) {
                    e.target.closest('.item-row').remove();
                    Array.from(orderItemsEdit.getElementsByClassName('item-row')).forEach(function(row, idx) {
                        row.querySelector('select').name = 'product_id_' + idx;
                        row.querySelector('input[type="number"]').name = 'quantity_' + idx;
                    });
                    itemCount.value = rows.length;
                }
            }
        });
    }
});
</script>
