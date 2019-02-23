package com.amairovi.dao;

import com.amairovi.model.Feed;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeedDaoTest {

    private final static Feed FEED = new Feed();
    private final static Feed FEED2 = new Feed();
    static {
        FEED.setId(1);
        FEED.setName("name");
        FEED.setHref("href");
        FEED.setId(2);
        FEED2.setName("name2");
        FEED2.setHref("href2");
    }

    private FeedDao feedDao = new FeedDao();

    @Test
    void add_feed_success(){

        feedDao.save(FEED);

        List<Feed> feeds = feedDao.findAll();
        assertThat(feeds, hasItem(FEED));
    }

    @Test
    void add_several_feeds_success(){
        feedDao.save(FEED);
        feedDao.save(FEED2);

        List<Feed> feeds = feedDao.findAll();
        assertThat(feeds, containsInAnyOrder(FEED, FEED2));
        assertThat(feeds.get(0).getId(), equalTo(1));
        assertThat(feeds.get(1).getId(), equalTo(2));
    }

    @Test
    void when_find_by_name_existed_then_return_according_one(){
        feedDao.save(FEED);

        Optional<Feed> actual = feedDao.findByName(FEED.getName());

        assertTrue(actual.isPresent());
        assertThat(actual.get(), equalTo(FEED));
    }

    @Test
    void when_find_by_name_non_existed_then_return_empty(){
        Optional<Feed> actual = feedDao.findByName(FEED.getName());

        assertFalse(actual.isPresent());
    }

    @Test
    void when_find_by_id_existed_then_return_according_one(){
        feedDao.save(FEED);
        feedDao.save(FEED2);

        Optional<Feed> actual = feedDao.findById(FEED.getId());

        assertTrue(actual.isPresent());
        assertThat(actual.get(), equalTo(FEED));
    }

    @Test
    void when_find_by_id_non_existed_then_return_empty(){
        Optional<Feed> actual = feedDao.findById(FEED.getId());

        assertFalse(actual.isPresent());
    }
}