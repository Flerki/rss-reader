package com.amairovi.core.service;

import com.amairovi.core.dao.FeedDao;
import com.amairovi.core.model.Feed;
import com.amairovi.core.service.polling.FeedLoaderService;
import com.amairovi.core.service.polling.LoadTaskFactory;
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
    private FeedStateService feedStateService;

    @BeforeEach
    void setup() {
        feedFileService = mock(FeedFileService.class);
        feedLoaderService = mock(FeedLoaderService.class);
        feedStateService = mock(FeedStateService.class);
        FeedDao feedDao = mock(FeedDao.class);
        loadTaskFactory = new LoadTaskFactory(feedFileService, feedLoaderService, feedStateService, feedDao);
    }

    @Test
    void when_create_task_then_success() {
        Feed feed = new Feed();
        feed.setFilename(FILENAME);

        SyndFeed syndFeed = mock(SyndFeed.class);

        when(feedLoaderService.load(eq(feed))).thenReturn(syndFeed);

        Runnable runnable = loadTaskFactory.create(feed);
        runnable.run();

        verify(feedLoaderService).load(eq(feed));
        verify(feedStateService).update(eq(syndFeed), eq(feed));

        verify(feedFileService).writeln(eq(feed.getFilename()), any());
    }
}