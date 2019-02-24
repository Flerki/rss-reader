package com.amairovi.service;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedFile;
import com.rometools.rome.feed.synd.SyndFeed;

import static java.util.Objects.requireNonNull;

public class LoadTaskFactory {

    private final FeedFileService fileService;
    private final FeedLoaderService feedLoaderService;

    public LoadTaskFactory(FeedFileService fileService, FeedLoaderService feedLoaderService) {
        this.fileService = fileService;
        this.feedLoaderService = feedLoaderService;
    }

    public Runnable create(Feed feed) {
        requireNonNull(feed);

        FeedFile feedFile = new FeedFile();
        feedFile.setFilename(feed.getName());

        return () -> feedLoaderService.load(feed)
                .map(SyndFeed::toString)
                .ifPresent(stringFeed -> fileService.writeln(feedFile, stringFeed));
    }

}
