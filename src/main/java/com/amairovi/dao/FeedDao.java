package com.amairovi.dao;

import com.amairovi.model.Feed;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class FeedDao {

    private final Map<Integer, Feed> idToFeed;
    private int nextId;

    private final FeedPersistenceStore persistenceStore;

    public FeedDao(FeedPersistenceStore persistenceStore) {
        this.persistenceStore = persistenceStore;

        nextId = 1;
        idToFeed = new HashMap<>();
        List<Feed> feeds = persistenceStore.load();
        for (int i = 0; i < feeds.size(); i++) {
            idToFeed.put(nextId++, feeds.get(i));
        }
    }


    public void save(Feed feed) {
        requireNonNull(feed);

        feed.setId(nextId);
        idToFeed.put(nextId, feed);
        nextId++;

        persistenceStore.store(findAll());
    }

    public void update(Feed feed) {
        requireNonNull(feed);

        persistenceStore.store(findAll());
    }

    public Optional<Feed> findByName(String name) {
        Predicate<Feed> feedWithSearchedName = feed -> feed.getName().equals(name);
        return feeds()
                .stream()
                .filter(feedWithSearchedName)
                .findFirst();
    }

    public Optional<Feed> findById(int id) {
        return Optional.ofNullable(idToFeed.get(id));
    }

    public List<Feed> findAll() {
        return new ArrayList<>(feeds());
    }

    private Collection<Feed> feeds() {
        return idToFeed.values();
    }
}
