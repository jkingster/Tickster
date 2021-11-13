package io.jking.tickster.object;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class ScheduledTask {

    private static Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private static final ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(
            2, runnable -> new Thread(runnable, "SCHEDULED_POOL")
    );

    private ScheduledTask() {
    }

    public static void scheduleTask(Runnable runnable, long period, TimeUnit timeUnit) {
        THREAD_POOL.scheduleAtFixedRate(runnable, 0, period, timeUnit);
    }

    public static void scheduleTask(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        THREAD_POOL.scheduleAtFixedRate(runnable, delay, period, timeUnit);
    }

}
