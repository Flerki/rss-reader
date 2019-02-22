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
    static {
        FEED.setName("name");
        FEED.setHref("href");
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
        Feed feed2 = new Feed();
        feed2.setHref("href2");

        feedDao.save(FEED);
        feedDao.save(feed2);

        List<Feed> feeds = feedDao.findAll();
        assertThat(feeds, containsInAnyOrder(FEED, feed2));
    }

    @Test
    void when_find_by_name_existed_then_return_according_one(){
        feedDao.save(FEED);

        Optional<Feed> actual = feedDao.findByName(FEED.getName());

        assertTrue(actual.isPresent());
        assertThat(actual.get(), equalTo(FEED));
    }

    @Test
    void when_find_by_name_non_existed_then_return_according_one(){
        Optional<Feed> actual = feedDao.findByName(FEED.getName());

        assertFalse(actual.isPresent());
    }
}