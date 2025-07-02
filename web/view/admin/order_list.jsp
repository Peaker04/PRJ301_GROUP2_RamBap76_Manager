<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

<style>
    body {
        background: #f5f6fa;
    }
    .custom-card {
        border-radius: 22px !important;
        box-shadow: 0 2px 18px 0 rgba(80,80,150,0.06);
        background: #fff;
        border: none;
    }
    .custom-table th, .custom-table td {
        vertical-align: middle;
        border-top: none;
    }
    .badge {
        font-size: 0.9em;
        border-radius: 8px;
        padding: 6px 14px;
        letter-spacing: 0.03em;
    }
    .filter-tabs .nav-link {
        border-radius: 14px !important;
        font-weight: 500;
        padding: 7px 22px;
        color: #666;
    }
    .filter-tabs .nav-link.active {
        background: #f0f4fa;
        color: #3366ff;
    }
    .search-box {
        max-width: 320px;
        border-radius: 10px;
        background: #f5f6fa;
        border: none;
    }
    .table-rounded {
        border-radius: 14px;
        overflow: hidden;
    }
    .icon-action {
        font-size: 1.1rem;
        margin: 0 5px;
        cursor: pointer;
    }
</style>

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
                            <option value="desc" ${param.sort == null || param.sort == 'desc' ? 'selected' : ''}>Mới nhất</option>
                            <option value="asc"  ${param.sort == 'asc' ? 'selected' : ''}>Cũ nhất</option>
                        </select>
                        <a href="${pageContext.request.contextPath}/admin/orders/create" class="btn btn-primary px-4 rounded-3 ms-1">
                            <i class="bi bi-plus-lg"></i> New Order
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
            <table class="table custom-table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th><input type="checkbox"></th>
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
                            <td><input type="checkbox"></td>
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
                                <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${order.id}" class="icon-action text-primary"><i class="bi bi-eye"></i></a>
                                <a href="${pageContext.request.contextPath}/admin/orders/detail?id=${order.id}&edit=1" class="icon-action text-warning"><i class="bi bi-pencil-square"></i></a>
                                <form action="${pageContext.request.contextPath}/admin/orders/detail" method="post" style="display:inline;" onsubmit="return confirm('Bạn chắc chắn muốn xóa đơn này?');">
                                    <input type="hidden" name="id" value="${order.id}"/>
                                    <input type="hidden" name="action" value="delete"/>
                                    <button type="submit" class="icon-action text-danger btn btn-link p-0 m-0" title="Xóa">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>                          
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
                
