package com.amairovi.core;

import com.amairovi.core.exception.IncorrectRssException;
import com.amairovi.utility.FeedStubServer;
import com.amairovi.utility.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.amairovi.utility.FileUtils.removeConfigsIfPresent;
import static com.amairovi.utility.FileUtils.removeFileIfPresent;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class FeedServiceFacadeTestIT {

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
        removeConfigsIfPresent();
        feedServiceFacade = new FeedServiceFacade();
        feedServiceFacade.initialize();
    }

    @AfterEach
    void cleanUp(){
        removeConfigsIfPresent();
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

    @Test
    void when_single_request_to_server_then_store_data_in_file() throws IOException {
        ScheduledExecutorService scheduledExecutorService = Mockito.mock(ScheduledExecutorService.class);
        ArgumentCaptor<Runnable> captor = mockScheduledExecutorServiceAndCaptureScheduledRunnable(scheduledExecutorService);
        int feedId = 1;

        FeedServiceFacade feedServiceFacade = new FeedServiceFacade(scheduledExecutorService);
        feedServiceFacade.initialize();
        feedServiceFacade.createFeed(feedStubServer.getUrlForSingleFeedRequest());
        feedServiceFacade.enablePoll(feedId);

        captor.getValue().run();

        String filename = feedServiceFacade.getFeedFullDescription(feedId).getFilename();
        assertFilesEqual(filename, "single-request");
        removeFileIfPresent(filename);
    }

    @Test
    void when_several_requests_to_server_then_store_data_in_file() throws IOException {
        ScheduledExecutorService scheduledExecutorService = Mockito.mock(ScheduledExecutorService.class);
        ArgumentCaptor<Runnable> captor = mockScheduledExecutorServiceAndCaptureScheduledRunnable(scheduledExecutorService);
        int feedId = 1;

        FeedServiceFacade feedServiceFacade = new FeedServiceFacade(scheduledExecutorService);
        feedServiceFacade.initialize();
        feedServiceFacade.createFeed(feedStubServer.getUrlForSeveralFeedsRequest());
        feedServiceFacade.enablePoll(feedId);

        Runnable loadTask = captor.getValue();
        loadTask.run();
        loadTask.run();
        loadTask.run();

        String filename = feedServiceFacade.getFeedFullDescription(feedId).getFilename();
        assertFilesEqual(filename, "several-requests");
        removeFileIfPresent(filename);
    }

    private ArgumentCaptor<Runnable> mockScheduledExecutorServiceAndCaptureScheduledRunnable(ScheduledExecutorService scheduledExecutorService){
        ScheduledFuture future = Mockito.mock(ScheduledFuture.class);
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        when(scheduledExecutorService.scheduleAtFixedRate(captor.capture(), anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(future);
        return captor;
    }

    private void assertFilesEqual(String actualFilename, String expectedFilename) throws IOException {
        Path actualPath = Paths.get(actualFilename);
        List<String> actualStrings = Files.readAllLines(actualPath)
                .stream()
                .map(String::trim)
                .collect(Collectors.toList());
        List<String> expectedStrings = FileUtils.readLines(expectedFilename, FeedServiceFacade.class);

        assertThat(actualStrings, contains(expectedStrings.toArray()));
    }
}