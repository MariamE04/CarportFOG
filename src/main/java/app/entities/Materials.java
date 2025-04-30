package app.entities;

import java.util.List;

public class Materials {
    List<Materials> materials;
    int materialId;
    String name;
    String description;
    String unit;
    String amount;
    int length;
    int price;

    public Materials(List<Materials> materials, int materialId, String name, String description, String unit, String amount, int length, int price) {
        this.materials = materials;
        this.materialId = materialId;
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.amount = amount;
        this.length = length;
        this.price = price;
    }

    public Materials(int length, String amount, String unit, String description, int price, String name, List<Materials> materials) {
        this.length = length;
        this.amount = amount;
        this.unit = unit;
        this.description = description;
        this.price = price;
        this.name = name;
        this.materials = materials;
    }

    public List<Materials> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Materials> materials) {
        this.materials = materials;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
