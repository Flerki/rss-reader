package com.amairovi.core;

import com.amairovi.core.dao.FeedDao;
import com.amairovi.core.dao.FeedPersistenceStore;
import com.amairovi.core.dto.FeedBriefInfo;
import com.amairovi.core.dto.FeedInfo;
import com.amairovi.core.model.Feed;
import com.amairovi.core.service.*;
import com.amairovi.core.service.polling.FeedLoaderService;
import com.amairovi.core.service.polling.LoadTaskFactory;
import com.amairovi.core.service.polling.ScheduleService;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class FeedServiceFacade {
    private final static long DEFAULT_POLL_PERIOD_MS = 60 * 1000;

    private final FeedDao feedDao;

    private final FeedService feedService;

    public FeedServiceFacade() {
        FeedPersistenceStore feedPersistenceStore = new FeedPersistenceStore("feed_config.yml");
        feedDao = new FeedDao(feedPersistenceStore);


        FeedLoaderService feedLoaderService = new FeedLoaderService();
        FeedFileService feedFileService = new FeedFileService();
        EntryPropertiesService entryPropertiesService = new EntryPropertiesService();
        FeedStateService feedStateService = new FeedStateService(entryPropertiesService);
        LoadTaskFactory loadTaskFactory = new LoadTaskFactory(feedFileService, feedLoaderService, feedStateService, feedDao);

        ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
        ScheduleService scheduleService = new ScheduleService(scheduledExecutorService, loadTaskFactory);

        feedService = new FeedService(feedDao, scheduleService, entryPropertiesService, feedLoaderService, feedStateService);

        feedDao.findAll()
                .stream()
                .filter(Feed::isPolled)
                .forEach(scheduleService::schedulePolling);
    }

    public int createFeed(String url) {
        return feedService.createFeed(url, DEFAULT_POLL_PERIOD_MS);
    }

    public void setPollPeriodInMs(int id, long pollPeriodInMs) {
        Feed feed = feedService.findById(id);
        feedService.setPollPeriodInMs(feed, pollPeriodInMs);
    }

    public void setFeedFilename(int id, String filename) {
        Feed feed = feedService.findById(id);
        feedService.setFeedFilename(feed, filename);
    }

    public void setFeedAmountOfElementsAtOnce(int id, int amountOfElementsAtOnce) {
        Feed feed = feedService.findById(id);
        feedService.setFeedAmountOfElementsAtOnce(feed, amountOfElementsAtOnce);
    }

    public void hideProperty(int id, String propertyName) {
        Feed feed = feedService.findById(id);
        feedService.hideProperty(feed, propertyName);
    }

    public void showProperty(int id, String propertyName) {
        Feed feed = feedService.findById(id);
        feedService.showProperty(feed, propertyName);
    }

    public void enablePoll(int id) {
        Feed feed = feedService.findById(id);
        feedService.enablePoll(feed);
    }

    public void disablePoll(int id) {
        Feed feed = feedService.findById(id);
        feedService.disablePoll(feed);
    }

    public List<FeedBriefInfo> list() {
        return feedDao.findAll()
                .stream()
                .map(FeedBriefInfo::new)
                .collect(Collectors.toList());
    }

    public void delete(int id) {
        Feed feed = feedService.findById(id);
        feedDao.delete(feed);
    }

    public void stop(){

    }

    public FeedInfo getFeedFullDescription(int feedId) {
        Feed feed = feedService.findById(feedId);
        return new FeedInfo(feed);
    }
}
