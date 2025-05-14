package app.controllers;

import app.entities.Carport;
import app.entities.Material;
import app.persistence.CarportMapper;
import app.util.CarportSvg;
import app.util.SvgToPdfConverter;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;
import java.io.IOException;
import java.util.List;

public class SvgController {
    // Metode til at vise og generere en ordre med SVG samt konvertering til PDF
    public static void showOrder(Context ctx) {
        try {
            int quoteId = Integer.parseInt(ctx.pathParam("id"));

            // HENT carport fra databasen via quoteId (her er det en placeholder – lav denne metode i din DAO)
            Carport carport = CarportMapper.getCarportByQuoteId(quoteId);
            if (carport == null) {
                ctx.status(404).result("Carport ikke fundet for tilbud ID: " + quoteId);
                return;
            }

            int width = carport.getWidth();
            int length = carport.getLength();
            int postCount = carport.getPostCount();
            int postSpace = carport.getPostSpacing();
            int rafterCount = carport.getRafterCount();
            List<Material> beams = carport.getBeams();

            ctx.attribute("id", quoteId);

            // Brug den udvidede constructor
            CarportSvg svg = new CarportSvg(width, length, postCount, postSpace, rafterCount, beams);
            String svgContent = svg.toString();

            String pdfFilename = "public/pdf/quote_" + quoteId + "_carport_" + width + "x" + length + ".pdf";
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
            ctx.attribute("pdfFilename", pdfFilename.replace("public/", ""));
            ctx.render("pay_quote.html");

        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).result("Ugyldige eller manglende parametre.");
        }
    }
}
