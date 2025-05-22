package app.util;

import app.entities.Material;

import java.util.List;

// bruges til at tegne en carport med SVG.
public class CarportSvg {
    //dimensionerne på carporten
    private int width;
    private int length;
    private Svg carportSvg; // selve SVG-objektet, der tegnes på
    private int postCount; // antal stolper
    private int postSpace; // mellemrum mellem stolper
    private int rafterCount; // antal spær
    private List<Material> beams; // liste med remme

    public CarportSvg(int width, int length, int postCount, int postSpace, int rafterCount, List<Material> beams) {
        this.width = width;
        this.length = length;
        this.postCount = postCount;
        this.postSpace = postSpace;
        this.rafterCount = rafterCount;
        this.beams = beams;
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
        double beamLength = beams.get(0).getLength(); // pixel
        double offsetFromTop = 35; // 35 pixel fra top og bund.
        int beamCount = beams.size(); // Antal remme
        double beamStart = 0; // hvor remmen placeres

        if (beamCount == 1){
            // Øverste rem (nær toppen af carporten)
            carportSvg.addRectangle(beamStart, offsetFromTop, beamHeight, beamLength,
                    "stroke-width:1px; stroke:#000000; fill: #ffffff");

            // Nederste rem (nær bunden af carporten)
            carportSvg.addRectangle(beamStart, length - offsetFromTop - beamHeight, beamHeight, beamLength,
                    "stroke-width:1px; stroke:#000000; fill: #ffffff");
        }

        while (beamCount > 0) {
            beamLength = 100;
            while (beamLength + postSpace <= beams.get(beams.size() - beamCount).getLength()){
                beamLength += postSpace;
            }

            if (beamStart + beamLength > width){
                beamLength = width - beamStart;
            }

            // Øverste rem (nær toppen af carporten)
            carportSvg.addRectangle(beamStart, offsetFromTop, beamHeight, beamLength,
                    "stroke-width:1px; stroke:#000000; fill: #ffffff");

            // Nederste rem (nær bunden af carporten)
            carportSvg.addRectangle(beamStart, length - offsetFromTop - beamHeight, beamHeight, beamLength,
                    "stroke-width:1px; stroke:#000000; fill: #ffffff");

            beamCount -= 2;
            beamStart += beamLength;
        }
    }

    //spær
    private void addRafters() {
        double spacing = width/(rafterCount - 1.0);
        double rafterWidth = 4.5;

        //For hver i, tilføjes et spær (vertikal firkant).
        for (double i = 0; i <= width; i += spacing) {
            carportSvg.addRectangle(i, 0, length, rafterWidth, "stroke:#000000; fill: #ffffff");
        }
    }

    //Stolper
    private void addPost() {
        int postDimension = 10; // Bredde og længde af stolper
        int postsPlaced = 0; // Antal stolper placeret indtil videre
        double x = 95; // Stolpes x-position

        // Itererer gennem antallet af stolper og placerer dem langs bredden
        while (postsPlaced < postCount) {
            // Stolper på den øverste rem (øverste linje)
            carportSvg.addRectangle(x, 35 - postDimension / 2.0, postDimension, postDimension, "stroke:#000000; fill: none;");

            // Stolper på den nederste rem (nederste linje)
            carportSvg.addRectangle(x, length - 35 - postDimension / 2.0, postDimension, postDimension, "stroke:#000000; fill: none;");
            x += postSpace;
            postsPlaced += 2;

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
