package model;

public class Shipper {
    private int userId; // Đây là user_id từ bảng users, cũng là ID liên kết của Shipper
    private String name; // full_name từ bảng users
    private String area;
    private int priorityLevel;
    private double dailyIncome;

    public Shipper() {
    }

    // Constructor chính xác mà DAO đang gọi
    public Shipper(int userId, String name, String area, int priorityLevel, double dailyIncome) {
        this.userId = userId;
        this.name = name;
        this.area = area;
        this.priorityLevel = priorityLevel;
        this.dailyIncome = dailyIncome;
    }

    // Getters và Setters
    public int getUserId() { // PHẢI CÓ phương thức này
        return userId;
    }

    public void setUserId(int userId) { // PHẢI CÓ phương thức này
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public double getDailyIncome() {
        return dailyIncome;
    }

    public void setDailyIncome(double dailyIncome) {
        this.dailyIncome = dailyIncome;
    }

    @Override
    public String toString() {
        return "Shipper{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", priorityLevel=" + priorityLevel +
                ", dailyIncome=" + dailyIncome +
                '}';
    }
}