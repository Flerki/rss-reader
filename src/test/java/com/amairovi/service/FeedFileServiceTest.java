package com.amairovi.service;

import com.amairovi.model.FeedFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

class FeedFileServiceTest {


    private static final String FILENAME = "feed_filename";

    private FeedFile feedFile;

    private FeedFileService feedFileService;

    private Consumer<String> writeToFeedFile;

    @BeforeEach
    void setup() {
        feedFile = new FeedFile();
        feedFile.setFilename(FILENAME);

        feedFileService = new FeedFileService();
        writeToFeedFile = data -> feedFileService.writeln(feedFile, data);
    }

    @AfterEach
    void cleanUp() throws IOException {
        Path path = Paths.get(FILENAME);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @RepeatedTest(2)
    void when_write_several_lines_sequentially_then_file_contain_according_lines() throws IOException {
        List<String> generatedLines = generateLines(10);

        generatedLines.forEach(writeToFeedFile);

        List<String> actual = Files.readAllLines(Paths.get(FILENAME));
        assertThat(actual, containsInAnyOrder(generatedLines.toArray()));
        assertThat(actual.size(), equalTo(generatedLines.size()));
    }


    private List<String> generateLines(int amount) {
        return new Random()
                .ints(amount)
                .mapToObj(Integer::toString)
                .collect(toList());
    }
}