package com.amairovi.service;

import com.amairovi.dao.FeedDao;
import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class LoadTaskFactory {
    private static final Logger LOGGER = Logger.getLogger(LoadTaskFactory.class.getName());

    private final FeedFileService fileService;
    private final FeedLoaderService feedLoaderService;
    private final FeedStateService feedStateService;
    private final FeedDao feedDao;

    public LoadTaskFactory(FeedFileService fileService,
                           FeedLoaderService feedLoaderService,
                           FeedStateService feedStateService,
                           FeedDao feedDao) {
        this.fileService = fileService;
        this.feedLoaderService = feedLoaderService;
        this.feedStateService = feedStateService;
        this.feedDao = feedDao;
    }

    public Runnable create(Feed feed) {
        LOGGER.log(Level.INFO, "create()");
        requireNonNull(feed);

        return () -> {
            try {
                LOGGER.log(Level.INFO, () -> "run load task for feed=" + feed);

                SyndFeed syndFeed = feedLoaderService.load(feed);
                feedStateService.update(syndFeed, feed);

                FeedFormatter feedFormatter = new FeedFormatter(feed);
                String syndFeedStr = feedFormatter.format(syndFeed);
                fileService.writeln(feed.getFilename(), syndFeedStr);

                feed.setLastPollAtMs(Instant.now().toEpochMilli());
                feedDao.update(feed);
            }catch (Exception e){
                LOGGER.log(Level.WARNING, e::getMessage);
            }
        };
    }
}
