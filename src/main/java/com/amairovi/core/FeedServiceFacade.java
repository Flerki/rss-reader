package com.amairovi.core;

import com.amairovi.core.dao.FeedDao;
import com.amairovi.core.dao.FeedPersistenceStore;
import com.amairovi.core.dto.FeedBriefInfo;
import com.amairovi.core.dto.FeedInfo;
import com.amairovi.core.exception.IncorrectRssException;
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

/**
 * <h1>The core of the app</h1>
 * It provides main functionality for any kind of interfaces (cli, gui and so on).
 * <p>
 * The idea is that {@link com.amairovi.core} package will encapsulate all logic related to feed processing,
 * loading and configuring. So anybody could use to build rich user interface.
 *
 * @author  Andrei Mairovich
 * @version 1.0
 * @since   2019-03-02
 */
public class FeedServiceFacade {
    /**
     * Defines the filename, which will be a storage for feeds' configurations.
     * @see FeedPersistenceStore
     */
    public final static String DEFAULT_CONFIG_FILENAME = "feed_config.yml";

    private final static long DEFAULT_POLL_PERIOD_MS = 60 * 1000;

    private FeedDao feedDao;

    private FeedService feedService;

    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Default constructor, which initialises executor for loading and processing feeds.
     * <p>
     * By default {@code amountOfThread = Runtime.getRuntime().availableProcessors() - 1}.
     * <p>
     * {@code -1}, because one thread is reserved by some kind of ui.
     */
    public FeedServiceFacade() {
        int amountOfAvailableProcessors = Runtime.getRuntime().availableProcessors();
        scheduledExecutorService = newScheduledThreadPool(amountOfAvailableProcessors - 1);
    }


    FeedServiceFacade(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * Actual initialization of rss-reader's core happens here.
     * Not only all required objects are instantiated and wired,
     * but also file with name {@value com.amairovi.core.FeedServiceFacade#DEFAULT_CONFIG_FILENAME} is read
     * and all presented configurations are loaded.
     */
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

    /**
     *
     * @param url - is url, which has to be point to rss-feed.
     * @return id of created feed in case of successful initialization
     *
     * @throws IncorrectRssException if feed doesn't contain entries or published date property
     */
    public int createFeed(String url) {
        return feedService.createFeed(url, DEFAULT_POLL_PERIOD_MS);
    }

    /**
     *
     * @param id - is id of the feed to update
     * @param pollPeriodInMs - is a period in ms between two sequential polls
     */
    public void setPollPeriodInMs(int id, long pollPeriodInMs) {
        Feed feed = feedService.findById(id);
        feedService.setPollPeriodInMs(feed, pollPeriodInMs);
    }

    /**
     *
     * @param id - is id of the feed to update
     * @param filename - is a name of file, where downloaded feed's entries will be stored.
     *                 Has to consistent only of filename.
     *                 Presence of folders or prohibited signs could lead to an error or undefined behaviour.
     */
    public void setFeedFilename(int id, String filename) {
        Feed feed = feedService.findById(id);
        feedService.setFeedFilename(feed, filename);
    }

    public void setFeedAmountOfElementsAtOnce(int id, int amountOfElementsAtOnce) {
        Feed feed = feedService.findById(id);
        feedService.setFeedAmountOfElementsAtOnce(feed, amountOfElementsAtOnce);
    }

    /**
     *
     * @param id - is id of the feed to update
     * @param propertyName - entry's property with property name won't be printed
     */
    public void hideProperty(int id, String propertyName) {
        Feed feed = feedService.findById(id);
        feedService.hideProperty(feed, propertyName);
    }

    /**
     *
     * @param id - is id of the feed to update
     * @param propertyName - entry's property with property name will be printed if present, otherwise, {@code -}.
     */
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
