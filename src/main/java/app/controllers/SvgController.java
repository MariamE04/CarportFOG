package app.controllers;

import app.util.CarportSvg;
import app.util.SvgToPdfConverter;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SvgController {
    // Metode til at vise og generere en ordre med SVG samt konvertering til PDF
    public static void showOrder(Context ctx) {
        int width = Integer.parseInt(ctx.queryParam("width"));
        int length = Integer.parseInt(ctx.queryParam("length"));

        // Genererer SVG baseret på bredden og længden
        CarportSvg svg = new CarportSvg(width, length);
        String svgContent = svg.toString();

        // Gemmer SVG'en i en fil
        String svgFilename = "carport_" + width + "x" + length + ".svg";
        saveSvgToFile(svgContent, svgFilename);

        // Konverterer SVG'en til PDF
        String pdfFilename = "carport_" + width + "x" + length + ".pdf";
        SvgToPdfConverter converter = new SvgToPdfConverter();

        try {
            // Konverterer SVG til PDF og gemmer filen
            converter.convertSvgToPdf(svgContent, pdfFilename);
            System.out.println("PDF gemt som " + pdfFilename);
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        // Returner SVG'en som en attribute til visning
        ctx.attribute("svg", svgContent);
        ctx.render("showOrder.html");
    }

    // Metode til at gemme SVG til en fil
    public static void saveSvgToFile(String svgContent, String filename) {
        try {
            // Opretter en ny fil
            File file = new File(filename);

            // Skriver SVG-indholdet til filen
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(svgContent);
            }

            System.out.println("SVG-fil gemt som " + filename);
        } catch (IOException e) {
            e.printStackTrace(); // Hvis der er en fejl, printes den
        }
    }

}
