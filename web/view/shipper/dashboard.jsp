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

                <!-- Main Content -->
                <div class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                    <h2 class="mt-3">Shipper Dashboard</h2>

                    <!-- Messages -->
                    <c:if test="${not empty message}">
                        <div class="alert alert-success">${message}</div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <div class="row mt-4">
                        <!-- Assigned Deliveries -->
                        <div class="col-md-6">
                            <h4>Assigned Deliveries</h4>
                            <c:choose>
                                <c:when test="${not empty assignedDeliveries}">
                                    <c:forEach items="${assignedDeliveries}" var="delivery">
                                        <div class="delivery-card">
                                            <h5>Delivery #${delivery.id}</h5>
                                            <p>Order ID: ${delivery.orderId}</p>
                                            <p>Priority: 
                                                <c:choose>
                                                    <c:when test="${delivery.priorityLevel == 1}">Low</c:when>
                                                    <c:when test="${delivery.priorityLevel == 2}">Medium</c:when>
                                                    <c:when test="${delivery.priorityLevel == 3}">High</c:when>
                                                </c:choose>
                                            </p>
                                            <form action="${pageContext.request.contextPath}/shipper/delivery-action" method="post">
                                                <input type="hidden" name="delivery_id" value="${delivery.id}">
                                                <input type="hidden" name="action" value="accept">
                                                <button type="submit" class="btn btn-primary btn-sm">Accept Delivery</button>
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
                            <h4>In Transit Deliveries</h4>
                            <c:choose>
                                <c:when test="${not empty inTransitDeliveries}">
                                    <c:forEach items="${inTransitDeliveries}" var="delivery">
                                        <div class="delivery-card">
                                            <h5>Delivery #${delivery.id}</h5>
                                            <p>Order ID: ${delivery.orderId}</p>
                                            <p>Accepted: <fmt:formatDate value="${delivery.acceptedTime}" pattern="dd/MM/yyyy HH:mm"/></p>
                                            <form action="${pageContext.request.contextPath}/shipper/delivery-action" method="post">
                                                <input type="hidden" name="delivery_id" value="${delivery.id}">
                                                <input type="hidden" name="action" value="complete">
                                                <div class="mb-2">
                                                    <label class="form-label">Collected Amount:</label>
                                                    <input type="number" step="0.01" name="collected_amount" class="form-control" required>
                                                </div>
                                                <button type="submit" class="btn btn-success btn-sm">Complete Delivery</button>
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

                    <!-- Notifications -->
                    <div class="row mt-4">
                        <div class="col-12">
                            <h4>Recent Notifications</h4>
                            <div class="notification-list">
                                <c:choose>
                                    <c:when test="${not empty notifications}">
                                        <c:forEach items="${notifications}" var="notification">
                                            <div class="notification-item ${notification.type == 'TRANSFER' ? 'transfer-urgent' : ''}">
                                                <div class="d-flex justify-content-between">
                                                    <div>
                                                        <strong>${notification.type}</strong>
                                                        <p>${notification.message}</p>
                                                    </div>
                                                    <small><fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="alert alert-info">No new notifications</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>