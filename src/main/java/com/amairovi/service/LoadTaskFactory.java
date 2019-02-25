package com.amairovi.service;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedFile;
import com.amairovi.model.FeedProperties;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class LoadTaskFactory {
    private static final Logger LOGGER = Logger.getLogger(LoadTaskFactory.class.getName());

    private final FeedFileService fileService;
    private final FeedLoaderService feedLoaderService;

    public LoadTaskFactory(FeedFileService fileService, FeedLoaderService feedLoaderService) {
        this.fileService = fileService;
        this.feedLoaderService = feedLoaderService;
    }

    public Runnable create(Feed feed) {
        LOGGER.log(Level.INFO, "create()");
        requireNonNull(feed);

        FeedFile feedFile = new FeedFile();
        feedFile.setFilename(feed.getName());

        return () -> {
            LOGGER.log(Level.INFO, () -> "run load task for feed=" + feed);

            Optional<SyndFeed> syndFeedOptional = feedLoaderService.load(feed);
            if (syndFeedOptional.isPresent()) {
                SyndFeed syndFeed = syndFeedOptional.get();

                updateFeed(syndFeed, feed);

                String syndFeedStr = syndFeed.toString();
                fileService.writeln(feedFile, syndFeedStr);
            }
        };
    }

    private void updateFeed(SyndFeed syndFeed, Feed feed) {
        FeedProperties feedProperties = new FeedProperties(syndFeed);
        feed.setFeedProperties(feedProperties);
    }

}
