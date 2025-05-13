package app.entities;

public class OrderDetails {
    int orderDetailId;
    int orderId;
    int quantity;
   Material materials;

    public OrderDetails(int orderDetailId, int orderId, int quantity, Material materials) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.materials = materials;
    }

    public OrderDetails(Material materials, int quantity, int orderId) {
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

    public Material getMaterials() {
        return materials;
    }

    public void setMaterials(Material materials) {
        this.materials = materials;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "order_detail_id=" + orderDetailId +
                ", order_id=" + orderId +
                ", quantity=" + quantity +
                ", materials=" + materials +
                '}';
    }
}
