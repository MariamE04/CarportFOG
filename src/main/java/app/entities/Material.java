package app.entities;

public class Material {
    int materialId;
    String name;
    String description;
    String unit;
    int amount;
    int length;
    double price;

    public Material(int materialId, String name, String description, String unit, int amount, int length, double price) {
        this.materialId = materialId;
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.amount = amount;
        this.length = length;
        this.price = price;
    }

    public Material(int length, int amount, String unit, String description, double price, String name) {
        this.length = length;
        this.amount = amount;
        this.unit = unit;
        this.description = description;
        this.price = price;
        this.name = name;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Materials{" +
                "materialId=" + materialId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", amount=" + amount +
                ", length=" + length +
                ", price=" + price +
                '}';
    }
}
