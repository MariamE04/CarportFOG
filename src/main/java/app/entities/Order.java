package app.entities;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

public class Order {
    private int order_id;
    private LocalDate date_created;
    private double total_price;
    private String status;
    private int user_id;
    private Carport carport;
    private Shed shed;
    private List<OrderDetails> orderDetails;

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

    public Order(int order_id, String status) {
        this.order_id = order_id;
        this.status = status;
    }

    public void priceSummation(){
        total_price = 0;
        for (OrderDetails detail : orderDetails) {
            total_price += detail.getMaterial().getPrice()/100*detail.getMaterial().getLength()*detail.getQuantity();
        }
    }

    public String twoDecimals(){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total_price);
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

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", date_created=" + date_created +
                ", total_price=" + total_price +
                ", status='" + status + '\'' +
                ", user_id=" + user_id +
                ", carport=" + carport +
                ", shed=" + shed +
                ", orderDetails=" + orderDetails +
                '}';
    }
    }