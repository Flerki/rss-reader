package com.amairovi;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.core.exception.IncorrectRssException;
import com.amairovi.utility.FeedStubServer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FeedServiceFacadeTest {

    private FeedServiceFacade feedServiceFacade;

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
        feedServiceFacade = new FeedServiceFacade();
    }

    @Test
    void when_rss_without_entries_then_throw_error(){
        assertThrows(IncorrectRssException.class,
                () -> feedServiceFacade.createFeed(feedStubServer.getAtomWithoutEntries()));
        assertThrows(IncorrectRssException.class,
                () -> feedServiceFacade.createFeed(feedStubServer.getRssWithoutEntries()));
    }

    @Test
    void when_rss_without_published_then_throw_error(){
        assertThrows(IncorrectRssException.class,
                () -> feedServiceFacade.createFeed(feedStubServer.getAtomWithoutPublished()));
        assertThrows(IncorrectRssException.class,
                () -> feedServiceFacade.createFeed(feedStubServer.getRssWithoutPublished()));
    }
}