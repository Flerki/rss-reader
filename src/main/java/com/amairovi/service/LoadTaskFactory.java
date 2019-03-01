package com.amairovi.service;

import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class LoadTaskFactory {
    private static final Logger LOGGER = Logger.getLogger(LoadTaskFactory.class.getName());

    private final FeedFileService fileService;
    private final FeedLoaderService feedLoaderService;
    private final FeedStateService feedStateService;


    public LoadTaskFactory(FeedFileService fileService,
                           FeedLoaderService feedLoaderService,
                           FeedStateService feedStateService) {
        this.fileService = fileService;
        this.feedLoaderService = feedLoaderService;
        this.feedStateService = feedStateService;
    }

    public Runnable create(Feed feed) {
        LOGGER.log(Level.INFO, "create()");
        requireNonNull(feed);

        return () -> {
            LOGGER.log(Level.INFO, () -> "run load task for feed=" + feed);

            SyndFeed syndFeed = feedLoaderService.load(feed);
            feedStateService.update(syndFeed, feed);

            String syndFeedStr = new FeedFormatter(feed).format(syndFeed);
            fileService.writeln(feed.getFilename(), syndFeedStr);
        };
    }
}
