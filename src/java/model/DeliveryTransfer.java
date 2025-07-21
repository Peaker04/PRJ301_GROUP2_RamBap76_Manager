package model;

import java.util.Date;

public class DeliveryTransfer {

    private int id;
    private int deliveryId;
    private int fromShipperId;
    private int toShipperId;
    private Date requestTime;
    private Date acceptedTime;
    private String reason;
    private String status;

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

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(Date acceptedTime) {
        this.acceptedTime = acceptedTime;
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
}
