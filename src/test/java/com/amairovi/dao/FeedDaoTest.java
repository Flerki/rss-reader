package com.amairovi.dao;

import com.amairovi.model.Feed;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;

public class FeedDaoTest {

    private FeedDao feedDao = new FeedDao();

    @Test
    void add_feed_success(){
        Feed feed = new Feed();
        feed.setHref("href");

        feedDao.save(feed);

        List<Feed> feeds = feedDao.findAll();
        assertThat(feeds, hasItem(feed));
    }

    @Test
    void add_several_feeds_success(){
        Feed feed1 = new Feed();
        feed1.setHref("href");

        Feed feed2 = new Feed();
        feed2.setHref("href2");

        feedDao.save(feed1);
        feedDao.save(feed2);

        List<Feed> feeds = feedDao.findAll();
        assertThat(feeds, containsInAnyOrder(feed1, feed2));
    }
}