package app.controllers;

import app.entities.Carport;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.util.Calculator;
import app.util.CarportSvg;
import app.util.SvgToPdfConverter;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;
import java.io.IOException;

public class SvgController {
    // Metode til at vise og generere en ordre med SVG samt konvertering til PDF
    public static void showOrder(Context ctx) {
        try {
            int quoteId = Integer.parseInt(ctx.pathParam("id"));

            // HENT carport fra databasen via quoteId (her er det en placeholder)
            Carport carport = CarportMapper.getCarportByQuoteId(quoteId);
            if (carport == null) {
                ctx.status(404).result("Carport ikke fundet for tilbud ID: " + quoteId);
                return;
            }

            CarportSvg carportSvg = Calculator.carportCalculator(carport.getWidth(), carport.getLength());


            ctx.attribute("id", quoteId);

            String svgContent = carportSvg.toString();

            String filename = "order_" + quoteId + "_carport_" + carport.getWidth() + "x" + carport.getLength() + ".pdf";
            String pdfFilename = "pdf/" + filename;
            SvgToPdfConverter converter = new SvgToPdfConverter();

            try {
                converter.convertSvgToPdf(svgContent, pdfFilename);
                System.out.println("PDF gemt som " + pdfFilename);
            } catch (IOException | TranscoderException e) {
                e.printStackTrace();
                ctx.status(500).result("Fejl ved konvertering til PDF.");
                return;
            }

            ctx.attribute("svg", svgContent);
            ctx.attribute("filename", filename);
            ctx.render("pay_quote.html");

        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).result("Ugyldige eller manglende parametre.");
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
