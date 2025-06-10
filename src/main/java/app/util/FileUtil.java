package app.util;

import java.nio.file.Files;
import java.nio.file.Paths;

// Læser hele en fil (fx en PDF) som en byte-array (byte[]).
// Det bruges fx når du vil sende en PDF som respons i controller.

public class FileUtil {

    //  returnerer indholdet af en fil som en byte[]
    public static byte[] readFileBytesFromProjectRoot(String relativePath) {
        try { //Opretter en filsti med Paths.get(relativePath)
            return Files.readAllBytes(Paths.get(relativePath)); //Læser hele filen som bytes

        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + relativePath, e);
        }
    }
}
