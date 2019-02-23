package com.amairovi.dao;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedExtras;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.Files.createFile;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedPersistenceStoreTest {

    private final String FILENAME = "serialised_feeds";
    private FeedPersistenceStore feedPersistenceStore = new FeedPersistenceStore(FILENAME);

    @AfterEach
    void cleanUp() throws IOException {
        Path path = Paths.get(FILENAME);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    @Test
    void when_load_empty_file_then_return_empty_list() throws IOException {
        createFile(Paths.get(FILENAME));

        List<Feed> load = feedPersistenceStore.load();

        assertTrue(load.isEmpty());
    }

    @Test
    void when_store_and_file_already_exist_then_override_it() throws IOException {
        Path file = createFile(Paths.get(FILENAME));
        List<String> lines = asList("line1", "line2", "line2");
        Files.write(file, lines);

        feedPersistenceStore.store(emptyList());
        List<Feed> actual = feedPersistenceStore.load();

        assertTrue(actual.isEmpty());
    }

    @Test
    void when_store_and_load_not_empty_file_then_return_stored_list() {
        List<Feed> feeds = createFeeds();

        feedPersistenceStore.store(feeds);

        List<Feed> actual = feedPersistenceStore.load();

        assertThat(actual, containsInAnyOrder(feeds.toArray()));
    }

    private List<Feed> createFeeds() {
        Feed feed = new Feed();
        feed.setName("feed1");
        feed.setHref("href1");

        FeedExtras feed1Extras = new FeedExtras();
        feed1Extras.setAmountOfElementsAtOnce(10);
        feed1Extras.setFilename("filename1");
        feed1Extras.setSurveyPeriod(1);

        feed.setFeedExtras(feed1Extras);

        Feed feed2 = new Feed();
        feed2.setName("feed2");
        feed2.setHref("href2");

        FeedExtras feed2Extras = new FeedExtras();
        feed2Extras.setAmountOfElementsAtOnce(20);
        feed2Extras.setFilename("filename2");
        feed2Extras.setSurveyPeriod(1);

        feed2.setFeedExtras(feed2Extras);

        return Arrays.asList(feed, feed2);
    }
}