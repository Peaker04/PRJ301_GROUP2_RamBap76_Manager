//  Đại diện cho mỗi người giao hàng (shipper) trong hệ thống
//  Làm việc với dữ liệu từ bảng shippers và users
package model;


public class Shipper {
   // Mã định danh của shipper, dùng để phân biệt shipper này với shipper khác
    private int id;

    // Tên của shipper, giúp nhận diện shipper khi cần
    private String name;

    // Khu vực mà shipper phụ trách giao hàng
    private String area;

    // Mức độ ưu tiên của shipper trong việc nhận đơn hàng (1 = cao nhất, 3 = thấp nhất)
    private int priorityLevel;

    // Thu nhập hàng ngày của shipper (có thể tính toán từ số đơn hàng giao được)
    private double dailyIncome;

    public Shipper() {
    }
    
    public Shipper(int id, String name, String area, int priorityLevel, double dailyIncome) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.priorityLevel = priorityLevel;
        this.dailyIncome = dailyIncome;
    }

       
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", area='" + area + '\'' +
                ", priorityLevel=" + priorityLevel +
                ", dailyIncome=" + dailyIncome +
                '}';
    }
    
}

