package app.util;

public class CarportSvg {
    private int width;
    private int length;
    private Svg carportSvg;

    public CarportSvg(int width, int length) {
        this.width = width;
        this.length = length;
        //carportSvg = new Svg(0, 0, "0 0 " + width + " " + length, "50%");
        carportSvg = new Svg(0, 0, "0 0 " + (width + 50) + " " + (length + 50), (width + 50) + "px");
        carportSvg.addRectangle(0, 0, length, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addPost();
        addDimensionArrows();
    }

    //remme
    private void addBeams() {
        double beamHeight = 4.5;
        double beamWidth = width; // hele bredden af carporten
        double offsetFromTop = 35;

        // Øverste rem (nær toppen af carporten)
        carportSvg.addRectangle(0, offsetFromTop, beamHeight, beamWidth,
                "stroke-width:1px; stroke:#000000; fill: #ffffff");

        // Nederste rem (nær bunden af carporten)
        carportSvg.addRectangle(0, length - offsetFromTop - beamHeight, beamHeight, beamWidth,
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

    //Stolper
    private void addPost() {
        int stolpeBredde = 10; // Bredde af stolper
        int stolpeHøjde = 10;   //højden af stolperne
        int afstandMellemStolper = 200; // Afstand mellem stolper

        // Beregner antallet af stolper baseret på bredden af carporten og afstanden mellem stolperne
        // Antallet af stolper kan ikke være mindre end 2
        int antalStolper = Math.max(2, (int) Math.ceil(width / (double) afstandMellemStolper));

        //Startposition for stolperne (afstanden fra venstre kant)
        int start = 50; //30 px fra venstre kant af carporten

        // Slutposition for stolperne (afstanden fra højre kant)
        int end = width - 50; //30 px fra højre kant af carporten

        // Beregner den præcise afstand mellem stolperne langs bredden
        int afstandMellem = (end - start) / (antalStolper - 1);

        // Itererer gennem antallet af stolper og placerer dem langs bredden
        for (int i = 0; i < antalStolper; i++) {
            int x = start + i * afstandMellem;  //Beregner x-positionen for hver stolpe, så de bliver jævnt fordelt langs bredden

            // Stolper på den øverste rem (øverste linje)
            carportSvg.addRectangle(x, 35 - stolpeHøjde / 2.0, stolpeBredde, stolpeHøjde, "stroke:#000000; fill: none;");

            // Stolper på den nederste rem (nederste linje)
            carportSvg.addRectangle(x, length - 35 - stolpeHøjde / 2.0, stolpeBredde, stolpeHøjde, "stroke:#000000; fill: none;");

        }
    }

    private void addDimensionArrows() {
        int textOffset = 20;

        // Lodret pil til længden (placeret til højre for carporten)
        int arrowX = width + 20;  // lidt udenfor carporten
        carportSvg.addArrow(arrowX, 0, arrowX, length, "stroke:#000000; stroke-width:1px");
        carportSvg.addText(arrowX + textOffset, length / 2, -90, length + " cm");

        // Vandret pil til bredden (placeret under carporten)
        int arrowY = length + 20;  // lidt under carporten
        carportSvg.addArrow(0, arrowY, width, arrowY, "stroke:#000000; stroke-width:1px");
        carportSvg.addText(width / 2, arrowY + textOffset, 0, width + " cm");
    }


    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
