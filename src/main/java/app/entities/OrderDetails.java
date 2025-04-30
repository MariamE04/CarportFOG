package app.entities;

import java.util.List;

public class OrderDetails {
    List<Carport> carportList;
    List<Materials> materialsList;
    int orderDetailId;
    int orderId;
    int quantity;
    int materialId;

    public OrderDetails(List<Carport> carportList, List<Materials> materialsList, int orderDetailId, int orderId, int quantity, int materialId) {
        this.carportList = carportList;
        this.materialsList = materialsList;
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.materialId = materialId;
    }

    public OrderDetails(int materialId, int quantity, int orderId, List<Materials> materialsList, List<Carport> carportList) {
        this.materialId = materialId;
        this.quantity = quantity;
        this.orderId = orderId;
        this.materialsList = materialsList;
        this.carportList = carportList;
    }

    public List<Carport> getCarportList() {
        return carportList;
    }

    public void setCarportList(List<Carport> carportList) {
        this.carportList = carportList;
    }

    public List<Materials> getMaterialsList() {
        return materialsList;
    }

    public void setMaterialsList(List<Materials> materialsList) {
        this.materialsList = materialsList;
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

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }
}
