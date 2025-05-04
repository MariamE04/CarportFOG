package app.entities;

import java.util.List;

public class OrderDetails {
    int order_detail_id;
    int order_id;
    int quantity;
   Materials materials;

    public OrderDetails(int order_detail_id, int order_id, int quantity, Materials materials) {
        this.order_detail_id = order_detail_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.materials = materials;
    }

    public OrderDetails(Materials materials, int quantity, int order_id) {
        this.materials = materials;
        this.quantity = quantity;
        this.order_id = order_id;
    }

    public int getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(int order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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

    @Override
    public String toString() {
        return "OrderDetails{" +
                "order_detail_id=" + order_detail_id +
                ", order_id=" + order_id +
                ", quantity=" + quantity +
                ", materials=" + materials +
                '}';
    }
}
