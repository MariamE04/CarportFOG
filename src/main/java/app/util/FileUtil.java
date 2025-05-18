package app.util;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static byte[] readFileBytesFromProjectRoot(String relativePath) {

        try { //Opretter en filsti med Paths.get(relativePath)
            return Files.readAllBytes(Paths.get(relativePath)); //Læser hele filen som bytes med Files.readAllBytes(...)

        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + relativePath, e);
        }
    }
}
