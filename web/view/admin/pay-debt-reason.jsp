<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Xác nhận công nợ đã thanh toán</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f7f7f7;
        }
        .container {
            width: 420px;
            margin: 40px auto;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            padding: 32px 28px 24px 28px;
        }
        h2 {
            text-align: center;
            color: #28a745;
            margin-bottom: 24px;
        }
        label {
            font-weight: bold;
            display: block;
            margin-bottom: 8px;
        }
        .readonly {
            background: #f0f0f0;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 18px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        textarea {
            min-height: 80px;
            resize: vertical;
        }
        button {
            width: 100%;
            background: #28a745;
            color: #fff;
            border: none;
            padding: 12px;
            border-radius: 4px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 8px;
        }
        button:hover {
            background: #218838;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 18px;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Xác nhận công nợ đã thanh toán</h2>
        <form action="pay-shipper-debt" method="post">
            <label>Tên Shipper:</label>
            <input type="text" class="readonly" value="${shipperName}" readonly>

            <label>Ngày công nợ:</label>
            <input type="text" class="readonly" value="${date}" readonly>

            <label>Lý do thanh toán <span style="color:red;">*</span>:</label>
            <textarea name="reason" required placeholder="Nhập lý do xác nhận thanh toán..."></textarea>

            <input type="hidden" name="shipperId" value="${shipperId}">
            <input type="hidden" name="date" value="${date}">
            <button type="submit" onclick="return confirm('Xác nhận công nợ này đã được thanh toán?')">Xác nhận thanh toán</button>
        </form>
        <a href="shipper-debt" class="back-link">← Quay lại danh sách công nợ</a>
    </div>
</body>
</html>