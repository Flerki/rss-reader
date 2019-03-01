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

        String filename = url.replaceAll("[^A-Za-z0-9]", "_");
        Feed feed = new Feed();
        feed.setHref(url);
        feed.setName(filename);
        feed.setSurveyPeriodInMs(pollPeriod);

        feedDao.save(feed);

        int id = feed.getId();

        Runnable task = loadTaskFactory.create(feed);
        scheduleService.scheduleTask(id, pollPeriod, task);
    }

    public void hideProperty(Feed feed, String propertyName){
        if (!feed.getEntryParameterNameToVisibility().containsKey(propertyName)){
            throw new IllegalArgumentException("There is no property with name=" + propertyName + " for feed with id=" + feed.getId());
        }
        feed.hideParameter(propertyName);
    }

    public void showProperty(Feed feed, String propertyName){
        if (!feed.getEntryParameterNameToVisibility().containsKey(propertyName)){
            throw new IllegalArgumentException("There is no property with name=" + propertyName + " for feed with id=" + feed.getId());
        }
        feed.showParameter(propertyName);
    }

    public void setFeedSurveyPeriod(Feed feed, long pollPeriod){
        requireNonNull(feed);

        if (pollPeriod < 1){
            throw new IllegalArgumentException("Poll period has to be greater than 0");
        }

        feed.setSurveyPeriodInMs(pollPeriod);
        feedDao.update(feed);

        Runnable task = loadTaskFactory.create(feed);
        scheduleService.scheduleTask(feed.getId(), pollPeriod, task);
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
