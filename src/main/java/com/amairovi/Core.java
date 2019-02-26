package com.amairovi;

import com.amairovi.dao.FeedDao;
import com.amairovi.dao.FeedPersistenceStore;
import com.amairovi.model.Feed;
import com.amairovi.service.*;

import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class Core {
    private final FeedDao feedDao;
    private final FeedPersistenceStore feedPersistenceStore;

    private final FeedLoaderService feedLoaderService;
    private final FeedFileService feedFileService;
    private final ScheduleService scheduleService;
    private final FeedService feedService;
    private final FeedFormatter feedFormatter;

    public Core() {
        feedPersistenceStore = new FeedPersistenceStore("data");
        feedDao = new FeedDao(feedPersistenceStore);

        feedLoaderService = new FeedLoaderService();
        feedFileService = new FeedFileService();
        feedFormatter = new FeedFormatter();
        LoadTaskFactory loadTaskFactory = new LoadTaskFactory(feedFileService, feedLoaderService, feedFormatter);

        ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
        scheduleService = new ScheduleService(scheduledExecutorService);

        feedService = new FeedService(feedDao, scheduleService, loadTaskFactory);
    }

    public void createFeed(String url, long pollPeriod) {
        feedService.createFeed(url, pollPeriod);
    }

    public void setFeedSurveyPeriod(Feed feed, long pollPeriod) {
        feedService.setFeedSurveyPeriod(feed, pollPeriod);
    }

    public void setFeedFilename(Feed feed, String filename) {
        feedService.setFeedFilename(feed, filename);
    }

    public void setFeedAmountOfElementsAtOnce(Feed feed, int amountOfElementsAtOnce) {
        feedService.setFeedAmountOfElementsAtOnce(feed, amountOfElementsAtOnce);
    }

    public void hideProperty(int id, String propertyName) {
        Feed feed = feedService.findById(id);
        feedService.hideProperty(feed, propertyName);
    }

    public void showProperty(int id,  String propertyName) {
        Feed feed = feedService.findById(id);
        feedService.showProperty(feed, propertyName);
    }

    public void disablePoll(Feed feed) {
        feedService.disablePoll(feed);
    }
}
