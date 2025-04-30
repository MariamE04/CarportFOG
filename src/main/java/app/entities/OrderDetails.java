package app.entities;

import java.util.List;

public class OrderDetails {
    int orderDetailId;
    int orderId;
    int quantity;
   Materials materials;

    public OrderDetails(int orderDetailId, int orderId, int quantity, Materials materials) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.materials = materials;
    }

    public OrderDetails(Materials materials, int quantity, int orderId) {
        this.materials = materials;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Materials getMaterials() {
        return materials;
    }

    public void setMaterials(Materials materials) {
        this.materials = materials;
    }
}
