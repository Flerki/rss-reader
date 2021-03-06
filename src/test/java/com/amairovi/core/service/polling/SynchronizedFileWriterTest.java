package com.amairovi.core.service.polling;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.amairovi.utility.FileUtils.removeFileIfPresent;
import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

class SynchronizedFileWriterTest {

    private static final String FILENAME = "feed_filename";

    private SynchronizedFileWriter synchronizedFileWriter;

    private Consumer<String> writeToFeedFile;

    @BeforeEach
    void setup() {
        synchronizedFileWriter = new SynchronizedFileWriter();
        writeToFeedFile = data -> synchronizedFileWriter.writeln(FILENAME, data);
        removeFileIfPresent(FILENAME);
    }

    @AfterEach
    void cleanUp() {
        removeFileIfPresent(FILENAME);
    }

    @RepeatedTest(2)
    void when_write_several_lines_sequentially_then_file_contain_according_lines() throws IOException {
        List<String> generatedLines = generateLines(10, 10);

        generatedLines.forEach(line -> writeToFeedFile.accept(line + lineSeparator()));

        List<String> actual = Files.readAllLines(Paths.get(FILENAME));
        assertThat(actual, equalTo(generatedLines));
    }


    @RepeatedTest(3)
    void when_write_several_lines_in_parallel_then_file_contain_according_lines_not_necessary_in_right_order() throws IOException {
        List<String> generatedLines = generateLines(100, 10000);

        generatedLines.stream()
                .parallel()
                .forEach(line -> writeToFeedFile.accept(line + lineSeparator()));

        List<String> actual = Files.readAllLines(Paths.get(FILENAME));
        assertThat(actual, containsInAnyOrder(generatedLines.toArray()));
        assertThat(actual.size(), equalTo(generatedLines.size()));
    }

    private List<String> generateLines(int amount, int minSize) {
        return new Random()
                .ints(amount)
                .mapToObj(Integer::toString)
                .map(str -> {
                    int amountOfIterationToGetEqOrGtMinSize = (int) Math.ceil(((double) minSize / str.length()));
                    return IntStream.range(0, amountOfIterationToGetEqOrGtMinSize)
                            .mapToObj(i -> str)
                            .collect(Collectors.joining());
                })
                .collect(toList());
    }
}