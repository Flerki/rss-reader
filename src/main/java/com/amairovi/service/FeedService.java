package com.amairovi.service;

import com.amairovi.dao.FeedDao;
import com.amairovi.exception.IncorrectRssException;
import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class FeedService {
    private final FeedDao feedDao;
    private final ScheduleService scheduleService;
    private final LoadTaskFactory loadTaskFactory;
    private final EntryPropertiesService entryPropertiesService;
    private final FeedLoaderService feedLoaderService;
    private final FeedStateService feedStateService;

    public FeedService(FeedDao feedDao,
                       ScheduleService scheduleService,
                       LoadTaskFactory loadTaskFactory,
                       EntryPropertiesService entryPropertiesService,
                       FeedLoaderService feedLoaderService,
                       FeedStateService feedStateService) {
        this.feedDao = feedDao;
        this.scheduleService = scheduleService;
        this.loadTaskFactory = loadTaskFactory;
        this.entryPropertiesService = entryPropertiesService;
        this.feedLoaderService = feedLoaderService;
        this.feedStateService = feedStateService;
    }

    public void createFeed(String url){
        requireNonNull(url);

        SyndFeed syndFeed = feedLoaderService.load(url);
        if (syndFeed.getEntries().isEmpty()){
            throw new IncorrectRssException("Feed doesn't contain entries");
        }

        if (isNull(syndFeed.getPublishedDate())){
            throw new IncorrectRssException("Feed doesn't contain published date");
        }

        Feed feed = new Feed();
        feedStateService.update(syndFeed, feed);
        feedDao.save(feed);
    }

    public void createFeed(String url, long pollPeriodInMs){
        requireNonNull(url);
        if (pollPeriodInMs < 1){
            throw new IllegalArgumentException("Poll period has to be greater than 0");
        }

        String filename = url.replaceAll("[^A-Za-z0-9]", "_");
        Feed feed = new Feed();
        feed.setHref(url);
        feed.setName(filename);
        feed.setPollPeriodInMs(pollPeriodInMs);

        feedDao.save(feed);

        int id = feed.getId();

        Runnable task = loadTaskFactory.create(feed);
        scheduleService.scheduleTask(id, pollPeriodInMs, task);
    }

    public void hideProperty(Feed feed, String propertyName){
        entryPropertiesService.hideParameter(feed, propertyName);
    }

    public void showProperty(Feed feed, String propertyName){
        entryPropertiesService.showParameter(feed, propertyName);
    }

    public void setFeedSurveyPeriod(Feed feed, long pollPeriodInMs){
        requireNonNull(feed);

        if (pollPeriodInMs < 1){
            throw new IllegalArgumentException("Poll period has to be greater than 0");
        }

        feed.setPollPeriodInMs(pollPeriodInMs);
        feedDao.update(feed);

        Runnable task = loadTaskFactory.create(feed);
        scheduleService.scheduleTask(feed.getId(), pollPeriodInMs, task);
    }

    public void setFeedFilename(Feed feed, String filename){
        requireNonNull(feed);
        requireNonNull(filename);

        feed.setFilename(filename);
        feedDao.update(feed);
    }

    public void setFeedAmountOfElementsAtOnce(Feed feed, int amountOfElementsAtOnce){
        requireNonNull(feed);
        if (amountOfElementsAtOnce < 1){
            throw new IllegalArgumentException("Amount of elements at once has to be greater than 0");
        }

        feed.setAmountOfElementsAtOnce(amountOfElementsAtOnce);
        feedDao.update(feed);
    }

    public void disablePoll(Feed feed){
        requireNonNull(feed);

        int id = feed.getId();
        scheduleService.cancelTask(id);
    }

//    public void enablePoll(Feed feed){
//        requireNonNull(feed);
//
//        int id = feed.getId();
//        scheduleService.scheduleTask(id, );
//    }

    public Feed findById(int id){
        return feedDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feed with id="+ id + "is not found"));
    }


}
