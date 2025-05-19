package app.entities;

public class Carport {
    int carportId;
    int width;
    int length;
    String roofType;
    Shed shed;
    User user;

    public Carport(int carportId, int width, int length, String roofType, Shed shed, User user) {
        this.carportId = carportId;
        this.width = width;
        this.length = length;
        this.roofType = roofType;
        this.shed = shed;
        this.user = user;
    }

    public Carport(int width, int length, String roofType, Shed shed, User user) {
        this.width = width;
        this.length = length;
        this.roofType = roofType;
        this.shed = shed;
        this.user = user;
    }

    public Carport(int width, int length, User user) {
        this.width = width;
        this.length = length;
        this.user = user;
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

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}