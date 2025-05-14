package app.controllers;

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
            int width = Integer.parseInt(ctx.queryParam("width"));
            int length = Integer.parseInt(ctx.queryParam("length"));

            ctx.attribute("quoteId", quoteId);

            CarportSvg svg = new CarportSvg(width, length);
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
