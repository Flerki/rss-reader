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
    private final EntryPropertiesService entryPropertiesService;
    private final FeedLoaderService feedLoaderService;
    private final FeedStateService feedStateService;

    public FeedService(FeedDao feedDao,
                       ScheduleService scheduleService,
                       EntryPropertiesService entryPropertiesService,
                       FeedLoaderService feedLoaderService,
                       FeedStateService feedStateService) {
        this.feedDao = feedDao;
        this.scheduleService = scheduleService;
        this.entryPropertiesService = entryPropertiesService;
        this.feedLoaderService = feedLoaderService;
        this.feedStateService = feedStateService;
    }

    public int createFeed(String url, long pollPeriodInMs){
        requireNonNull(url);

        SyndFeed syndFeed = feedLoaderService.load(url);
        if (syndFeed.getEntries().isEmpty()){
            throw new IncorrectRssException("Feed doesn't contain entries");
        }

        if (isNull(syndFeed.getPublishedDate())){
            throw new IncorrectRssException("Feed doesn't contain published date");
        }

        Feed feed = new Feed();
        feed.setName(url);
        String filename = url.replaceAll("[^A-Za-z0-9]", "_");
        feed.setFilename(filename);
        feed.setHref(url);
        feed.setPollPeriodInMs(pollPeriodInMs);

        feedStateService.update(syndFeed, feed);
        feedDao.save(feed);
        return feed.getId();
    }

    public void hideProperty(Feed feed, String propertyName){
        entryPropertiesService.hideParameter(feed, propertyName);
        feedDao.update(feed);
    }

    public void showProperty(Feed feed, String propertyName){
        entryPropertiesService.showParameter(feed, propertyName);
        feedDao.update(feed);
    }

    public void setPollPeriodInMs(Feed feed, long pollPeriodInMs){
        requireNonNull(feed);

        if (pollPeriodInMs < 1){
            throw new IllegalArgumentException("Poll period has to be greater than 0");
        }

        feed.setPollPeriodInMs(pollPeriodInMs);
        feedDao.update(feed);
        
        if (feed.isPolled()){
            scheduleService.cancelPolling(feed.getId());

            scheduleService.schedulePolling(feed);
        }
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

    public void enablePoll(Feed feed) {
        feed.setPolled(true);
        feedDao.update(feed);

        scheduleService.schedulePolling(feed);
    }

    public void disablePoll(Feed feed){
        requireNonNull(feed);

        feed.setPolled(false);
        feedDao.update(feed);

        int id = feed.getId();
        scheduleService.cancelPolling(id);
    }

    public Feed findById(int id){
        return feedDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feed with id="+ id + "is not found"));
    }
}
