package com.amairovi.dao;

import com.amairovi.model.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class FeedDao {

    private List<Feed> feeds = new ArrayList<>();

    public void save(Feed feed) {
        feeds.add(feed);
    }

    public Optional<Feed> findByName(String name){
        Predicate<Feed> feedWithSearchedName = feed -> feed.getName().equals(name);
        return feeds.stream()
                .filter(feedWithSearchedName)
                .findFirst();
    }

    public List<Feed> findAll() {
        return new ArrayList<>(feeds);
    }

}
