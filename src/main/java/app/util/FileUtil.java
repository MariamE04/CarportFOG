package app.util;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class FileUtil {

    public static byte[] readFileBytesFromProjectRoot(String relativePath) {
        try {
            return Files.readAllBytes(Paths.get(relativePath));
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + relativePath, e);
        }
    }
}
