<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Cập nhật công nợ Shipper</title>
        <style>
            form {
                width: 50%;
                margin: 20px auto;
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 8px;
            }
            label {
                font-weight: bold;
            }
            input[type="text"], input[type="number"], textarea {
                width: 100%;
                padding: 8px;
                margin: 10px 0;
                border-radius: 4px;
                border: 1px solid #ccc;
            }
            button {
                padding: 8px 16px;
                margin: 5px;
                cursor: pointer;
            }
        </style>
    </head>
    <body>
        <h2 style="text-align:center;">Cập nhật công nợ Shipper </h2>
        <form action="update-shipper-debt" method="post">
            <label>Tên Shipper:</label>
            <input type="text" name="shipperName" value="${debt.shipperName}" readonly>

            <label>Số tiền công nợ cũ:</label>
            <input type="number" name="oldAmount" value="${debt.debtAmount}" readonly>

            <label>Số tiền công nợ mới:</label>
            <input type="number" name="amount" value="${debt.debtAmount}" required>

            <label>Lý do thay đổi:</label>
            <textarea name="reason" required></textarea>

            <!-- Truyền đủ khóa chính để update -->
            <input type="hidden" name="shipperId" value="${debt.shipperId}">
            <input type="hidden" name="date" value="${debt.date}">

            <button type="submit" onclick="return confirm('Bạn có chắc chắn muốn cập nhật công nợ này không?')">Cập nhật công nợ</button>
        </form>
        <!-- Nút xóa nên dùng form riêng hoặc xác nhận JS -->
        <form action="delete-shipper-debt" method="post" style="text-align:center;">
            <input type="hidden" name="shipperId" value="${debt.shipperId}">
            <input type="hidden" name="date" value="${debt.date}">
            <button type="submit" onclick="return confirm('Bạn có chắc chắn muốn xóa công nợ này không?')">Xóa công nợ</button>
        </form>

        <form action="pay-shipper-debt-reason" method="get" style="display:inline;">
            <input type="hidden" name="shipperId" value="${debt.shipperId}">
            <input type="hidden" name="date" value="${debt.date}">
            <button type="submit" style="margin-left:10px; background: #28a745; color: #fff;">Công nợ đã thanh toán</button>
        </form>    
    </body>
</html>