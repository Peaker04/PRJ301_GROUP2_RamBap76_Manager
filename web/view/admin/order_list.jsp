<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">


<div class="container py-4" style="max-width: 1200px;">
    <div class="mb-4">
        <h4 class="fw-bold">Orders</h4>
    </div>
    <div class="custom-card p-4 mb-3">
        <!-- Search, Filter & New Order Row -->
        <div class="row align-items-center mb-2 g-2">
            <div class="col-12 col-md-12">
                <form class="d-flex mb-3 justify-content-between" method="get" id="orderFilterForm">
                    <div style="flex:1; display: flex; gap: 8px;">
                        <input type="hidden" name="status" value="${status}" />
                        <input type="text" name="search" class="form-control search-box" value="${param.search}" placeholder="Search by id, customer, notes...">
                    </div>
                    <div style="display: flex; gap: 8px;">
                        <select name="sort" class="form-select" style="width: 130px;" onchange="document.getElementById('orderFilterForm').submit()">
                            <option value="desc" ${param.sort == null || param.sort == 'desc' ? 'selected' : ''}>Newest</option>
                            <option value="asc"  ${param.sort == 'asc' ? 'selected' : ''}>Oldest</option>
                        </select>
                        <a href="${pageContext.request.contextPath}/admin/orders/create" class="btn btn-primary px-4 rounded-3 ms-1">
                            <i class="bi bi-plus-circle"></i> New Order
                        </a>
                    </div>
                </form>
            </div>
        </div>
        <!-- Filter tabs -->
        <ul class="nav filter-tabs mb-3 flex-wrap">
            <li class="nav-item">
                <a class="nav-link ${status == null ? 'active' : ''}" href="orders?sort=${param.sort}&search=${param.search}">
                    All Orders <span class="badge bg-light text-dark ms-1">${statusCounts.ALL}</span>
                </a>
            </li>
            <c:forEach var="s" items="${['NEW','DELIVERING','DELIVERED','URGENT','APPOINTMENT']}">
                <li class="nav-item">
                    <a class="nav-link ${status == s ? 'active' : ''}" href="orders?status=${s}&sort=${param.sort}&search=${param.search}">
                        ${s} <span class="badge bg-light text-dark ms-1">${statusCounts[s]}</span>
                    </a>
                </li>
            </c:forEach>
        </ul>

        <div class="table-responsive table-rounded">
            <%--Selected order--%> 
            <div id="multiActionBar">
                <div class="multi-action-bar">
                    <span>
                        <span id="selectedCount">0</span> order(s) selected
                    </span>
                    <div class="action-btns">
                        <button type="button" class="btn btn-danger btn-sm" id="multiDeleteBtn">
                            <i class="bi bi-trash"></i> Delete Selected
                        </button>
                        <button type="button" class="btn btn-outline-secondary btn-sm" id="multiUnselectBtn">
                            Unselect All
                        </button>
                    </div>
                </div>
                <form id="multiDeleteForm" action="${pageContext.request.contextPath}/admin/orders" method="post">
                    <input type="hidden" name="ids" id="deleteIds"/>
                    <input type="hidden" name="action" value="multiDelete"/>
                </form>
            </div>

            <%--Table list order--%> 
            <table class="table custom-table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th><input type="checkbox" id="checkAll"></th>
                        <th>ID</th>
                        <th>Customer</th>
                        <th>Status</th>
                        <th>Order Date</th>
                        <th>Appointment</th>
                        <th>Priority</th>
                        <th>Notes</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td><input type="checkbox" class="order-check" value="${order.id}" ></td>
                            <td>
                                <a href="orders/detail?id=${order.id}" class="text-decoration-none fw-semibold text-primary">
                                    #${order.id}
                                </a>
                            </td>
                            <td>${order.customer.name}</td>
                            <td>
                                <span class="badge 
                                    ${order.status == 'NEW' ? 'bg-primary' : 
                                      order.status == 'DELIVERING' ? 'bg-warning text-dark' :
                                      order.status == 'DELIVERED' ? 'bg-success' :
                                      order.status == 'URGENT' ? 'bg-danger' : 'bg-info'}">
                                    ${order.status}
                                </span>
                            </td>
                            <td>
                                <c:if test="${order.orderDate != null}">
                                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${order.appointmentTime != null}">
                                    <fmt:formatDate value="${order.appointmentTime}" pattern="dd/MM/yyyy"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${order.priorityDeliveryDate != null}">
                                    <fmt:formatDate value="${order.priorityDeliveryDate}" pattern="dd/MM/yyyy"/>
                                </c:if>
                            </td>
                            <td>${order.notes}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${order.id}" class="icon-action text-primary"><i class="bi bi-eye" title="View"></i></a>
                                <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${order.id}&edit=1" class="icon-action text-warning"><i class="bi bi-pencil-square" title="Edit"></i></a>
                                <form action="${pageContext.request.contextPath}/admin/orders/detail" method="post" style="display:inline;" onsubmit="return confirm('Bạn chắc chắn muốn xóa đơn này?');">
                                    <input type="hidden" name="id" value="${order.id}"/>
                                    <input type="hidden" name="action" value="delete"/>
                                    <button type="submit" class="icon-action text-danger btn btn-link p-0 m-0" title="Delete">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>                          
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <div class="d-flex justify-content-between align-items-center mt-3 px-2">
                <span class="text-muted small">
                    ${(page-1)*size+1} - ${(page-1)*size+orders.size()} of ${totalOrders} Orders
                </span>
                <form method="get" id="pagingForm" style="display:inline;">
                    <input type="hidden" name="search" value="${search}" />
                    <input type="hidden" name="sort" value="${sort}" />
                    <input type="hidden" name="status" value="${status}" />
                    <button type="button" class="btn btn-link p-0 m-0" onclick="changePage(${page-1})" <c:if test="${page==1}">disabled</c:if>>
                        <i class="bi bi-chevron-left"></i>
                    </button>
                    <select name="page" class="form-select d-inline-block" style="width:60px;display:inline;" onchange="this.form.submit()">
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <option value="${i}" <c:if test="${i==page}">selected</c:if>>${i}</option>
                        </c:forEach>
                    </select>
                    <button type="button" class="btn btn-link p-0 m-0" onclick="changePage(${page+1})" <c:if test="${page==totalPages}">disabled</c:if>>
                        <i class="bi bi-chevron-right"></i>
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
                            
