package model;

import java.time.LocalDateTime;

public class DeliveryTransfer {
    private int id;
    private int deliveryId;
    private int fromShipperId;
    private int toShipperId;
    private LocalDateTime requestTime;
    private LocalDateTime acceptedTime;
    private LocalDateTime expirationTime;
    private String reason;
    private String status;
    
    // Additional fields for display purposes
    private String fromShipperName;
    private String toShipperName;
    private String deliveryInfo;

    public DeliveryTransfer() {
    }

    public DeliveryTransfer(int deliveryId, int fromShipperId, int toShipperId, String reason) {
        this.deliveryId = deliveryId;
        this.fromShipperId = fromShipperId;
        this.toShipperId = toShipperId;
        this.reason = reason;
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getFromShipperId() {
        return fromShipperId;
    }

    public void setFromShipperId(int fromShipperId) {
        this.fromShipperId = fromShipperId;
    }

    public int getToShipperId() {
        return toShipperId;
    }

    public void setToShipperId(int toShipperId) {
        this.toShipperId = toShipperId;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public LocalDateTime getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(LocalDateTime acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromShipperName() {
        return fromShipperName;
    }

    public void setFromShipperName(String fromShipperName) {
        this.fromShipperName = fromShipperName;
    }

    public String getToShipperName() {
        return toShipperName;
    }

    public void setToShipperName(String toShipperName) {
        this.toShipperName = toShipperName;
    }

    public String getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(String deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    @Override
    public String toString() {
        return "DeliveryTransfer{" +
                "id=" + id +
                ", deliveryId=" + deliveryId +
                ", fromShipperId=" + fromShipperId +
                ", toShipperId=" + toShipperId +
                ", status='" + status + '\'' +
                '}';
    }
} 