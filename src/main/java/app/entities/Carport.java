package app.entities;

public class Carport {
int carportId;
int width;
int length;
int height;
String roofType;
int shedId;

    public Carport(int carportId, int width, int length, int height, String roofType, int shedId) {
        this.carportId = carportId;
        this.width = width;
        this.length = length;
        this.height = height;
        this.roofType = roofType;
        this.shedId = shedId;
    }

    public Carport(int width, int length, int height, String roofType, int shedId) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.roofType = roofType;
        this.shedId = shedId;
    }

    public Carport(int width, int length, int height, String roofType) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.roofType = roofType;
    }

    public int getCarportId() {
        return carportId;
    }

    public void setCarportId(int carportId) {
        this.carportId = carportId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getRoofType() {
        return roofType;
    }

    public void setRoofType(String roofType) {
        this.roofType = roofType;
    }

    public int getShedId() {
        return shedId;
    }

    public void setShedId(int shedId) {
        this.shedId = shedId;
    }
}
