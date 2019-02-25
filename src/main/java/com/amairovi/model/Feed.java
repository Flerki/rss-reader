package com.amairovi.model;

import java.io.Serializable;
import java.util.Objects;

public class Feed implements Serializable {

    private transient int id;

    private String name;

    private String href;

    private FeedExtras feedExtras = new FeedExtras();

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
