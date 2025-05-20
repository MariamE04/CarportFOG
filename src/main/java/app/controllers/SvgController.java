package app.controllers;

import app.entities.Carport;
import app.entities.Quote;
import app.exceptions.DatabaseException;
import app.persistence.CarportMapper;
import app.persistence.QuoteMapper;
import app.util.Calculator;
import app.util.CarportSvg;
import app.util.SvgToPdfConverter;
import io.javalin.http.Context;
import org.apache.batik.transcoder.TranscoderException;
import java.io.IOException;

public class SvgController {
    // Metode til at vise og generere en ordre med SVG samt konvertering til PDF
    public static void showOrder(Context ctx) {

        try { // Hent ID fra URL’en og lav det om til et heltal (fx "123" bliver til 123).
            int quoteId = Integer.parseInt(ctx.pathParam("id"));

            // HENT QUOTE og tjek om det er accepteret
            Quote quote = QuoteMapper.getQuoteById(quoteId);
            if (!quote.isAccepted()) {
                ctx.attribute("message", "Du skal betale for at få adgang til carport og PDF.");
                ctx.render("quote_not_accepted.html");
                return;
            }


            // HENT carport fra databasen via quoteId (her er det en placeholder)
            Carport carport = CarportMapper.getCarportByQuoteId(quoteId);
            if (carport == null) {
                ctx.status(404).result("Carport ikke fundet for tilbud ID: " + quoteId);
                return;
            }

            //Beregner og genererer en SVG-tegning ud fra bredden og længden. Det returnerer et objekt CarportSvg
            CarportSvg carportSvg = Calculator.svgCalculator(carport.getWidth(), carport.getLength());

            //Gemmer quoteId i ctx, så det kan bruges i din HTML-skabelon.
            ctx.attribute("id", quoteId);

            //Konverterer SVG-objektet til en streng (tekst), som indeholder hele SVG-koden.
            String svgContent = carportSvg.toString();

            //Laver et filnavn til PDF’en ud fra tilbuds-id og carportens mål
            String filename = "order_" + quoteId + "_carport_" + carport.getWidth() + "x" + carport.getLength() + ".pdf";

            //Tilføjer pdf/ foran, så PDF’en gemmes i den mappe.
            String pdfFilename = "pdf/" + filename;

            //Opretter en konverter, som kan lave SVG om til PDF.
            SvgToPdfConverter converter = new SvgToPdfConverter();

            try {
                //Konverterer SVG-tekst til PDF og gemmer den i pdf/-mappen.
                converter.convertSvgToPdf(svgContent, pdfFilename);
                System.out.println("PDF gemt som " + pdfFilename);

            } catch (IOException | TranscoderException e) {
                e.printStackTrace();
                ctx.status(500).result("Fejl ved konvertering til PDF.");
                return;
            }

            //Gemmer SVG-indholdet og filnavnet, så HTML’en kan bruge dem.
            ctx.attribute("svg", svgContent);
            ctx.attribute("filename", filename);

            //Viser HTML-siden pay_quote.html, hvor SVG og PDF-link måske vises.
            ctx.render("pay_quote.html");

            //Hvis ID’et mangler eller ikke er et tal, sendes fejl tilbage.
        } catch (NumberFormatException | NullPointerException e) {
            ctx.status(400).result("Ugyldige eller manglende parametre.");

        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}