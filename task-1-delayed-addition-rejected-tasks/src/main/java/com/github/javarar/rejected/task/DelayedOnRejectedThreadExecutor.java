package com.github.javarar.rejected.task;

import java.util.concurrent.*;

public class DelayedOnRejectedThreadExecutor extends ThreadPoolExecutor {

    private final ScheduledExecutorService scheduledExecutor;
    private final long rejectedRetrayDelayInTimeUnits;
    private final TimeUnit rejectedRetrayDelayTimeUnit;

    public DelayedOnRejectedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, long rejectedRetrayDelayInTimeUnits, TimeUnit rejectedRetrayDelayTimeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.rejectedRetrayDelayInTimeUnits = rejectedRetrayDelayInTimeUnits;
        this.rejectedRetrayDelayTimeUnit = rejectedRetrayDelayTimeUnit;
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.setRejectedExecutionHandler(this::rejectedExecution);
    }

    public DelayedOnRejectedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, long rejectedRetrayDelayInTimeUnits, TimeUnit rejectedRetrayDelayTimeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.rejectedRetrayDelayInTimeUnits = rejectedRetrayDelayInTimeUnits;
        this.rejectedRetrayDelayTimeUnit = rejectedRetrayDelayTimeUnit;
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.setRejectedExecutionHandler(this::rejectedExecution);
    }

    public DelayedOnRejectedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, long rejectedRetrayDelayInTimeUnits, TimeUnit rejectedRetrayDelayTimeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.rejectedRetrayDelayInTimeUnits = rejectedRetrayDelayInTimeUnits;
        this.rejectedRetrayDelayTimeUnit = rejectedRetrayDelayTimeUnit;
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.setRejectedExecutionHandler(this::rejectedExecution);
    }

    public DelayedOnRejectedThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, long rejectedRetrayDelayInTimeUnits, TimeUnit rejectedRetrayDelayTimeUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.rejectedRetrayDelayInTimeUnits = rejectedRetrayDelayInTimeUnits;
        this.rejectedRetrayDelayTimeUnit = rejectedRetrayDelayTimeUnit;
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.setRejectedExecutionHandler(this::rejectedExecution);
    }

    private void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        scheduledExecutor.schedule(() -> {
            System.out.printf("Retry to add rejected task: %s%n", r);
            executor.execute(r);
        }, rejectedRetrayDelayInTimeUnits, rejectedRetrayDelayTimeUnit);
    }
}
