package com.amairovi.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Feed {

    private int id;

    private String name;

    private String href;

    private FeedExtras feedExtras = new FeedExtras();

    private FeedProperties feedProperties;

    private Map<String, Boolean> entryParameterNameToVisibility = new LinkedHashMap<>();

    private long surveyPeriodInMs;

    private String filename;

    private int amountOfElementsAtOnce = 1;

    public FeedProperties getFeedProperties() {
        return feedProperties;
    }

    public void setFeedProperties(FeedProperties feedProperties) {
        this.feedProperties = feedProperties;
    }

    public Map<String, Boolean> getEntryParameterNameToVisibility() {
        return entryParameterNameToVisibility;
    }

    public void addParameter(String parameterName) {
        if (!entryParameterNameToVisibility.containsKey(parameterName)) {
            entryParameterNameToVisibility.put(parameterName, TRUE);
        }
    }

    public void hideParameter(String parameterName) {
        if (!entryParameterNameToVisibility.containsKey(parameterName)) {
            throw new IllegalArgumentException("No entry with name=" + parameterName);
        }
        entryParameterNameToVisibility.put(parameterName, FALSE);
    }

    public void showParameter(String parameterName) {
        if (!entryParameterNameToVisibility.containsKey(parameterName)) {
            throw new IllegalArgumentException("No entry with name=" + parameterName);
        }
        entryParameterNameToVisibility.put(parameterName, TRUE);
    }

    public FeedExtras getFeedExtras() {
        return feedExtras;
    }

    public void setFeedExtras(FeedExtras feedExtras) {
        this.feedExtras = feedExtras;
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

    public long getSurveyPeriodInMs() {
        return surveyPeriodInMs;
    }

    public void setSurveyPeriodInMs(long surveyPeriodInMs) {
        this.surveyPeriodInMs = surveyPeriodInMs;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return Objects.equals(name, feed.name) &&
                Objects.equals(href, feed.href) &&
                Objects.equals(feedExtras, feed.feedExtras);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, href, feedExtras);
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", href='" + href + '\'' +
                ", feedExtras=" + feedExtras +
                '}';
    }
}
