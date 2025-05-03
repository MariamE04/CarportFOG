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

    private void addPost() {
        int stolpeBredde = 10; // Bredde af stolper
        int stolpeHøjde = 10;   //højden af stolperne
        int afstandMellemStolper = 240; // Afstand mellem stolper

        // Beregner antallet af stolper baseret på bredden af carporten og afstanden mellem stolperne
        // Antallet af stolper kan ikke være mindre end 2
        int antalStolper = Math.max(2, (int) Math.ceil(width / (double) afstandMellemStolper));

        //Startposition for stolperne (afstanden fra venstre kant)
        int start = 30; //30 px fra venstre kant af carporten

        // Slutposition for stolperne (afstanden fra højre kant)
        int end = width - 30; //30 px fra højre kant af carporten

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

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
