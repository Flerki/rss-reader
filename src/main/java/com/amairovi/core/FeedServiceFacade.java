package com.amairovi.core;

import com.amairovi.core.dao.FeedDao;
import com.amairovi.core.dao.FeedPersistenceStore;
import com.amairovi.core.dto.FeedBriefInfo;
import com.amairovi.core.dto.FeedInfo;
import com.amairovi.core.model.Feed;
import com.amairovi.core.service.EntryPropertiesService;
import com.amairovi.core.service.FeedService;
import com.amairovi.core.service.FeedStateService;
import com.amairovi.core.service.polling.FeedLoaderService;
import com.amairovi.core.service.polling.LoadTaskFactory;
import com.amairovi.core.service.polling.ScheduleService;
import com.amairovi.core.service.polling.SynchronizedFileWriter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.newScheduledThreadPool;

public class FeedServiceFacade {
    public final static String DEFAULT_CONFIG_FILENAME = "feed_config.yml";

    private final static long DEFAULT_POLL_PERIOD_MS = 60 * 1000;

    private FeedDao feedDao;

    private FeedService feedService;

    private final ScheduledExecutorService scheduledExecutorService;

    public FeedServiceFacade() {
        int amountOfAvailableProcessors = Runtime.getRuntime().availableProcessors();
        scheduledExecutorService = newScheduledThreadPool(amountOfAvailableProcessors - 1);
    }


    FeedServiceFacade(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public void initialize() {
        FeedPersistenceStore feedPersistenceStore = new FeedPersistenceStore(DEFAULT_CONFIG_FILENAME);
        feedDao = new FeedDao(feedPersistenceStore);


        FeedLoaderService feedLoaderService = new FeedLoaderService();
        SynchronizedFileWriter synchronizedFileWriter = new SynchronizedFileWriter();
        EntryPropertiesService entryPropertiesService = new EntryPropertiesService();
        FeedStateService feedStateService = new FeedStateService(entryPropertiesService);
        LoadTaskFactory loadTaskFactory = new LoadTaskFactory(synchronizedFileWriter, feedLoaderService, feedStateService, feedDao);

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

    public void stop() {

    }

    public FeedInfo getFeedFullDescription(int feedId) {
        Feed feed = feedService.findById(feedId);
        return new FeedInfo(feed);
    }

    public void setFeedName(int feedId, String name) {
        Feed feed = feedService.findById(feedId);
        feed.setName(name);
        feedDao.update(feed);
    }
}
