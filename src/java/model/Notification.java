package model;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private int shipperId;
    private String message;
    private String type;
    private LocalDateTime createdAt;
    private boolean isRead;
    
    // Additional field for display purposes
    private String shipperName;

    public Notification() {
    }

    public Notification(int shipperId, String message, String type) {
        this.shipperId = shipperId;
        this.message = message;
        this.type = type;
        this.isRead = false;
    }

    // Getters and Setters
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", shipperId=" + shipperId +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", isRead=" + isRead +
                '}';
    }
} 