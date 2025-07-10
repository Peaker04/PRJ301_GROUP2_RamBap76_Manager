<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Quản lý công nợ Shipper</title>
        <style>
            table {
                border-collapse: collapse;
                width: 80%;
                margin: 20px auto;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 8px;
                text-align: center;
            }
            th {
                background: #f2f2f2;
            }
            .paid {
                color: green;
            }
            .unpaid {
                color: red;
            }
            .overdue {
                color: red;
            }
            button {
                padding: 4px 12px;
            }
            .reminder {
                color: orange;
                font-weight: bold;
            }
            /* Định dạng nút cập nhật */
            .update-btn {
                color: white;
                background-color: #28a745;
                border: none;
                padding: 5px 15px;
                cursor: pointer;
                text-align: center;
                display: inline-block;
                text-decoration: none;
                border-radius: 4px;
            }
            /* Căn chỉnh nút cập nhật nằm bên ngoài bảng nhưng trong mỗi hàng */
            .update-container {
                display: flex;
                justify-content: center;
                align-items: center;
            }
        </style>
    </head>
    <body>
        <h2 style="text-align:center;">Danh sách công nợ Shipper</h2>

        <table>
            <tr>
                <th>Họ và Tên</th>
                <th>Ngày tạo nợ</th>
                <th>Số tiền</th>
                <th>Ngày thanh toán</th>
                <th>Trạng thái</th>
                <th>Trạng thái quá hạn</th>
                <th>Hành động</th>
            </tr>
            <c:forEach var="debt" items="${debts}">
                <tr>
                    <td>${debt.shipperName}</td> <!-- Hiển thị tên của shipper -->
                    <td>${debt.date}</td> <!-- Hiển thị ngày công nợ -->
                    <td>${debt.debtAmount}</td> <!-- Hiển thị số tiền công nợ -->
                    <td>
                        <c:choose>
                            <c:when test="${debt.paymentDate != null}">
                                ${debt.paymentDate} <!-- Hiển thị ngày thanh toán nếu đã thanh toán -->
                            </c:when>
                            <c:otherwise>
                                - <!-- Nếu chưa thanh toán, hiển thị dấu - -->
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${debt.paymentDate != null}">
                                <span class="paid">Đã thanh toán</span> <!-- Trạng thái đã thanh toán -->
                            </c:when>
                            <c:otherwise>
                                <span class="unpaid">Chưa thanh toán</span> <!-- Trạng thái chưa thanh toán -->
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <!-- Cột Trạng thái quá hạn -->
                    <td>
                        <c:choose>
                            <c:when test="${debt.paymentDate == null && debt.overdue}">
                                <span class="overdue">Đã quá hạn</span>
                            </c:when>
                            <c:otherwise>
                                <span>Chưa quá hạn</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${debt.paymentDate == null && debt.overdue}">
                                <span class="reminder">Công nợ quá hạn, vui lòng xử lý</span>
                            </c:when>
                            <c:otherwise>
                                <span></span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <!-- Nút Cập nhật nằm bên ngoài bảng nhưng vẫn ở mỗi hàng -->
                    <td class="update-container">
                    <td>
                        <a href="debt-history?shipperId=${debt.shipperId}&date=${debt.date}" style="margin-right: 10px;">Chi tiết</a>
                        <a href="update-shipper-debt?shipperId=${debt.shipperId}&date=${debt.date}">Cập nhật</a>
                    </td>
                    </td>

                </tr>
            </c:forEach>
        </table>
    </body>
</html>
