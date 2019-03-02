package com.amairovi.utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.amairovi.core.FeedServiceFacade.DEFAULT_CONFIG_FILENAME;

public class FileUtils {
    public static void removeFileIfPresent(String filename) {
        Path path = Paths.get(filename);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeConfigsIfPresent() {
        Path path = Paths.get(DEFAULT_CONFIG_FILENAME);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> readLines(String filename, Class<?> clazz) throws IOException {
        URL resource = clazz.getResource(filename);
        File file = new File(resource.getFile());
        Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllLines(path);
    }
}
