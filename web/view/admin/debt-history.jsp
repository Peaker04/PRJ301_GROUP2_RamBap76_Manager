<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Lịch sử thay đổi công nợ</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f8f8f8;
            }
            .container {
                width: 80%;
                margin: 30px auto;
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 8px #ccc;
                padding: 24px;
            }
            h2 {
                text-align: center;
                color: #333;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 10px 8px;
                text-align: center;
            }
            th {
                background: #f2f2f2;
                color: #222;
            }
            tr:nth-child(even) {
                background: #fafafa;
            }
            .back-btn {
                margin: 20px 0;
                display: inline-block;
                padding: 8px 18px;
                background: #007bff;
                color: #fff;
                border-radius: 4px;
                text-decoration: none;
            }
            .back-btn:hover {
                background: #0056b3;
            }
            .reason {
                text-align: left;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Lịch sử thay đổi công nợ Shipper</h2>
            <a href="shipper-debt" class="back-btn">← Quay lại danh sách công nợ</a>
            <table>
                <tr>
                    <th>Ngày thay đổi</th>
                    <th>Số tiền cũ</th>
                    <th>Số tiền mới</th>
                    <th>Lý do thay đổi</th>
                    <th>Người thay đổi</th>
                    <th>Vai trò</th>
                </tr>
                <c:forEach var="h" items="${historyList}">
                    <tr>
                        <td>${h.changedAt}</td>
                        <td>${h.oldAmount}</td>
                        <td>${h.newAmount}</td>
                        <td class="reason">${h.changeReason}</td>
                        <td>${h.changerName}</td>
                        <td>${h.changerRole}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty historyList}">
                    <tr>
                        <td colspan="6" style="color: #888;">Chưa có lịch sử thay đổi nào cho công nợ này.</td>
                    </tr>
                </c:if>
            </table>
        </div>
    </body>
</html>