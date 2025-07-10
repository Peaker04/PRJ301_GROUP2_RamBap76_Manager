<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">

<div class="container py-4" style="max-width: 1200px;">
    <div class="mb-4">
        <h4 class="fw-bold">Customers</h4>
    </div>
    <div class="custom-card p-4 mb-3">
        <!-- Search, Sort, Add -->
        <form class="d-flex mb-3 justify-content-between" method="get" id="customerFilterForm">
            <div style="flex:1; display: flex; gap: 8px;">
                <input type="text" name="search" class="form-control search-box"
                    value="${param.search}" placeholder="Search by name, phone, address...">
            </div>
            <div style="display: flex; gap: 8px;">
                <select name="sort" class="form-select" style="width: 130px;"
                        onchange="document.getElementById('customerFilterForm').submit()">
                    <option value="az" ${param.sort == null || param.sort == 'az' ? 'selected' : ''}>A - Z</option>
                    <option value="za" ${param.sort == 'za' ? 'selected' : ''}>Z - A</option>
                </select>
                <a href="${pageContext.request.contextPath}/admin/customers/create" class="btn btn-primary px-4 rounded-3 ms-1">
                    <i class="bi bi-person-plus"></i> New Customer
                </a>
            </div>
        </form>

        <!-- Action Bar -->
        <div id="multiActionBar">
            <div class="multi-action-bar">
                <span>
                    <span id="selectedCount">0</span> customer(s) selected
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
            <form id="multiDeleteForm" action="${pageContext.request.contextPath}/admin/customers" method="post">
                <input type="hidden" name="ids" id="deleteIds"/>
                <input type="hidden" name="action" value="multiDelete"/>
            </form>
        </div>

        <!-- Table -->
        <div class="table-responsive table-rounded">
            <table class="table custom-table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th><input type="checkbox" id="checkAll"></th>
                        <th>Name</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Notes</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="cus" items="${customers}">
                        <tr>
                            <td>
                                <input type="checkbox" class="customer-check" value="${cus.id}">
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/customers/detail?id=${cus.id}" class="fw-semibold text-primary text-decoration-none">${cus.name}</a>
                            </td>
                            <td>${cus.phone}</td>
                            <td>${cus.address}</td>
                            <td>${cus.notes}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/customers/detail?id=${cus.id}" class="icon-action text-primary" title="View"><i class="bi bi-eye"></i></a>
                                <a href="${pageContext.request.contextPath}/admin/customers/detail?id=${cus.id}&edit=1" class="icon-action text-warning" title="Edit"><i class="bi bi-pencil-square"></i></a>
                                <form action="${pageContext.request.contextPath}/admin/customers/detail" method="post" style="display:inline;" onsubmit="return confirm('Xóa khách hàng này?');">
                                    <input type="hidden" name="id" value="${cus.id}"/>
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
        </div>
    
        <div class="d-flex justify-content-between align-items-center mt-3 px-2">
                <span class="text-muted small">
                    ${(page-1)*size+1} - ${(page-1)*size+customers.size()} of ${totalCustomers} Customers
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

<script>
    function changePage(p) {
        document.querySelector('select[name="page"]').value = p;
        document.getElementById('pagingForm').submit();
    }
    document.addEventListener("DOMContentLoaded", function() {
        const checkAll = document.getElementById('checkAll');
        const customerChecks = document.querySelectorAll('.customer-check');
        const multiActionBar = document.getElementById('multiActionBar');
        const selectedCount = document.getElementById('selectedCount');
        const unselectBtn = document.getElementById('multiUnselectBtn');
        const deleteBtn = document.getElementById('multiDeleteBtn');
        const deleteForm = document.getElementById('multiDeleteForm');
        const deleteIds = document.getElementById('deleteIds');

        function updateMultiActionBar() {
            let checked = document.querySelectorAll('.customer-check:checked');
            selectedCount.textContent = checked.length;
            multiActionBar.style.display = checked.length > 0 ? 'flex' : 'none';
            checkAll.checked = checked.length === customerChecks.length && customerChecks.length > 0;
        }

        checkAll.addEventListener('change', function() {
            customerChecks.forEach(c => c.checked = checkAll.checked);
            updateMultiActionBar();
        });
        customerChecks.forEach(c => c.addEventListener('change', updateMultiActionBar));

        unselectBtn.addEventListener('click', function() {
            customerChecks.forEach(c => c.checked = false);
            checkAll.checked = false;
            updateMultiActionBar();
        });

        deleteBtn.addEventListener('click', function() {
            let ids = Array.from(document.querySelectorAll('.customer-check:checked')).map(cb => cb.value);
            if (ids.length === 0) return;
            if (!confirm('Bạn chắc chắn muốn xóa các khách hàng đã chọn?')) return;
            deleteIds.value = ids.join(',');
            deleteForm.submit();
        });

        updateMultiActionBar();
    });
</script>
