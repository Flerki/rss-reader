package com.amairovi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleService {

    private final Map<Integer, ScheduledFuture<?>> idToTask;

    private final ScheduledExecutorService executor;

    public ScheduleService(ScheduledExecutorService executor) {
        this.idToTask = new HashMap<>();
        this.executor = executor;
    }

    public void scheduleTask(int feedId, long period, Runnable loadTask) {
        removeExisted(feedId);
        scheduleNewOne(feedId, period, loadTask);
    }

    private void removeExisted(int feedId) {
        ScheduledFuture<?> existed = idToTask.get(feedId);

        if (existed != null) {
            existed.cancel(false);
        }
    }

    private void scheduleNewOne(int feedId, long period, Runnable loadTask) {
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(loadTask, 0, period, TimeUnit.MILLISECONDS);
        idToTask.put(feedId, scheduledFuture);
    }

    public Set<Integer> getScheduledIds() {
        return idToTask.keySet();
    }
}
