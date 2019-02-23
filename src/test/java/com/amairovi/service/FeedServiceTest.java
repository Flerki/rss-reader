package com.amairovi.service;

import com.amairovi.dao.FeedDao;
import com.amairovi.model.Feed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import static com.amairovi.utility.CustomAssert.assertThrowsIae;
import static com.amairovi.utility.CustomAssert.assertThrowsNpe;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class FeedServiceTest {

    private Feed feed;

    private FeedDao feedDao;

    private FeedService feedService;

    @BeforeEach
    void setup() {
        feedDao = Mockito.mock(FeedDao.class);
        feedService = new FeedService(feedDao);

        feed = new Feed();
    }


    @Nested
    class SetFeedSurveyPeriod {
        @Test
        void when_feed_is_null_then_NPE() {
            assertThrowsNpe(() -> feedService.setFeedSurveyPeriod(null, 1));
        }

        @ParameterizedTest
        @ValueSource(longs = {0, -1, Long.MIN_VALUE})
        void when_period_is_less_then_one_then_exception(long surveyPeriodInMs) {
            assertThrowsIae(() -> feedService.setFeedSurveyPeriod(feed, surveyPeriodInMs));
        }

        @ParameterizedTest
        @ValueSource(longs = {1, 100, Long.MAX_VALUE})
        void when_feed_and_period_is_ok_then_success(long surveyPeriodInMs) {
            feedService.setFeedSurveyPeriod(feed, surveyPeriodInMs);

            assertEquals(feed.getFeedExtras().getSurveyPeriod(), surveyPeriodInMs);

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

            assertEquals(feed.getFeedExtras().getFilename(), filename);

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

            assertEquals(feed.getFeedExtras().getAmountOfElementsAtOnce(), amountOfElementsAtOnce);

            verify(feedDao).update(eq(feed));
        }
    }

}