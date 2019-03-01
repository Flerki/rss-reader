package com.amairovi.dto;

import com.amairovi.model.Feed;

import java.util.LinkedHashMap;
import java.util.Map;

public class FeedInfo {
    private final int id;

    private final String name;

    private final String link;

    private final long pollPeriodInMs;

    private final String filename;

    private final int amountOfElementsAtOnce;

    private final ChannelInfo channelInfo;

    private final Map<String, Boolean> entryParameterNameToVisibility;

    private final boolean isPolled;

    public FeedInfo(Feed feed) {
        id = feed.getId();
        name = feed.getName();
        link = feed.getHref();
        pollPeriodInMs = feed.getPollPeriodInMs();
        filename = feed.getFilename();
        amountOfElementsAtOnce = feed.getAmountOfElementsAtOnce();
        isPolled = feed.isPolled();

        channelInfo = new ChannelInfo(feed.getFeedProperties());
        entryParameterNameToVisibility = new LinkedHashMap<>(feed.getEntryParameterNameToVisibility());
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

    public ChannelInfo getChannelInfo() {
        return channelInfo;
    }

    public Map<String, Boolean> getEntryParameterNameToVisibility() {
        return entryParameterNameToVisibility;
    }

    public boolean isPolled() {
        return isPolled;
    }
}
