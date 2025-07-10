package model;

public class StationReceiptDetail {
    private int receiptId;
    private int productId;
    private String productName;
    private int quantity;
    private int currentStock;
    private int remainingQuantity;
    private StationReceipt stationReceipt;

    public StationReceiptDetail() {
    }

    public StationReceiptDetail(int receiptId, int productId, String productName, int quantity, int currentStock, int remainingQuantity) {
        this.receiptId = receiptId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.currentStock = currentStock;
        this.remainingQuantity = remainingQuantity;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public StationReceipt getStationReceipt() {
        return stationReceipt;
    }

    public void setStationReceipt(StationReceipt stationReceipt) {
        this.stationReceipt = stationReceipt;
    }
}
