package com.amairovi.dao;

import com.amairovi.model.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedDao {

    private List<Feed> feeds = new ArrayList<>();

    public void save(Feed feed) {
        feeds.add(feed);
    }

    public List<Feed> findAll() {
        return new ArrayList<>(feeds);
    }

}
