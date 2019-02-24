package com.amairovi.service;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedFile;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoadTaskFactoryTest {

    private LoadTaskFactory loadTaskFactory;
    private FeedFileService feedFileService;
    private FeedLoaderService feedLoaderService;

    @BeforeEach
    void setup() {
        feedFileService = mock(FeedFileService.class);
        feedLoaderService = mock(FeedLoaderService.class);

        loadTaskFactory = new LoadTaskFactory(feedFileService, feedLoaderService);
    }

    @Test
    void when_create_task_then_success() {
        Feed feed = new Feed();
        SyndFeed syndFeed = mock(SyndFeed.class);
        String syndFeedStr = "syndFeed";

        when(syndFeed.toString()).thenReturn(syndFeedStr);
        when(feedLoaderService.load(eq(feed))).thenReturn(of(syndFeed));

        Runnable runnable = loadTaskFactory.create(feed);
        runnable.run();

        verify(feedLoaderService).load(eq(feed));
        verify(feedFileService).writeln(any(FeedFile.class), eq(syndFeedStr));
    }

    @Test
    void when_feed_is_nullable_then_do_nothing() {
        Feed feed = new Feed();

        when(feedLoaderService.load(eq(feed))).thenReturn(empty());

        Runnable runnable = loadTaskFactory.create(feed);
        runnable.run();

        verify(feedLoaderService).load(eq(feed));
        verifyZeroInteractions(feedFileService);
    }
}