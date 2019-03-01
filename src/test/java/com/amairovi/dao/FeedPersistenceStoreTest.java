package com.amairovi.dao;

import com.amairovi.model.Feed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

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
    void when_load_from_empty_file_then_load_empty_list() {
        String absolutePathToFile = generateAbsolutePathToFile("empty.yml");
        FeedPersistenceStore store = new FeedPersistenceStore(absolutePathToFile);

        List<Feed> feeds = store.load();

        assertThat(feeds, empty());
    }

    @Test
    void when_load_from_file_not_empty_then_load_not_empty_list() {
        String absolutePathToFile = generateAbsolutePathToFile("config1.yml");
        FeedPersistenceStore store = new FeedPersistenceStore(absolutePathToFile);

        List<Feed> feeds = store.load();

        assertThat(feeds, containsInAnyOrder(createFeeds().toArray()));
    }

    @Test
    void when_store_empty_list_then_empty_list_is_stored() {
        feedPersistenceStore.store(emptyList());

        List<Feed> feeds = feedPersistenceStore.load();
        assertThat(feeds, empty());
    }

    @Test
    void when_store_not_empty_list_then_according_list_is_stored() {
        feedPersistenceStore.store(createFeeds());

        List<Feed> feeds = feedPersistenceStore.load();
        assertThat(feeds, containsInAnyOrder(createFeeds().toArray()));
    }

    private String generateAbsolutePathToFile(String filename) {
        URL resource = FeedPersistenceStoreTest.class.getResource(filename);
        File file = new File( resource.getFile());
        return file.getAbsolutePath();
    }

    private List<Feed> createFeeds() {
        Feed feed = new Feed();
        feed.setName("feed1");
        feed.setHref("href1");
        feed.setAmountOfElementsAtOnce(10);
        feed.setFilename("filename1");
        feed.setSurveyPeriodInMs(1);

        Feed feed2 = new Feed();
        feed2.setName("feed2");
        feed2.setHref("href2");
        feed2.setAmountOfElementsAtOnce(20);
        feed2.setFilename("filename2");
        feed2.setSurveyPeriodInMs(1);

        return Arrays.asList(feed, feed2);
    }
}