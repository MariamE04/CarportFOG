package app.entities;

public class Shed {
    int shedId;
    int width;
    int length;

    public Shed(int shedId, int width, int length) {
        this.shedId = shedId;
        this.width = width;
        this.length = length;
    }

    public Shed(int width, int length) {
        this.length = length;
        this.width = width;
    }

    public int getShedId() {
        return shedId;
    }

    public void setShedId(int shedId) {
        this.shedId = shedId;
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

    @Override
    public String toString() {
        return "Shed{" +
                "shedId=" + shedId +
                ", width=" + width +
                ", length=" + length +
                '}';
    }
}


