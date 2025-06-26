

package model;

// Đại diện cho 1 bản ghi (dòng) trong bảng transactions trong cơ sở dữ liệu
// Là cầu nối giữa CSDL và code Java
// Giúp thao tác rõ ràng, dễ hiểu với từng giao dịch
// Hỗ trợ tính toán công nợ shipper, tạo báo cáo thu – chi

public class Transaction {
    
// Mã giao dịch (ID duy nhất cho mỗi bản ghi trong bảng transactions)
private int id;

// Mã của shipper thực hiện giao dịch (liên kết với bảng shippers.user_id)
private int shipperId;

// Số tiền của giao dịch (dương nếu thu tiền, âm nếu hoàn tiền/phạt)
private double amount;

// Loại giao dịch: 
// - 'COLLECT' = thu tiền từ khách hàng,
// - 'PAYMENT' = nộp lại tiền cho hệ thống,
// - 'REFUND' = hoàn tiền,
// - 'FINE' = phạt shipper
private String type;

// Mô tả chi tiết giao dịch (ví dụ: "Thu hộ đơn hàng #123", "Phạt do giao muộn", ...)
private String description;
;

    public Transaction() {}
       
    public Transaction(int id, int shipperId, double amount, String type, String description) {
        this.id = id;
        this.shipperId = shipperId;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", shipperId=" + shipperId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
    
}