<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Delivery Details</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container mt-4">
            <h2>Delivery Details #${delivery.id}</h2>

            <div class="card mb-4">
                <div class="card-body">
                    <h5 class="card-title">Order Information</h5>
                    <p><strong>Order ID:</strong> ${delivery.orderId}</p>
                    <p><strong>Status:</strong> ${delivery.status}</p>
                    <p><strong>Assigned Time:</strong> <fmt:formatDate value="${delivery.assignedTime}" pattern="dd/MM/yyyy HH:mm"/></p>

                    <c:if test="${not empty delivery.acceptedTime}">
                        <p><strong>Accepted Time:</strong> <fmt:formatDate value="${delivery.acceptedTime}" pattern="dd/MM/yyyy HH:mm"/></p>
                    </c:if>

                    <c:if test="${not empty delivery.actualDeliveryTime}">
                        <p><strong>Completed Time:</strong> <fmt:formatDate value="${delivery.actualDeliveryTime}" pattern="dd/MM/yyyy HH:mm"/></p>
                    </c:if>
                </div>
            </div>

            <c:if test="${delivery.status == 'ASSIGNED'}">
                <div class="card mb-4">
                    <div class="card-body">
                        <h5 class="card-title">Transfer Delivery</h5>
                        <form action="${pageContext.request.contextPath}/shipper/transfer-request" method="post">
                            <input type="hidden" name="delivery_id" value="${delivery.id}">

                            <div class="mb-3">
                                <label class="form-label">Transfer to Shipper ID:</label>
                                <input type="number" name="to_shipper_id" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Reason:</label>
                                <textarea name="reason" class="form-control" rows="3" required></textarea>
                            </div>

                            <button type="submit" class="btn btn-warning">Request Transfer</button>
                        </form>
                    </div>
                </div>
            </c:if>

            <a href="${pageContext.request.contextPath}/shipper/dashboard" class="btn btn-secondary">Back to Dashboard</a>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>