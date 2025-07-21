<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông báo - RamBap76</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_layout.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <!-- Main content -->
                <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                    <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                        <h1 class="h2">Thông báo</h1>
                    </div>

                    <!-- Success/Error Messages -->
                    <c:if test="${param.success == '1'}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="fas fa-check-circle me-2"></i>
                            Đã xử lý yêu cầu chuyển giao thành công!
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>
                    <c:if test="${param.error == '1'}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            Có lỗi xảy ra khi xử lý yêu cầu!
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Pending Transfer Requests -->
                    <div class="row mb-4">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-exchange-alt me-2"></i>
                                        Yêu cầu chuyển giao đang chờ (${pendingTransfers.size()})
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${empty pendingTransfers}">
                                            <p class="text-muted text-center">Không có yêu cầu chuyển giao nào đang chờ</p>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="row">
                                                <c:forEach var="transfer" items="${pendingTransfers}">
                                                    <div class="col-md-6 col-lg-4 mb-3">
                                                        <div class="card notification-item h-100 unread">
                                                            <div class="card-body">
                                                                <h6 class="card-title">
                                                                    <i class="fas fa-exchange-alt me-2 text-primary"></i>
                                                                    Yêu cầu chuyển giao
                                                                </h6>
                                                                <p class="card-text">
                                                                    <strong>Từ:</strong> ${transfer.fromShipperName}<br>
                                                                    <strong>Đơn hàng:</strong> ${transfer.deliveryInfo}<br>
                                                                    <strong>Lý do:</strong> ${transfer.reason}<br>
                                                                    <strong>Thời gian:</strong> 
                                                                    <fmt:formatDate value="${transfer.requestTime}" pattern="dd/MM/yyyy HH:mm"/>
                                                                </p>
                                                                <div class="d-flex gap-2">
                                                                    <form action="${pageContext.request.contextPath}/shipper/handle-transfer" method="post" class="d-inline">
                                                                        <input type="hidden" name="transferId" value="${transfer.id}">
                                                                        <input type="hidden" name="action" value="accept">
                                                                        <button type="submit" class="btn btn-success btn-sm">
                                                                            <i class="fas fa-check me-1"></i>Chấp nhận
                                                                        </button>
                                                                    </form>
                                                                    <form action="${pageContext.request.contextPath}/shipper/handle-transfer" method="post" class="d-inline">
                                                                        <input type="hidden" name="transferId" value="${transfer.id}">
                                                                        <input type="hidden" name="action" value="reject">
                                                                        <button type="submit" class="btn btn-danger btn-sm">
                                                                            <i class="fas fa-times me-1"></i>Từ chối
                                                                        </button>
                                                                    </form>
                                                                </div>
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

                    <!-- All Notifications -->
                    <div class="row">
                        <div class="col-12">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-bell me-2"></i>
                                        Tất cả thông báo (${notifications.size()})
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <c:choose>
                                        <c:when test="${empty notifications}">
                                            <p class="text-muted text-center">Không có thông báo nào</p>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="list-group">
                                                <c:forEach var="notification" items="${notifications}">
                                                    <div class="list-group-item notification-item ${!notification.read ? 'unread' : ''}">
                                                        <div class="d-flex w-100 justify-content-between">
                                                            <h6 class="mb-1">
                                                                <c:choose>
                                                                    <c:when test="${notification.type == 'TRANSFER'}">
                                                                        <i class="fas fa-exchange-alt me-2 text-primary"></i>
                                                                    </c:when>
                                                                    <c:when test="${notification.type == 'URGENT'}">
                                                                        <i class="fas fa-exclamation-triangle me-2 text-warning"></i>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <i class="fas fa-info-circle me-2 text-info"></i>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                ${notification.message}
                                                            </h6>
                                                            <small class="text-muted">
                                                                <fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                            </small>
                                                        </div>
                                                        <c:if test="${!notification.read}">
                                                            <small class="text-primary">
                                                                <i class="fas fa-circle me-1"></i>Chưa đọc
                                                            </small>
                                                        </c:if>
                                                    </div>
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

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 