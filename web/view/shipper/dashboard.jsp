<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Shipper Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shipper.css">
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <!-- Main Content Area -->
                <div class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                    <h2 class="welcome">Shipper Dashboard</h2>

                    <!-- Success or Error Messages -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <!-- Assigned and In-Transit Deliveries -->
                    <div class="row g-4">
                        <!-- Assigned Deliveries -->
                        <div class="col-md-6">
                            <h5 class="mb-3 fw-semibold">Assigned Deliveries</h5>
                            <c:choose>
                                <c:when test="${not empty assignedDeliveries}">
                                    <c:forEach items="${assignedDeliveries}" var="delivery">
                                        <div class="delivery-card">
                                            <h6 class="fw-bold mb-1">Delivery #${delivery.id}</h6>
                                            <p class="mb-1">Order ID: ${delivery.orderId}</p>
                                            <p class="mb-2">
                                                Priority:
                                                <span class="badge bg-secondary">
                                                    <c:choose>
                                                        <c:when test="${delivery.priorityLevel == 1}">Low</c:when>
                                                        <c:when test="${delivery.priorityLevel == 2}">Medium</c:when>
                                                        <c:when test="${delivery.priorityLevel == 3}">High</c:when>
                                                    </c:choose>
                                                </span>
                                            </p>
                                            <form action="${pageContext.request.contextPath}/shipper/delivery-action" method="post">
                                                <input type="hidden" name="delivery_id" value="${delivery.id}">
                                                <input type="hidden" name="action" value="accept">
                                                <button type="submit" class="btn btn-primary btn-sm w-100">Accept Delivery</button>
                                            </form>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">No assigned deliveries</div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- In Transit Deliveries -->
                        <div class="col-md-6">
                            <h5 class="mb-3 fw-semibold">In Transit Deliveries</h5>
                            <c:choose>
                                <c:when test="${not empty inTransitDeliveries}">
                                    <c:forEach items="${inTransitDeliveries}" var="delivery">
                                        <div class="delivery-card">
                                            <h6 class="fw-bold mb-1">Delivery #${delivery.id}</h6>
                                            <p class="mb-1">Order ID: ${delivery.orderId}</p>
                                            <p class="mb-2">Accepted: <fmt:formatDate value="${delivery.acceptedTime}" pattern="dd/MM/yyyy HH:mm"/></p>
                                            <form action="${pageContext.request.contextPath}/shipper/delivery-action" method="post">
                                                <input type="hidden" name="delivery_id" value="${delivery.id}">
                                                <input type="hidden" name="action" value="complete">
                                                <label class="form-label">Collected Amount:</label>
                                                <input type="number" step="0.01" name="collected_amount" class="form-control" required>
                                                <button type="submit" class="btn btn-success btn-sm w-100">Complete Delivery</button>
                                            </form>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">No deliveries in transit</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Recent Notifications -->
                    <div class="row mt-4">
                        <div class="col-12">
                            <h5 class="mb-3 fw-semibold">Recent Notifications</h5>
                            <c:choose>
                                <c:when test="${not empty notifications}">
                                    <div class="notification-list">
                                        <c:forEach items="${notifications}" var="notification">
                                            <div class="notification-item ${notification.type == 'TRANSFER' ? 'transfer-urgent' : ''}">
                                                <div class="d-flex justify-content-between align-items-start">
                                                    <div>
                                                        <strong>${notification.type}</strong>
                                                        <p class="mb-0">${notification.message}</p>
                                                    </div>
                                                    <small class="text-muted"><fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">No new notifications</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                </div> <!-- end main-content -->
            </div> <!-- end row -->
        </div> <!-- end container-fluid -->

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>