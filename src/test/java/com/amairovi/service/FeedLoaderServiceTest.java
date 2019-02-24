package com.amairovi.service;

import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FeedLoaderServiceTest {
    private FeedLoaderService feedLoaderService;

    @BeforeEach
    void setup() {
        feedLoaderService = new FeedLoaderService();
    }

    @Nested
    class Load {
        @Test
        void when_malformed_url_then_return_empty() {
            Feed feed = new Feed();
            feed.setHref("href");

            Optional<SyndFeed> syndFeed = feedLoaderService.load(feed);

            assertFalse(syndFeed.isPresent());
        }

        @Test
        void when_not_real_url_then_return_empty() {
            Feed feed = new Feed();
            feed.setHref("http://some-made-up-url");

            Optional<SyndFeed> syndFeed = feedLoaderService.load(feed);

            assertFalse(syndFeed.isPresent());
        }
    }

}