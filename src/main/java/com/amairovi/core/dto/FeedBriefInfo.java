package com.amairovi.core.dto;

import com.amairovi.core.model.Feed;

public class FeedBriefInfo {
    private final int id;
    private final String name;
    private final String link;
    private final String filename;
    private final boolean isPolled;

    public FeedBriefInfo(Feed feed){
        this.id = feed.getId();
        this.name = feed.getName();
        this.link = feed.getHref();
        this.filename = feed.getFilename();
        this.isPolled = feed.isPolled();
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

    public String getFilename() {
        return filename;
    }

    public boolean isPolled() {
        return isPolled;
    }
}
