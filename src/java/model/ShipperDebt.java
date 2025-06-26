package model;
// Đại diện cho dữ liệu công nợ của một shipper trong hệ thống
// Làm việc với bảng shipper_debts

public class ShipperDebt {
    
    // ID của shipper – dùng để xác định người giao hàng nào đang có công nợ
    private int shipperId;

    // Số tiền công nợ hiện tại của shipper (tính theo đơn đã giao nhưng chưa thanh toán)
    private double debtAmount;

    public ShipperDebt() {
    }

    public ShipperDebt(int shipperId, double debtAmount) {
        this.shipperId = shipperId;
        this.debtAmount = debtAmount;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }

    @Override
    public String toString() {
        return "ShipperDebt{"
                + "shipperId=" + shipperId
                + ", debtAmount=" + debtAmount
                + '}';
    }
}
