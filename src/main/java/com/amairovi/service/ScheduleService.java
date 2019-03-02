package com.amairovi.service;

import com.amairovi.model.Feed;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleService {

    private final Map<Integer, ScheduledFuture<?>> idToTask;

    private final ScheduledExecutorService executor;

    private final LoadTaskFactory loadTaskFactory;


    public ScheduleService(ScheduledExecutorService executor, LoadTaskFactory loadTaskFactory) {
        this.loadTaskFactory = loadTaskFactory;
        this.idToTask = new ConcurrentHashMap<>();
        this.executor = executor;
    }

    public void schedulePolling(Feed feed){
        int id = feed.getId();

        removeExisted(id);

        Runnable task = loadTaskFactory.create(feed);
        long pollPeriodInMs = feed.getPollPeriodInMs();

        long delay = calculateDelay(feed);
        scheduleNewOne(id, delay, pollPeriodInMs, task);
    }

    private long calculateDelay(Feed feed) {
        long lastPollAtMs = feed.getLastPollAtMs();
        long pollPeriodInMs = feed.getPollPeriodInMs();
        long nowInMs = Instant.now().toEpochMilli();
        return Math.max(0, lastPollAtMs + pollPeriodInMs - nowInMs);
    }

    public void cancelPolling(int feedId) {
        removeExisted(feedId);
    }

    private void removeExisted(int feedId) {
        ScheduledFuture<?> existed = idToTask.get(feedId);

        if (existed != null) {
            existed.cancel(false);
            idToTask.remove(feedId);
        }
    }

    private void scheduleNewOne(int feedId, long delay, long period, Runnable loadTask) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(loadTask, delay, period, TimeUnit.MILLISECONDS);
        idToTask.put(feedId, scheduledFuture);
    }


    public Set<Integer> getScheduledIds() {
        return idToTask.keySet();
    }
}
