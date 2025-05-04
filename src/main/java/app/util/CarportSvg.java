package app.util;

// bruges til at tegne en carport med SVG.
public class CarportSvg {
    //dimensionerne på carporten
    private int width;
    private int length;
    private Svg carportSvg; // selve SVG-objektet, der tegnes på

    public CarportSvg(int width, int length) {
        this.width = width;
        this.length = length;
        //Opretter et Svg-objekt med lidt ekstra plads rundt om carporten.
        carportSvg = new Svg(0, 0, "0 0 " + (width + 50) + " " + (length + 50), (width + 50) + "px"); //carportSvg = new Svg(0, 0, "0 0 " + width + " " + length, "50%");
        //Tegner en stor hvid firkant som baggrund (selve carporten).
        carportSvg.addRectangle(0, 0, length, width, "stroke-width:1px; stroke:#000000; fill: #ffffff");

        //Kalder metoder der tilføjer:
        addBeams();
        addRafters();
        addPost();
        addDimensionArrows();
    }

    //remme
    private void addBeams() {
        double beamHeight = 4.5; // pixel
        double beamWidth = width; // hele bredden af carporten
        double offsetFromTop = 35; // 35 pixel fra top og bund.

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

        //For hver i, med 55 pixel mellemrum, tilføjes et spær (vertikal firkant).
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

        //Startposition for stolperne
        int start = 50; //50 px fra venstre kant af carporten

        // Slutposition for stolperne
        int end = width - 50; //50 px fra højre kant af carporten

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

    // tilføjer mål og pile
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

    //Når objektet udskrives, returneres hele SVG-tegningen som tekst.
    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