<script>
    function changePage(p) {
        if (p < 1) p = 1;
        if (p > ${totalPages}) p = ${totalPages};
        document.querySelector('select[name="page"]').value = p;
        document.getElementById('pagingForm').submit();
    }
    
    document.addEventListener("DOMContentLoaded", function() {
        const checkAll = document.getElementById('checkAll');
        const orderChecks = document.querySelectorAll('.order-check');
        const multiActionBar = document.getElementById('multiActionBar');
        const selectedCount = document.getElementById('selectedCount');
        const unselectBtn = document.getElementById('multiUnselectBtn');
        const deleteBtn = document.getElementById('multiDeleteBtn');
        const deleteForm = document.getElementById('multiDeleteForm');
        const deleteIds = document.getElementById('deleteIds');

        function updateMultiActionBar() {
            let checked = document.querySelectorAll('.order-check:checked');
            selectedCount.textContent = checked.length;
            multiActionBar.style.display = checked.length > 0 ? 'flex' : 'none';
            checkAll.checked = checked.length === orderChecks.length && orderChecks.length > 0;
        }

        // Sự kiện chọn/bỏ tất cả
        checkAll.addEventListener('change', function() {
            orderChecks.forEach(c => c.checked = checkAll.checked);
            updateMultiActionBar();
        });
        // Sự kiện từng checkbox
        orderChecks.forEach(c => c.addEventListener('change', updateMultiActionBar));

        // Unselect All
        unselectBtn.addEventListener('click', function() {
            orderChecks.forEach(c => c.checked = false);
            checkAll.checked = false;
            updateMultiActionBar();
        });

        // Delete Selected
        deleteBtn.addEventListener('click', function() {
            let ids = Array.from(document.querySelectorAll('.order-check:checked')).map(cb => cb.value);
            if (ids.length === 0) return;
            if (!confirm('Bạn chắc chắn muốn xóa các đơn đã chọn?')) return;
            deleteIds.value = ids.join(',');
            deleteForm.submit();
        });

        // Khi load trang lần đầu, luôn đảm bảo ẩn action bar nếu không chọn gì
        updateMultiActionBar();
    });
</script>