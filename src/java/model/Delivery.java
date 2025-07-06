package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Delivery {
    private int id;
    private int orderId;
    private int initialShipperId;
    private int currentShipperId;
    private Integer completedShipperId;
    private BigDecimal deliveryFee;
    private BigDecimal boxFee;
    private BigDecimal collectedAmount;
    private String status;
    private LocalDateTime actualDeliveryTime;
    private LocalDateTime assignedTime;
    private LocalDateTime acceptedTime;
    private String notes;
    private int priorityLevel;
    private LocalDateTime estimatedDeliveryTime;
    private Integer stationReceiptId;
    
    // Additional fields for display purposes
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String shipperName;
    private String orderStatus;

    public Delivery() {
    }

    public Delivery(int id, int orderId, int initialShipperId, int currentShipperId, 
                   BigDecimal deliveryFee, BigDecimal boxFee, String status) {
        this.id = id;
        this.orderId = orderId;
        this.initialShipperId = initialShipperId;
        this.currentShipperId = currentShipperId;
        this.deliveryFee = deliveryFee;
        this.boxFee = boxFee;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getInitialShipperId() {
        return initialShipperId;
    }

    public void setInitialShipperId(int initialShipperId) {
        this.initialShipperId = initialShipperId;
    }

    public int getCurrentShipperId() {
        return currentShipperId;
    }

    public void setCurrentShipperId(int currentShipperId) {
        this.currentShipperId = currentShipperId;
    }

    public Integer getCompletedShipperId() {
        return completedShipperId;
    }

    public void setCompletedShipperId(Integer completedShipperId) {
        this.completedShipperId = completedShipperId;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getBoxFee() {
        return boxFee;
    }

    public void setBoxFee(BigDecimal boxFee) {
        this.boxFee = boxFee;
    }

    public BigDecimal getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(BigDecimal collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public LocalDateTime getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(LocalDateTime assignedTime) {
        this.assignedTime = assignedTime;
    }

    public LocalDateTime getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(LocalDateTime acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public Integer getStationReceiptId() {
        return stationReceiptId;
    }

    public void setStationReceiptId(Integer stationReceiptId) {
        this.stationReceiptId = stationReceiptId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", status='" + status + '\'' +
                ", currentShipperId=" + currentShipperId +
                '}';
    }
} 