package com.amairovi.dao;

import com.amairovi.model.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class FeedDao {

    private final List<Feed> feeds;

    private final FeedPersistenceStore persistenceStore;

    public FeedDao(FeedPersistenceStore persistenceStore) {
        this.persistenceStore = persistenceStore;
        this.feeds = persistenceStore.load();
    }


    public void save(Feed feed) {
        requireNonNull(feed);

        int nextId = getLastId() + 1;
        feed.setId(nextId);
        feeds.add(feed);

        persistenceStore.store(feeds);
    }

    public void update(Feed feed){
        persistenceStore.store(feeds);
    }

    private int getLastId() {
        if (feeds.isEmpty()){
            return 0;
        }
        int positionOfLastFeed = feeds.size() - 1;
        return feeds.get(positionOfLastFeed).getId();
    }

    public Optional<Feed> findByName(String name){
        Predicate<Feed> feedWithSearchedName = feed -> feed.getName().equals(name);
        return feeds.stream()
                .filter(feedWithSearchedName)
                .findFirst();
    }

    public Optional<Feed> findById(int id){
        Predicate<Feed> feedWithSearchedId = feed -> feed.getId() == id;
        return feeds.stream()
                .filter(feedWithSearchedId)
                .findFirst();
    }

    public List<Feed> findAll() {
        return new ArrayList<>(feeds);
    }

}
