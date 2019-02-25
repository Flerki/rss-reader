package com.amairovi.service;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedFile;
import com.rometools.rome.feed.synd.SyndFeed;

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

            feedLoaderService.load(feed)
                    .map(SyndFeed::toString)
                    .ifPresent(stringFeed -> fileService.writeln(feedFile, stringFeed));
        };
    }

}
