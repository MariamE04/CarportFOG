package app.util;

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

    public void convertSvgToPdf(String svgContent, String outputPdfPath) throws IOException, TranscoderException {

        //Opretter en ny instans af PDFTranscoder, som bruges til at konvertere SVG til PDF.
        PDFTranscoder transcoder = new PDFTranscoder();

        try (ByteArrayInputStream svgInputStream = new ByteArrayInputStream(svgContent.getBytes()); //svgInputStream: laver SVG-teksten om til en byte-stream, så PDFTranscoder kan læse den.
             FileOutputStream pdfOutputStream = new FileOutputStream(outputPdfPath)) { //forbereder en fil, som PDF’en bliver skrevet til.

            //Pakker SVG-streamen ind i en TranscoderInput – formatet som transcoder kræver.
            TranscoderInput input = new TranscoderInput(svgInputStream);

            //Pakker PDF-outputstreamen ind i TranscoderOutput.
            TranscoderOutput output = new TranscoderOutput(pdfOutputStream);

            //Selve konverteringen sker her. transcode(...) læser SVG’en og skriver PDF’en.
            transcoder.transcode(input, output);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Udskriv fejlmeddelelser for at finde ud af, hvad der går galt.
        }
    }
}

