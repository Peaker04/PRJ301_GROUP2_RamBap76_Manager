package model;

import java.sql.Timestamp;
import java.sql.Date;

/**
 * Đại diện cho dữ liệu công nợ của một shipper trong hệ thống. Làm việc với
 * bảng shipper_debts.
 */
public class ShipperDebt {

    // ID shipper – dùng để làm khoá chính xác định người giao hàng nào đang có công nợ
    private int shipperId;  // Dùng shipper_id làm khóa chính

    // Họ và Tên của shipper – dùng để hiển thị người giao hàng nào đang có công nợ
    private String shipperName;

    // Ngày công nợ (ngày ghi nhận công nợ của shipper)
    private Date date;

    // Số tiền công nợ hiện tại của shipper (tính theo đơn đã giao nhưng chưa thanh toán)
    private double debtAmount;

    // Ngày thanh toán (nếu đã thanh toán)
    private Timestamp paymentDate;
    
    private boolean overdue;  // Cột trạng thái quá hạn
    
    private String reason;  // Thêm trường reason để lưu lý do
    

    // Constructor mặc định
    public ShipperDebt() {
    }

    // Constructor với shipperId, shipperName, date và debtAmount
    public ShipperDebt(int shipperId, String shipperName, Date date, double debtAmount) {
        this.shipperId = shipperId;
        this.shipperName = shipperName;
        this.date = date;
        this.debtAmount = debtAmount;
    }

    // Constructor với tất cả các trường (bao gồm paymentDate)
    public ShipperDebt(int shipperId, String shipperName, Date date, double debtAmount, Timestamp paymentDate) {
        this.shipperId = shipperId;
        this.shipperName = shipperName;
        this.date = date;
        this.debtAmount = debtAmount;
        this.paymentDate = paymentDate;
    }

    // Constructor, getters và setters
    public boolean isOverdue() { // Quá hạn hay chưa
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }
    
    // Getter và Setter cho reason (Lý do cập nhật công nợ)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;  // Set lý do cập nhật công nợ
    }
    
    // Getter và Setter cho shipperId
    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    // Getter và Setter cho shipperName
    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    // Getter và Setter cho date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Getter và Setter cho debtAmount
    public double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }

    // Getter và Setter cho paymentDate
    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    // Kiểm tra nếu công nợ đã được thanh toán
    public boolean isPaid() {
        return paymentDate != null;
    }

    /// ToString để dễ dàng hiển thị thông ti
    /// @return n
    @Override
    public String toString() {
        return "ShipperDebt{" +
                "shipperId=" + shipperId +
                ", shipperName='" + shipperName + '\'' +
                ", date=" + date +
                ", debtAmount=" + debtAmount +
                ", paymentDate=" + (paymentDate != null ? paymentDate.toString() : "Chưa thanh toán") +
                ", reason='" + reason + '\'' +  // Hiển thị lý do
                '}';
    }

}
