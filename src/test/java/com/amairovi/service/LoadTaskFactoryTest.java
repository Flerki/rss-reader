package com.amairovi.service;

import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class LoadTaskFactoryTest {

    private static final String FILENAME = "feed_filename";

    private LoadTaskFactory loadTaskFactory;
    private FeedFileService feedFileService;
    private FeedLoaderService feedLoaderService;

    @BeforeEach
    void setup() {
        feedFileService = mock(FeedFileService.class);
        feedLoaderService = mock(FeedLoaderService.class);
        FeedStateService feedStateService = mock(FeedStateService.class);
        loadTaskFactory = new LoadTaskFactory(feedFileService, feedLoaderService, feedStateService);
    }

    @Test
    void when_create_task_then_success() {
        Feed feed = new Feed();
        feed.setFilename(FILENAME);

        SyndFeed syndFeed = mock(SyndFeed.class);
        String syndFeedStr = "syndFeed";

        when(syndFeed.toString()).thenReturn(syndFeedStr);
        when(feedLoaderService.load(eq(feed))).thenReturn(syndFeed);

        Runnable runnable = loadTaskFactory.create(feed);
        runnable.run();

        verify(feedLoaderService).load(eq(feed));
        verify(feedFileService).writeln(eq(feed.getFilename()), eq(syndFeedStr));
    }
}