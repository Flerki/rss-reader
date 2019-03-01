package com.amairovi;

import com.amairovi.exception.IncorrectRssException;
import com.amairovi.utility.FeedStubServer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class CoreTest {

    private Core core;

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
    void setup(){
        core = new Core();
    }

    @Test
    void when_rss_without_entries_then_throw_error(){
        assertThrows(IncorrectRssException.class,
                () -> core.createFeed(feedStubServer.getAtomWithoutEntries()));
        assertThrows(IncorrectRssException.class,
                () -> core.createFeed(feedStubServer.getRssWithoutEntries()));
    }

    @Test
    void when_rss_without_published_then_throw_error(){
        assertThrows(IncorrectRssException.class,
                () -> core.createFeed(feedStubServer.getAtomWithoutPublished()));
        assertThrows(IncorrectRssException.class,
                () -> core.createFeed(feedStubServer.getRssWithoutPublished()));
    }
}