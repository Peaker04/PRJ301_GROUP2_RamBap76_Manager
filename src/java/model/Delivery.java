package model;

import java.math.BigDecimal;
import java.util.Date;

public class Delivery {

    private int id;
    private int orderId;
    private int initialShipperId;
    private int currentShipperId;
    private BigDecimal deliveryFee;
    private BigDecimal boxFee;
    private Double collectedAmount;
    private String status;
    private Date actualDeliveryTime;
    private Date assignedTime;
    private Date acceptedTime;
    private String notes;
    private int priorityLevel;
    private Order order;
    private StationReceipt stationReceipt;
    private Customer customer;

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

    public Double getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(Double collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(Date actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public Date getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(Date assignedTime) {
        this.assignedTime = assignedTime;
    }

    public Date getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(Date acceptedTime) {
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
    
     public void setOrder(Order order) {
        this.order = order;
    }

    public void setStationReceipt(StationReceipt stationReceipt) {
        this.stationReceipt = stationReceipt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    
}
