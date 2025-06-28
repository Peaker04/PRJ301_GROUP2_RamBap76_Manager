<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Yêu cầu chuyển giao - RamBap76</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/shipper.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <div class="text-center mb-4">
                        <h4 class="text-white">RamBap76</h4>
                        <p class="text-white-50">Shipper Dashboard</p>
                    </div>
                    
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/shipper/dashboard">
                                <i class="fas fa-tachometer-alt me-2"></i>
                                Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/shipper/notifications">
                                <i class="fas fa-bell me-2"></i>
                                Thông báo
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

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Yêu cầu chuyển giao đơn hàng</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/shipper/dashboard" class="btn btn-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Quay lại Dashboard
                        </a>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-8 mx-auto">
                        <!-- Delivery Information -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-info-circle me-2"></i>
                                    Thông tin đơn hàng
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <p><strong>Mã đơn hàng:</strong> #${delivery.orderId}</p>
                                        <p><strong>Khách hàng:</strong> ${delivery.customerName}</p>
                                        <p><strong>Số điện thoại:</strong> ${delivery.customerPhone}</p>
                                    </div>
                                    <div class="col-md-6">
                                        <p><strong>Địa chỉ:</strong> ${delivery.customerAddress}</p>
                                        <p><strong>Phí giao hàng:</strong> <fmt:formatNumber value="${delivery.deliveryFee}" type="currency" currencySymbol="₫"/></p>
                                        <p><strong>Phí hộp:</strong> <fmt:formatNumber value="${delivery.boxFee}" type="currency" currencySymbol="₫"/></p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Transfer Request Form -->
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-exchange-alt me-2"></i>
                                    Yêu cầu chuyển giao
                                </h5>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/shipper/request-transfer" method="post">
                                    <input type="hidden" name="deliveryId" value="${delivery.id}">
                                    
                                    <div class="mb-3">
                                        <label for="toShipperId" class="form-label">Chuyển giao cho shipper:</label>
                                        <select class="form-select" id="toShipperId" name="toShipperId" required>
                                            <option value="">-- Chọn shipper --</option>
                                            <c:forEach var="shipper" items="${availableShippers}">
                                                <option value="${shipper.userId}">
                                                    ${shipper.fullName} - ${shipper.area}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="reason" class="form-label">Lý do chuyển giao:</label>
                                        <textarea class="form-control" id="reason" name="reason" rows="4" 
                                                  placeholder="Nhập lý do chuyển giao đơn hàng..." required></textarea>
                                    </div>
                                    
                                    <div class="d-flex gap-2">
                                        <a href="${pageContext.request.contextPath}/shipper/dashboard" class="btn btn-secondary">
                                            <i class="fas fa-times me-2"></i>Hủy
                                        </a>
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-paper-plane me-2"></i>Gửi yêu cầu
                                        </button>
                                    </div>
                                </form>
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