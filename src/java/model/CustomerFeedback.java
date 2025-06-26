package model;

import java.sql.Timestamp;

public class CustomerFeedback {
    private int id;                // ID đánh giá
    private int customerId;       // Khách hàng bị đánh giá
    private int shipperId;        // Shipper đánh giá
    private int orderId;          // Đánh giá trong đơn hàng nào
    private String notes;         // Ghi chú
    private int rating;           // Điểm (1–5)
    private Timestamp createdAt;  // Ngày tạo đánh giá

    public CustomerFeedback() {}

    public CustomerFeedback(int id, int customerId, int shipperId, int orderId, String notes, int rating, Timestamp createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.shipperId = shipperId;
        this.orderId = orderId;
        this.notes = notes;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CustomerFeedback{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", shipperId=" + shipperId +
                ", orderId=" + orderId +
                ", notes='" + notes + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }
}
