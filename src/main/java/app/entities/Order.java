package app.entities;

import java.time.LocalDate;
import java.util.List;

public class Order {
    int order_id;
    LocalDate date_created;
    double total_price;
    String status;
    int user_id;
    Carport carport;
    Shed shed;
    List<OrderDetails> orderDetails;

    public Order(int order_id, LocalDate date_created, double total_price, String status, int user_id, Carport carport, Shed shed) {
        this.order_id = order_id;
        this.date_created = date_created;
        this.total_price = total_price;
        this.status = status;
        this.user_id = user_id;
        this.carport = carport;
        this.shed = shed;
    }

    public Order(LocalDate date_created, double total_price, String status, int user_id, Carport carport, Shed shed) {
        this.date_created = date_created;
        this.total_price = total_price;
        this.status = status;
        this.user_id = user_id;
        this.carport = carport;
        this.shed = shed;
    }

    public void priceSummation(){
        total_price = 0;
        for (OrderDetails detail : orderDetails) {
            total_price += detail.getMaterials().getPrice()*detail.getMaterials().getAmount();
        }
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Carport getCarport() {
        return carport;
    }

    public void setCarport(Carport carport) {
        this.carport = carport;
    }

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

}