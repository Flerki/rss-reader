package com.amairovi.core.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Feed {

    private int id;

    private String name;

    private String href;

    private FeedProperties feedProperties;

    private Map<String, Boolean> entryParameterNameToVisibility = new LinkedHashMap<>();

    private long pollPeriodInMs;

    private String filename;

    private int amountOfElementsAtOnce;

    private boolean isPolled;

    private long lastPollAtMs;

    public long getLastPollAtMs() {
        return lastPollAtMs;
    }

    public void setLastPollAtMs(long lastPollAtMs) {
        this.lastPollAtMs = lastPollAtMs;
    }

    public FeedProperties getFeedProperties() {
        return feedProperties;
    }

    public void setFeedProperties(FeedProperties feedProperties) {
        this.feedProperties = feedProperties;
    }

    public Map<String, Boolean> getEntryParameterNameToVisibility() {
        return entryParameterNameToVisibility;
    }

    public void setEntryParameterNameToVisibility(Map<String, Boolean> entryParameterNameToVisibility) {
        this.entryParameterNameToVisibility = entryParameterNameToVisibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPollPeriodInMs() {
        return pollPeriodInMs;
    }

    public void setPollPeriodInMs(long pollPeriodInMs) {
        this.pollPeriodInMs = pollPeriodInMs;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getAmountOfElementsAtOnce() {
        return amountOfElementsAtOnce;
    }

    public void setAmountOfElementsAtOnce(int amountOfElementsAtOnce) {
        this.amountOfElementsAtOnce = amountOfElementsAtOnce;
    }

    public boolean isPolled() {
        return isPolled;
    }

    public void setPolled(boolean polled) {
        isPolled = polled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return id == feed.id &&
                pollPeriodInMs == feed.pollPeriodInMs &&
                amountOfElementsAtOnce == feed.amountOfElementsAtOnce &&
                Objects.equals(name, feed.name) &&
                Objects.equals(href, feed.href) &&
                Objects.equals(feedProperties, feed.feedProperties) &&
                Objects.equals(entryParameterNameToVisibility, feed.entryParameterNameToVisibility) &&
                Objects.equals(filename, feed.filename);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, href, feedProperties, entryParameterNameToVisibility, pollPeriodInMs, filename, amountOfElementsAtOnce);
    }
}
