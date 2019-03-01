package com.amairovi.dto;

import com.amairovi.model.Feed;

public class FeedBriefInfo {
    private final int id;
    private final String name;
    private final String link;
    private final String filename;

    public FeedBriefInfo(Feed feed){
        this.id = feed.getId();
        this.name = feed.getName();
        this.link = feed.getHref();
        this.filename = feed.getFilename();
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
}
