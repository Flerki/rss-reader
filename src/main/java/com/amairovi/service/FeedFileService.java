package com.amairovi.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.Files.exists;

public class FeedFileService {

    private static final Logger LOGGER = Logger.getLogger(FeedFileService.class.getName());

    private Map<String, Object> filenameToMonitor = new ConcurrentHashMap<>();

    public void writeln(String filename, String data) {
        filenameToMonitor.putIfAbsent(filename, new Object());
        Object monitor = filenameToMonitor.get(filename);

        Path pathToFile = Paths.get(filename);

        try {
            writeln(pathToFile, data, monitor);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e::getMessage);
        }
    }

    private void writeln(Path pathToFile, String data, Object monitor) throws IOException {
        if (!exists(pathToFile)) {
            try {
                Files.createFile(pathToFile);
            } catch (FileAlreadyExistsException ignore) {
                LOGGER.log(Level.INFO, () -> "file=" + pathToFile.getFileName() + " already exists");
            }
        }

        synchronized (monitor) {
            try (BufferedWriter out = Files.newBufferedWriter(pathToFile, StandardOpenOption.APPEND)) {
                out.write(data);
                out.newLine();
            }
        }
    }
}
