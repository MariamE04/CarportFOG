package app.entities;

public class Carport {
int carportId;
int width;
int length;
String roofType;
int shedId;

    public Carport(int carportId, int width, int length, String roofType, int shedId) {
        this.carportId = carportId;
        this.width = width;
        this.length = length;
        this.roofType = roofType;
        this.shedId = shedId;
    }

    public Carport(int width, int length, String roofType, int shedId) {
        this.width = width;
        this.length = length;

        this.roofType = roofType;
        this.shedId = shedId;
    }

    public Carport(int width, int length, String roofType) {
        this.width = width;
        this.length = length;
        this.roofType = roofType;
    }

    public Carport(int width, int length) {
        this.width = width;
        this.length = length;
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
