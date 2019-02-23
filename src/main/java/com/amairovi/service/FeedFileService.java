package com.amairovi.service;

import com.amairovi.model.FeedFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.Files.exists;

public class FeedFileService {

    private static final Logger LOGGER = Logger.getLogger(FeedFileService.class.getName());

    public void writeln(FeedFile feedFile, String data) {
        LOGGER.log(Level.INFO, () -> "writeln: feedFile=" + feedFile + " data=" + data);
        String filename = feedFile.getFilename();
        Path pathToFile = Paths.get(filename);

        try {
            writeln(pathToFile, data);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e::getMessage);
        }
    }

    public void writeln(Path pathToFile, String data) throws IOException {
        if (!exists(pathToFile)) {
            try {
                Files.createFile(pathToFile);
            } catch (FileAlreadyExistsException ignore) {
                LOGGER.log(Level.INFO, () -> "file=" + pathToFile.getFileName() + " already exists");
            }
        }

        try (BufferedWriter out = Files.newBufferedWriter(pathToFile, StandardOpenOption.APPEND)) {
            out.write(data);
            out.newLine();
        }
    }
}
