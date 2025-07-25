<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shipper Notifications</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shipper.css">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Main Content -->
            <div class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                <h2 class="welcome">Notifications</h2>

                <!-- Alerts -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <!-- Notification List -->
                <div class="row mt-3">
                    <div class="col-12">
                        <c:choose>
                            <c:when test="${not empty notifications}">
                                <div class="notification-list">
                                    <c:forEach items="${notifications}" var="notification">
                                        <div class="notification-item 
                                            ${notification.read ? 'read' : 'unread'} 
                                            ${notification.type == 'TRANSFER' ? 'transfer-notification' : ''} 
                                            ${notification.type == 'URGENT' ? 'urgent-notification' : ''}">
                                            
                                            <div class="d-flex justify-content-between align-items-start">
                                                <div>
                                                    <h6 class="fw-bold mb-1">${notification.type}</h6>
                                                    <p class="mb-1">${notification.message}</p>
                                                </div>
                                                <small class="text-muted">
                                                    <fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </small>
                                            </div>

                                            <c:if test="${!notification.read}">
                                                <form method="post" action="${pageContext.request.contextPath}/shipper/notifications">
                                                    <input type="hidden" name="notification_id" value="${notification.id}">
                                                    <button type="submit" class="btn btn-sm btn-outline-secondary mt-2">Mark as Read</button>
                                                </form>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">No notifications found</div>
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