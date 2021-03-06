package com.amairovi.core.service;

import com.amairovi.core.dao.FeedDao;
import com.amairovi.core.model.Feed;
import com.amairovi.core.service.polling.FeedLoaderService;
import com.amairovi.core.service.polling.LoadTaskFactory;
import com.amairovi.core.service.polling.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.amairovi.utility.CustomAssert.assertThrowsIae;
import static com.amairovi.utility.CustomAssert.assertThrowsNpe;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FeedServiceTest {

    private Feed feed;
    private int feedId;

    private FeedDao feedDao;

    private FeedService feedService;
    private ScheduleService scheduleService;

    @BeforeEach
    void setup() {
        feedDao = mock(FeedDao.class);
        scheduleService = mock(ScheduleService.class);
        EntryPropertiesService entryPropertiesService = mock(EntryPropertiesService.class);
        FeedLoaderService feedLoaderService = mock(FeedLoaderService.class);
        FeedStateService feedStateService = mock(FeedStateService.class);

        feedService = new FeedService(feedDao, scheduleService, entryPropertiesService, feedLoaderService, feedStateService);

        feed = new Feed();
        feedId = 1;
        feed.setId(feedId);
    }


    @Nested
    class CreateFeed{
        @Test
        void when_url_is_null_then_NPE() {
            assertThrowsNpe(() -> feedService.createFeed(null, 1));
        }
    }


    @Nested
    class SetFeedSurveyPeriod {
        @Test
        void when_feed_is_null_then_NPE() {
            assertThrowsNpe(() -> feedService.setPollPeriodInMs(null, 1));
        }

        @ParameterizedTest
        @ValueSource(longs = {0, -1, Long.MIN_VALUE})
        void when_period_is_less_then_one_then_exception(long surveyPeriodInMs) {
            assertThrowsIae(() -> feedService.setPollPeriodInMs(feed, surveyPeriodInMs));
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 100, Long.MAX_VALUE})
        void when_feed_and_period_is_ok_then_success(long surveyPeriodInMs) {
            feedService.setPollPeriodInMs(feed, surveyPeriodInMs);

            assertEquals(feed.getPollPeriodInMs(), surveyPeriodInMs);

            verify(feedDao).update(eq(feed));
        }
    }

    @Nested
    class SetFeedFilename {
        @Test
        void when_feed_is_null_then_NPE() {
            assertThrowsNpe(() -> feedService.setFeedFilename(null, null));
        }

        @Test
        void when_filename_is_less_then_one_then_exception() {
            assertThrowsNpe(() -> feedService.setFeedFilename(feed, null));
        }

        @Test
        void when_feed_and_filename_is_ok_then_success() {
            String filename = "filename";
            feedService.setFeedFilename(feed, filename);

            assertEquals(feed.getFilename(), filename);

            verify(feedDao).update(eq(feed));
        }
    }

    @Nested
    class SetFeedAmountOfElementsAtOnce {
        @Test
        void when_feed_is_null_then_NPE() {
            assertThrowsNpe(() -> feedService.setFeedAmountOfElementsAtOnce(null, 1));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
        void when_amount_is_less_then_one_then_exception(int amountOfElementsAtOnce) {
            assertThrowsIae(() -> feedService.setFeedAmountOfElementsAtOnce(feed, amountOfElementsAtOnce));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 100, Integer.MAX_VALUE})
        void when_feed_and_period_is_ok_then_success(int amountOfElementsAtOnce) {
            feedService.setFeedAmountOfElementsAtOnce(feed, amountOfElementsAtOnce);

            assertEquals(feed.getAmountOfElementsAtOnce(), amountOfElementsAtOnce);

            verify(feedDao).update(eq(feed));
        }
    }

    @Nested
    class DisablePoll {
        @Test
        void when_feed_is_null_then_NPE() {
            assertThrowsNpe(() -> feedService.disablePoll(null));
        }

        @Test
        void when_feed_is_not_null_then_disable() {
            feedService.disablePoll(feed);

            verify(scheduleService).cancelPolling(feedId);
        }
    }

}