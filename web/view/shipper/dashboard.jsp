<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shipper Dashboard - RamBap76</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/shipper.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <div class="text-center mb-4">
                        <h4 class="text-white">RamBap76</h4>
                        <p class="text-white-50">Shipper Dashboard</p>
                    </div>
                    
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link active text-white" href="${pageContext.request.contextPath}/shipper/dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i>
                                Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/shipper/notifications">
                                <i class="fas fa-bell me-2"></i>
                                Thông báo
                                <c:if test="${unreadCount > 0}">
                                    <span class="badge bg-danger ms-2">${unreadCount}</span>
                                </c:if>
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>
                                Đăng xuất
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Dashboard</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <span class="text-muted">Xin chào, ${currentUser.fullName}</span>
                        </div>
                    </div>
                </div>

                <c:if test="${param.success == '1'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>
                        Đã nhận đơn hàng thành công!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.success == '2'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>
                        Đã hoàn thành đơn hàng thành công!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.success == '3'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle me-2"></i>
                        Đã gửi yêu cầu chuyển giao thành công!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.error == '1' || param.error == '2' || param.error == '3'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>
                        Có lỗi xảy ra, vui lòng thử lại!
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>


                <div class="row mb-4">
                    <div class="col-md-4">
                        <div class="card income-card">
                            <div class="card-body text-center">
                                <h5 class="card-title">
                                    <i class="fas fa-money-bill-wave me-2"></i>
                                    Thu nhập hôm nay
                                </h5>
                                <h3 class="card-text">
                                    <fmt:formatNumber value="${dailyIncome}" type="currency" currencySymbol="₫"/>
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="row mb-4">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-list me-2"></i>
                                    Đơn hàng được gán (${assignedDeliveries.size()})
                                </h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${empty assignedDeliveries}">
                                        <p class="text-muted text-center">Không có đơn hàng nào được gán</p>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="row">
                                            <c:forEach var="delivery" items="${assignedDeliveries}">
                                                <div class="col-md-6 col-lg-4 mb-3">
                                                    <div class="card delivery-item h-100">
                                                        <div class="card-body">
                                                            <h6 class="card-title">Đơn hàng #${delivery.orderId}</h6>
                                                            <p class="card-text">
                                                                <strong>Khách hàng:</strong> ${delivery.customerName}<br>
                                                                <strong>SĐT:</strong> ${delivery.customerPhone}<br>
                                                                <strong>Địa chỉ:</strong> ${delivery.customerAddress}<br>
                                                                <strong>Phí giao:</strong> <fmt:formatNumber value="${delivery.deliveryFee}" type="currency" currencySymbol="₫"/><br>
                                                                <strong>Phí hộp:</strong> <fmt:formatNumber value="${delivery.boxFee}" type="currency" currencySymbol="₫"/>
                                                            </p>
                                                            <form action="${pageContext.request.contextPath}/shipper/update-delivery-status" method="post" class="d-inline">
                                                                <input type="hidden" name="deliveryId" value="${delivery.id}">
                                                                <input type="hidden" name="status" value="IN_TRANSIT">
                                                                <button type="submit" class="btn btn-primary btn-sm">
                                                                    <i class="fas fa-check me-1"></i>Nhận đơn
                                                                </button>
                                                            </form>
                                                            <a href="${pageContext.request.contextPath}/shipper/request-transfer?deliveryId=${delivery.id}" 
                                                               class="btn btn-warning btn-sm">
                                                                <i class="fas fa-exchange-alt me-1"></i>Chuyển giao
                                                            </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-truck me-2"></i>
                                    Đơn hàng đang giao (${inTransitDeliveries.size()})
                                </h5>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${empty inTransitDeliveries}">
                                        <p class="text-muted text-center">Không có đơn hàng nào đang giao</p>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="row">
                                            <c:forEach var="delivery" items="${inTransitDeliveries}">
                                                <div class="col-md-6 col-lg-4 mb-3">
                                                    <div class="card delivery-item h-100">
                                                        <div class="card-body">
                                                            <h6 class="card-title">Đơn hàng #${delivery.orderId}</h6>
                                                            <p class="card-text">
                                                                <strong>Khách hàng:</strong> ${delivery.customerName}<br>
                                                                <strong>SĐT:</strong> ${delivery.customerPhone}<br>
                                                                <strong>Địa chỉ:</strong> ${delivery.customerAddress}<br>
                                                                <strong>Phí giao:</strong> <fmt:formatNumber value="${delivery.deliveryFee}" type="currency" currencySymbol="₫"/><br>
                                                                <strong>Phí hộp:</strong> <fmt:formatNumber value="${delivery.boxFee}" type="currency" currencySymbol="₫"/>
                                                            </p>
                                                            <button type="button" class="btn btn-success btn-sm" 
                                                                    data-bs-toggle="modal" 
                                                                    data-bs-target="#completeModal${delivery.id}">
                                                                <i class="fas fa-check-double me-1"></i>Hoàn thành
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
<div class="modal" id="completeDeliveryModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="completeModalTitle">Hoàn thành đơn hàng</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/shipper/complete-delivery" method="post">
                    <div class="modal-body">
                        <input type="hidden" name="deliveryId" id="modalDeliveryId">
                        <div class="mb-3">
                            <label for="collectedAmount" class="form-label">Số tiền thu hộ (₫)</label>
                            <input type="number" class="form-control" id="collectedAmount"
                                   name="collectedAmount" required min="0" step="1000">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-success">Hoàn thành</button>
                    </div>
                </form>
            </div>
        </div>
    </div>F
                                            </c:forEach>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>

<script>
        var completeModal = document.getElementById('completeDeliveryModal');
        if (completeModal) {
            completeModal.addEventListener('show.bs.modal', function (event) {
                var button = event.relatedTarget;
                var deliveryId = button.getAttribute('data-delivery-id');
                var orderId = button.getAttribute('data-order-id');

                var modalTitle = completeModal.querySelector('.modal-title');
                var modalDeliveryIdInput = completeModal.querySelector('#modalDeliveryId');
                var collectedAmountInput = completeModal.querySelector('#collectedAmount');

                modalTitle.textContent = 'Hoàn thành đơn hàng #' + orderId;
                modalDeliveryIdInput.value = deliveryId;
                
                collectedAmountInput.value = '';
                setTimeout(function() {
                    collectedAmountInput.focus();
                }, 500);
            });
        }
    </script>
</body>
</html> 
