package app.util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SvgToPdfConverter {

    public void convertSvgToPdf(String svgContent, String outputPdfPath) throws IOException, TranscoderException {
        PDFTranscoder transcoder = new PDFTranscoder();

        try (ByteArrayInputStream svgInputStream = new ByteArrayInputStream(svgContent.getBytes());
             FileOutputStream pdfOutputStream = new FileOutputStream(outputPdfPath)) {
            TranscoderInput input = new TranscoderInput(svgInputStream);
            TranscoderOutput output = new TranscoderOutput(pdfOutputStream);

            transcoder.transcode(input, output);
        }
    }
}
