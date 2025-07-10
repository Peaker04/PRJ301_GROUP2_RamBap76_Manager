package model;

import java.util.Date;

public class Order {
    private Customer customer;
    private int id;
    private int customerId;
    private String status;
    private Date orderDate;
    private Date appointmentTime;
    private String notes;
    private Date priorityDeliveryDate;

    public Order() {
    }

    public Order(int id, int customerId, String status, Date orderDate, Date appointmentTime, String notes, Date priorityDeliveryDate) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.orderDate = orderDate;
        this.appointmentTime = appointmentTime;
        this.notes = notes;
        this.priorityDeliveryDate = priorityDeliveryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getPriorityDeliveryDate() {
        return priorityDeliveryDate;
    }

    public void setPriorityDeliveryDate(Date priorityDeliveryDate) {
        this.priorityDeliveryDate = priorityDeliveryDate;
    }
}
