package app.entities;

import java.time.LocalDate;
import java.util.List;

public class Order {
    int order_id;
    LocalDate date_created;
    double total_price;
    String status;
    int user_id;
    int carport_id;
    int quote_id;

    public Order(int order_id, LocalDate date_created, double total_price, String status, int user_id, int carport_id, int quote_id) {
        this.order_id = order_id;
        this.date_created = date_created;
        this.total_price = total_price;
        this.status = status;
        this.user_id = user_id;
        this.carport_id = carport_id;
        this.quote_id = quote_id;
    }

    public Order(LocalDate date_created, double total_price, String status, int user_id, int carport_id, int quote_id) {
        this.date_created = date_created;
        this.total_price = total_price;
        this.status = status;
        this.user_id = user_id;
        this.carport_id = carport_id;
        this.quote_id = quote_id;
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

    public int getCarport_id() {
        return carport_id;
    }

    public void setCarport_id(int carport_id) {
        this.carport_id = carport_id;
    }

    public int getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(int quote_id) {
        this.quote_id = quote_id;
    }
}