package com.amairovi.service;

import com.amairovi.dao.FeedDao;
import com.amairovi.model.Feed;

import static java.util.Objects.requireNonNull;

public class FeedService {
    private final FeedDao feedDao;
    private final ScheduleService scheduleService;
    private final LoadTaskFactory loadTaskFactory;

    public FeedService(FeedDao feedDao, ScheduleService scheduleService, LoadTaskFactory loadTaskFactory) {
        this.feedDao = feedDao;
        this.scheduleService = scheduleService;
        this.loadTaskFactory = loadTaskFactory;
    }

    public void createFeed(String url, long pollPeriod){
        requireNonNull(url);
        if (pollPeriod < 1){
            throw new IllegalArgumentException("Poll period has to be greater than 0");
        }

        String filename = url.replaceAll("^[A-Za-z0-9]", "_");
        Feed feed = new Feed();
        feed.setHref(url);
        feed.setName(filename);
        feed.getFeedExtras().setSurveyPeriod(pollPeriod);

        feedDao.save(feed);

        int id = feed.getId();

        Runnable task = loadTaskFactory.create(feed);
        scheduleService.scheduleTask(id, pollPeriod, task);
    }

    public void setFeedSurveyPeriod(Feed feed, long pollPeriod){
        requireNonNull(feed);

        if (pollPeriod < 1){
            throw new IllegalArgumentException("Poll period has to be greater than 0");
        }

        feed.getFeedExtras().setSurveyPeriod(pollPeriod);
        feedDao.update(feed);

        Runnable task = loadTaskFactory.create(feed);
        scheduleService.scheduleTask(feed.getId(), pollPeriod, task);
    }

    public void setFeedFilename(Feed feed, String filename){
        requireNonNull(feed);
        requireNonNull(filename);

        feed.getFeedExtras().setFilename(filename);
        feedDao.update(feed);
    }

    public void setFeedAmountOfElementsAtOnce(Feed feed, int amountOfElementsAtOnce){
        requireNonNull(feed);
        if (amountOfElementsAtOnce < 1){
            throw new IllegalArgumentException("Amount of elements at once has to be greater than 0");
        }

        feed.getFeedExtras().setAmountOfElementsAtOnce(amountOfElementsAtOnce);
        feedDao.update(feed);
    }

    public void disablePoll(Feed feed){
        requireNonNull(feed);

        int id = feed.getId();
        scheduleService.cancelTask(id);
    }
}
