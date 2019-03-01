package com.amairovi.service;

import com.amairovi.exception.FeedProcessingException;
import com.amairovi.exception.InvalidLinkFormatException;
import com.amairovi.model.Feed;
import com.amairovi.utility.FeedStubServer;
import com.amairovi.utility.Main;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FeedLoaderServiceTest {
    private FeedLoaderService feedLoaderService;

    private static FeedStubServer feedStubServer;
    private static int port = 8080;

    @BeforeAll
    static void beforeAll() {
        feedStubServer = new FeedStubServer(port);
        feedStubServer.start();
    }

    @AfterAll
    static void afterAll() {
        feedStubServer.stop();
    }

    @BeforeEach
    void setup() {
        feedLoaderService = new FeedLoaderService();
    }

    @Nested
    class Load {
        @Test
        void when_malformed_url_then_throw_error() {
            Feed feed = new Feed();
            feed.setHref("href");

            assertThrows(InvalidLinkFormatException.class, () -> feedLoaderService.load(feed));
        }

        @Test
        void when_not_real_url_then_return_empty() {
            Feed feed = new Feed();
            feed.setHref("http://some-made-up-url");

            assertThrows(FeedProcessingException.class, () -> feedLoaderService.load(feed));
        }

        @Test
        void when_atom_returns_then_load_and_parse_successfully() throws IOException, FeedException {
            Feed feed = new Feed();
            feed.setHref(feedStubServer.getAtomUrl());

            SyndFeed result = feedLoaderService.load(feed);

            SyndFeed expected = loadFeedFromFile("atom.xml");
            assertEquals(expected, result);
        }

        @Test
        void when_rss_returns_then_load_and_parse_successfully() throws IOException, FeedException {
            Feed feed = new Feed();
            feed.setHref(feedStubServer.getRssUrl());

            SyndFeed result = feedLoaderService.load(feed);

            SyndFeed expected = loadFeedFromFile("rss.xml");
            assertEquals(expected, result);
        }

        private SyndFeed loadFeedFromFile(String name) throws IOException, FeedException {
            URL url = Main.class.getResource(name);
            try (XmlReader xmlReader = new XmlReader(url)) {
                return new SyndFeedInput().build(xmlReader);
            }
        }

    }

}