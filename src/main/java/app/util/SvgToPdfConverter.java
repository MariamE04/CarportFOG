package app.util;

// konverterer SVG-indhold til PDF ved at bruge en ekstern biblioteksfunktion.

import org.apache.batik.transcoder.TranscoderException; //bruges til at håndtere fejl under konverteringen.

// bruges til input/output af konverteringen.
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;

// er selve motoren der konverterer SVG til PDF.
import org.apache.fop.svg.PDFTranscoder;

import java.io.ByteArrayInputStream; // Læser data fra en byte-array (som vores SVG-indhold).
import java.io.FileOutputStream; //Skriver til en fil – her PDF’en.
import java.io.IOException; //Bruges til at håndtere fejl i filhåndtering.

public class SvgToPdfConverter {

    // svgContent: inholder svg-koden, outputPdfPath: stien til hvor PDF-filen skal gemmes.
    public void convertSvgToPdf(String svgContent, String outputPdfPath) throws IOException, TranscoderException {

        //Opretter en ny instans af PDFTranscoder, motoren bag konverteringen.
        PDFTranscoder transcoder = new PDFTranscoder();

        //svgInputStream: omdanne SVG-teksten til en byte-stream, så PDFTranscoder kan læse den.
        try (ByteArrayInputStream svgInputStream = new ByteArrayInputStream(svgContent.getBytes());
             // forbereder en fil, for at gemme den genererede PDF.
             FileOutputStream pdfOutputStream = new FileOutputStream(outputPdfPath)) {

            // pakker inputtet (SVG-streamen) ind i et TranscoderInput-objekt.
            TranscoderInput input = new TranscoderInput(svgInputStream);

            //Pakker outputtet (PDF-filstreamen) ind i et TranscoderOutput-objekt.
            TranscoderOutput output = new TranscoderOutput(pdfOutputStream);

            //Selve konverteringen sker her. læser SVG’en fra input og skriver en færdig PDF til output.
            transcoder.transcode(input, output);

        } catch (RuntimeException e) {
            e.printStackTrace(); // Udskriv fejlmeddelelser for at finde ud af, hvad der går galt.
        }
    }
}

