package com.amairovi.service;

import com.amairovi.dao.FeedDao;
import com.amairovi.model.Feed;

import static java.util.Objects.requireNonNull;

public class FeedService {
    private final FeedDao feedDao;
    private final ScheduleService scheduleService;


    public FeedService(FeedDao feedDao, ScheduleService scheduleService) {
        this.feedDao = feedDao;
        this.scheduleService = scheduleService;
    }

    public void setFeedSurveyPeriod(Feed feed, long surveyPeriodInMs){
        requireNonNull(feed);

        if (surveyPeriodInMs < 1){
            throw new IllegalArgumentException("Survey period has to be greater than 0");
        }

        feed.getFeedExtras().setSurveyPeriod(surveyPeriodInMs);
        feedDao.update(feed);

        scheduleService.scheduleTask(feed.getId(), surveyPeriodInMs, () -> {});
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
}
