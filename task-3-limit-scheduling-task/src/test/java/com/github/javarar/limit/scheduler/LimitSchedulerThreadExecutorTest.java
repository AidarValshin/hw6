package com.github.javarar.limit.scheduler;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimitSchedulerThreadExecutorTest {
    @Test
    void testExecutionsCount() throws InterruptedException {
        LimitSchedulerThreadExecutor executor = new LimitSchedulerThreadExecutor(2);

        AtomicInteger executionCountAtFixedRate = new AtomicInteger();
        executor.scheduleAtFixedRate(executionCountAtFixedRate::incrementAndGet, 0, 100, TimeUnit.MILLISECONDS, 10L);

        AtomicInteger executionCountWithFixedDelay = new AtomicInteger();
        executor.scheduleWithFixedDelay(executionCountWithFixedDelay::incrementAndGet, 10, 200, TimeUnit.MILLISECONDS, 15L);

        while(!executor.getQueue().isEmpty()){

        }
        assertAll(
                () -> assertEquals(10, executionCountAtFixedRate.get()),
                () -> assertEquals(15, executionCountWithFixedDelay.get()));
    }
}
