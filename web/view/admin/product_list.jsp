<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/list.css">

<div class="container py-4" style="max-width: 1200px;">
    <div class="mb-4">
        <h4 class="fw-bold">Products</h4>
    </div>
    <div class="custom-card p-4 mb-3">
        <!-- Search, Sort, Add -->
        <form class="d-flex mb-3 justify-content-between" method="get" id="productFilterForm">
            <div style="flex:1; display: flex; gap: 8px;">
                <input type="text" name="search" class="form-control search-box"
                    value="${param.search}" placeholder="Search by name...">
            </div>
            <div style="display: flex; gap: 8px;">
                <select name="sort" class="form-select" style="width: 130px;"
                        onchange="document.getElementById('productFilterForm').submit()">
                    <option value="az" ${param.sort == null || param.sort == 'az' ? 'selected' : ''}>A - Z</option>
                    <option value="za" ${param.sort == 'za' ? 'selected' : ''}>Z - A</option>
                </select>
                <a href="${pageContext.request.contextPath}/admin/products/create" class="btn btn-primary px-4 rounded-3 ms-1">
                    <i class="bi bi-plus-circle"></i> New Product
                </a>
            </div>
        </form>

        <!-- Action Bar -->
        <div id="multiActionBar" style="display:none;">
            <div class="multi-action-bar">
                <span>
                    <span id="selectedCount">0</span> product(s) selected
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
            <form id="multiDeleteForm" action="${pageContext.request.contextPath}/admin/products" method="post">
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
                        <th>ID</th>
                        <th>Name</th>
                        <th>Stock</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pro" items="${products}">
                            <tr>
                                <td>
                                    <input type="checkbox" class="product-check" value="${pro.id}">
                                </td>
                                <td>${pro.id}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/products/detail?id=${pro.id}" class="fw-semibold text-primary text-decoration-none">
                                        ${pro.name}
                                    </a>
                                </td>
                                <td>${pro.stock}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/products/detail?id=${pro.id}" class="icon-action text-primary" title="View"><i class="bi bi-eye"></i></a>
                                    <form action="${pageContext.request.contextPath}/admin/products/detail" method="post" style="display:inline;" onsubmit="return confirm('Xóa sản phẩm này?');">
                                        <input type="hidden" name="id" value="${pro.id}"/>
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
        <!-- Paging -->
        <div class="d-flex justify-content-between align-items-center mt-3 px-2">
            <span class="text-muted small">
                ${(page-1)*size+1} - ${(page-1)*size+products.size()} of ${totalProducts} Products
            </span>
            <form method="get" id="pagingForm" style="display:inline;">
                <input type="hidden" name="search" value="${search}" />
                <input type="hidden" name="sort" value="${sort}" />
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
        const productChecks = document.querySelectorAll('.product-check');
        const multiActionBar = document.getElementById('multiActionBar');
        const selectedCount = document.getElementById('selectedCount');
        const unselectBtn = document.getElementById('multiUnselectBtn');
        const deleteBtn = document.getElementById('multiDeleteBtn');
        const deleteForm = document.getElementById('multiDeleteForm');
        const deleteIds = document.getElementById('deleteIds');

        function updateMultiActionBar() {
            let checked = document.querySelectorAll('.product-check:checked');
            selectedCount.textContent = checked.length;
            multiActionBar.style.display = checked.length > 0 ? 'flex' : 'none';
            checkAll.checked = checked.length === productChecks.length && productChecks.length > 0;
        }

        checkAll.addEventListener('change', function() {
            productChecks.forEach(c => c.checked = checkAll.checked);
            updateMultiActionBar();
        });
        productChecks.forEach(c => c.addEventListener('change', updateMultiActionBar));

        unselectBtn.addEventListener('click', function() {
            productChecks.forEach(c => c.checked = false);
            checkAll.checked = false;
            updateMultiActionBar();
        });

        deleteBtn.addEventListener('click', function() {
            let ids = Array.from(document.querySelectorAll('.product-check:checked')).map(cb => cb.value);
            if (ids.length === 0) return;
            if (!confirm('Bạn chắc chắn muốn xóa các sản phẩm đã chọn?')) return;
            deleteIds.value = ids.join(',');
            deleteForm.submit();
        });

        updateMultiActionBar();
    });
</script>