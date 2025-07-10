package model;

import java.sql.*;
import java.util.List;

public class StationReceipt {
    private int id;
    private Date receiptDate;
    private String stationName;
    private double transportFee;
    private int totalQuantity;
    private boolean Verified;
    private Integer verifiedBy;
    private Timestamp verifiedAt;
    private Date expirationDate;
    private List<StationReceiptDetail> details;

    public StationReceipt() {
    }

    public StationReceipt(int id, Date receiptDate, String stationName, double transportFee, int totalQuantity, boolean Verified, Integer verifiedBy, Timestamp verifiedAt, Date expirationDate, List<StationReceiptDetail> details) {
        this.id = id;
        this.receiptDate = receiptDate;
        this.stationName = stationName;
        this.transportFee = transportFee;
        this.totalQuantity = totalQuantity;
        this.Verified = Verified;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = verifiedAt;
        this.expirationDate = expirationDate;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(double transportFee) {
        this.transportFee = transportFee;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public boolean isVerified() {
        return Verified;
    }

    public void setIsVerified(boolean isVerified) {
        this.Verified = isVerified;
    }

    public Integer getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Timestamp getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Timestamp verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public List<StationReceiptDetail> getDetails() {
        return details;
    }

    public void setDetails(List<StationReceiptDetail> details) {
        this.details = details;
    }
}
