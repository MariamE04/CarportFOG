package app.entities;

public class OrderDetails {
    int orderDetailId;
    int orderId;
    int quantity;
    Material material;

    public OrderDetails(int orderDetailId, int orderId, int quantity, Material material) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.material = material;
    }

    public OrderDetails(Material material, int quantity, int orderId, int orderDetailId) {
        this.material = material;
        this.quantity = quantity;
        this.orderId = orderId;
        this.orderDetailId = orderDetailId;
    }

    public OrderDetails(Material material, int quantity, int orderId) {
        this.material = material;
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial (Material material){
            this.material = material;
        }

    @Override
    public String toString () {
        return "OrderDetails{" +
                "orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                ", material=" + material +
                '}';
    }
}

