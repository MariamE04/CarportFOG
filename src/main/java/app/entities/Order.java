package app.entities;

import java.time.LocalDate;
import java.util.List;

public class Order {
    int orderId;
    LocalDate localDate;
    int price;
    boolean paymentStatus;
    int userId;
    int carportId;
    int quoteId;

    public Order(int orderId, LocalDate localDate, int price, boolean paymentStatus, int userId, int carportId, int quoteId) {
        this.orderId = orderId;
        this.localDate = localDate;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.userId = userId;
        this.carportId = carportId;
        this.quoteId = quoteId;
    }

    public Order(LocalDate localDate, int price, boolean paymentStatus, int userId, int carportId, int quoteId) {
        this.localDate = localDate;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.userId = userId;
        this.carportId = carportId;
        this.quoteId = quoteId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }
}