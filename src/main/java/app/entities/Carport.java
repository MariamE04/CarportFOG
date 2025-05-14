package app.entities;

public class Carport {
    int carportId;
    int width;
    int length;
    String roofType;
    Shed shed;

    public Carport(int carportId, int width, int length, String roofType, Shed shed) {
        this.carportId = carportId;
        this.width = width;
        this.length = length;
        this.roofType = roofType;
        this.shed = shed;
    }

    public Carport(int width, int length, String roofType, Shed shed) {
        this.width = width;
        this.length = length;
        this.roofType = roofType;
        this.shed = shed;
    }



    public Carport(int width, int length, String roofType, int carportId) {
        this.width = width;
        this.length = length;
        this.roofType = roofType;
        this.carportId = carportId;
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

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    @Override
    public String toString() {
        return "Carport{" +
                "carportId=" + carportId +
                ", width=" + width +
                ", length=" + length +
                ", roofType='" + roofType + '\'' +
                ", shed=" + shed +
                '}';
    }
}