package com.amairovi.core.service.polling;

import com.amairovi.core.model.Feed;
import com.amairovi.core.service.polling.LoadTaskFactory;
import com.amairovi.core.service.polling.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.amairovi.utility.ReflectionUtils.setFinalStatic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    private ScheduleService scheduleService;

    private Runnable doNothing = () -> {};

    private LoadTaskFactory loadTaskFactory;

    @BeforeEach
    void setup() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        loadTaskFactory = mock(LoadTaskFactory.class);
        scheduleService = new ScheduleService(scheduledExecutorService, loadTaskFactory);
    }

    @Nested
    class GetScheduledIds {
        @Test
        void when_no_scheduled_task_then_scheduled_is_empty() {
            assertThat(scheduleService.getScheduledIds(), empty());
        }

        @Test
        void when_scheduled_task_then_they_are_returned() throws Exception {
            Map<Integer, ScheduledFuture<?>> idToTask = new HashMap<>();
            idToTask.put(1, null);
            idToTask.put(2, null);
            idToTask.put(3, null);

            setFinalStatic(scheduleService, "idToTask", idToTask);

            assertThat(scheduleService.getScheduledIds(), contains(1, 2, 3));
        }
    }

    @Nested
    class ScheduleTask {
        @Test
        void when_schedule_task_then_success() throws InterruptedException {
            int period = 100;
            int feedId = 1;
            Feed feed = new Feed();
            feed.setPollPeriodInMs(period);
            feed.setId(feedId);

            AtomicInteger atomicInteger = new AtomicInteger();
            when(loadTaskFactory.create(eq(feed))).thenReturn(atomicInteger::incrementAndGet);

            scheduleService.schedulePolling(feed);

            assertThat(scheduleService.getScheduledIds(), contains(feedId));

            Thread.sleep(500);
            assertThat(atomicInteger.get(), greaterThan(3));
        }

        @Test
        void when_schedule_several_tasks_then_success() {
            int feedId = 1;
            int feedId2 = 2;
            int period = 100;
            Feed feed1 = new Feed();
            feed1.setId(feedId);
            feed1.setPollPeriodInMs(period);
            Feed feed2 = new Feed();
            feed2.setId(feedId2);
            feed2.setPollPeriodInMs(period);

            when(loadTaskFactory.create(any())).thenReturn(doNothing);

            scheduleService.schedulePolling(feed1);
            scheduleService.schedulePolling(feed2);

            assertThat(scheduleService.getScheduledIds(), contains(feedId, feedId2));
            verify(loadTaskFactory).create(eq(feed1));
            verify(loadTaskFactory).create(eq(feed2));
        }
    }

    @Nested
    class CancelTask {
        private ScheduledFuture scheduledFuture2;

        @BeforeEach
        void setup() throws Exception {
            Map<Integer, ScheduledFuture<?>> idToTask = new HashMap<>();
            idToTask.put(1, mock(ScheduledFuture.class));

            scheduledFuture2 = mock(ScheduledFuture.class);
            idToTask.put(2, scheduledFuture2);
            setFinalStatic(scheduleService, "idToTask", idToTask);
        }

        @Test
        void when_no_cancelling_task_then_do_nothing() {
            scheduleService.cancelPolling(3);

            assertThat(scheduleService.getScheduledIds(), contains(1, 2));
        }

        @Test
        void when_cancel_scheduled_task_then_cancel_it() {
            scheduleService.cancelPolling(2);

            assertThat(scheduleService.getScheduledIds(), contains(1));

            verify(scheduledFuture2).cancel(eq(false));
        }
    }
}