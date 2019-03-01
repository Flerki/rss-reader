package com.amairovi;

import com.amairovi.dao.FeedDao;
import com.amairovi.dao.FeedPersistenceStore;
import com.amairovi.dto.FeedBriefInfo;
import com.amairovi.dto.FeedInfo;
import com.amairovi.model.Feed;
import com.amairovi.service.*;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class Core {
    private final static long DEFAULT_POLL_PERIOD_MS = 60 * 1000;

    private final FeedDao feedDao;
    private final FeedPersistenceStore feedPersistenceStore;

    private final FeedLoaderService feedLoaderService;
    private final FeedFileService feedFileService;
    private final ScheduleService scheduleService;
    private final FeedService feedService;
    private final EntryPropertiesService entryPropertiesService;

    public Core() {
        feedPersistenceStore = new FeedPersistenceStore("feed_config.yml");
        feedDao = new FeedDao(feedPersistenceStore);

        feedLoaderService = new FeedLoaderService();
        feedFileService = new FeedFileService();
        entryPropertiesService = new EntryPropertiesService();
        FeedStateService feedStateService = new FeedStateService(entryPropertiesService);
        LoadTaskFactory loadTaskFactory = new LoadTaskFactory(feedFileService, feedLoaderService, feedStateService);

        ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
        scheduleService = new ScheduleService(scheduledExecutorService);

        feedService = new FeedService(feedDao, scheduleService, loadTaskFactory, entryPropertiesService, feedLoaderService, feedStateService);
    }

    public int createFeed(String url) {
        return feedService.createFeed(url);
    }

    public void setFeedSurveyPeriod(int id, long pollPeriod) {
        Feed feed = feedService.findById(id);
        feedService.setFeedSurveyPeriod(feed, pollPeriod);
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
        throw new RuntimeException("Not implemented");

    }

    public void stop(){

    }

    public String describe(int id) {
        throw new RuntimeException("Not implemented");
    }

    public void redirectFeedTo(int id, String filename) {
        throw new RuntimeException("Not implemented");

    }

    public FeedInfo getFeedFullDescription(int feedId) {
        Feed feed = feedService.findById(feedId);
        return new FeedInfo(feed);
    }
}
