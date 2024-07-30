package com.github.javarar.limit.scheduler;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class LimitSchedulerThreadExecutor extends ScheduledThreadPoolExecutor {

    public LimitSchedulerThreadExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public LimitSchedulerThreadExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public LimitSchedulerThreadExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public LimitSchedulerThreadExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit, Long maxExecutionCount) {
        if (maxExecutionCount == null) {
            super.scheduleAtFixedRate(command, initialDelay, period, unit);
        }
        if (maxExecutionCount < 1) {
            throw new IllegalArgumentException();
        }
        AtomicLong availableAttemts = new AtomicLong(maxExecutionCount);
        return super.scheduleAtFixedRate(() -> {
            if (availableAttemts.get() > 0) {
                command.run();
                availableAttemts.decrementAndGet();
            } else {
                this.remove(command);
            }

        }, initialDelay, period, unit);
    }


    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit, Long maxExecutionCount) {
        if (maxExecutionCount == null) {
            super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        }
        if (maxExecutionCount < 1) {
            throw new IllegalArgumentException();
        }
        AtomicLong availableAttemts = new AtomicLong(maxExecutionCount);
        return super.scheduleWithFixedDelay(() -> {
            if (availableAttemts.get() > 0) {
                command.run();
                availableAttemts.decrementAndGet();
            } else {
                this.remove(command);
            }

        }, initialDelay, delay, unit);
    }
    }
