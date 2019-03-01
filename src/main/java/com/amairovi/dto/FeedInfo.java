package com.amairovi.dto;

import com.amairovi.model.Feed;

public class FeedInfo {
    private final int id;

    private final String name;

    private final String link;

    private final long pollPeriodInMs;

    private final String filename;

    private final int amountOfElementsAtOnce;

    public FeedInfo(Feed feed) {
        id = feed.getId();
        name = feed.getName();
        link = feed.getHref();
        pollPeriodInMs = feed.getPollPeriodInMs();
        filename = feed.getFilename();
        amountOfElementsAtOnce = feed.getAmountOfElementsAtOnce();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public long getPollPeriodInMs() {
        return pollPeriodInMs;
    }

    public String getFilename() {
        return filename;
    }

    public int getAmountOfElementsAtOnce() {
        return amountOfElementsAtOnce;
    }
}
