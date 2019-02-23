package com.amairovi.dao;

import com.amairovi.model.Feed;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;

public class FeedPersistenceStore {
    private static final Logger LOGGER = Logger.getLogger(FeedPersistenceStore.class.getName());

    private final Path filepath;

    public FeedPersistenceStore(String file) {
        this.filepath = Paths.get(file);
    }

    @SuppressWarnings("unchecked")
    public List<Feed> load() {
        List<Feed> result;
        try (InputStream input = newInputStream(filepath)) {
            ObjectInputStream in = new ObjectInputStream(input);
            result = (List<Feed>) in.readObject();
        } catch (IOException e) {
            String message = "Exception occurred during load of the file with feeds. " +
                    "Highly likely error related to inconsistent state of file or its absence. " +
                    "So file with feeds is ignored. " +
                    "Message: " + e.getMessage();
            LOGGER.log(Level.SEVERE, message);
            return new ArrayList<>();
        } catch (ClassNotFoundException c) {
            LOGGER.log(Level.SEVERE, "Feed class not found");
            throw new RuntimeException(c);
        }
        return result;
    }

    public void store(List<Feed> feeds) {
        try (OutputStream outStream = newOutputStream(filepath, StandardOpenOption.CREATE)) {
            ObjectOutputStream out = new ObjectOutputStream(outStream);
            out.writeObject(feeds);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
