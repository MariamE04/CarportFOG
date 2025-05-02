package app.util;

public class CarportSvg {
    private int width;
    private int length;
    private Svg carportSvg;

    public CarportSvg(int width, int length) {
        this.width = width;
        this.length = length;
        carportSvg = new Svg(0, 0, "0 0 " + width + " " + length, "50%");
        carportSvg.addRectangle(0, 0, length, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPost();
    }

    //remme
    private void addBeams() {
        double beamHeight = 4.5; // Tynd højde (lodret tykkelse af remmen)
        double offsetFromEdge = 35; // Afstand fra top og bund

        // Øverste rem
        carportSvg.addRectangle(0, offsetFromEdge, width, beamHeight,
                "stroke-width:1px; stroke:#000000; fill: #ffffff");

        // Nederste rem
        carportSvg.addRectangle(0, length - offsetFromEdge - beamHeight, width, beamHeight,
                "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }


    //spær
    private void addRafters() {
        double spacing = 55.0;
        double rafterWidth = 4.5;

        for (double i = 0; i < width; i += spacing) {
            carportSvg.addRectangle(i, 0, length, rafterWidth, "stroke:#000000; fill: #ffffff");
        }
    }


    private void addPost(){

    }

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
