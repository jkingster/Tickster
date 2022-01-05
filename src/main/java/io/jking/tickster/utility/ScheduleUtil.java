package io.jking.tickster.utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;

public final class ScheduleUtil {
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(
            r -> {
                final Thread thread = new Thread(r, "Scheduler-Thread");
                thread.setDaemon(true);
                return thread;
            }
    );

    private ScheduleUtil() {
    }

    public static void scheduleTask(Runnable runnable, long duration, TimeUnit timeUnit) {
        EXECUTOR_SERVICE.scheduleAtFixedRate(runnable, 0L, duration, timeUnit);
    }

    public static void scheduleDelayedTask(Runnable runnable, long delay, long duration, TimeUnit timeUnit) {
        EXECUTOR_SERVICE.scheduleWithFixedDelay(runnable, delay, duration, timeUnit);
    }

}
