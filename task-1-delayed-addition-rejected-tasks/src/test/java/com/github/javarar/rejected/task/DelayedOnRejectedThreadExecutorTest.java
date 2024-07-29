package com.github.javarar.rejected.task;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DelayedOnRejectedThreadExecutorTest {
    private final int TASK_COUNT = 30;
    @Test
    void tesCorrectWork() throws InterruptedException {
        DelayedOnRejectedThreadExecutor executor = new DelayedOnRejectedThreadExecutor(
                3,
                4,
                100,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(5),
                200,
                TimeUnit.MILLISECONDS
        );

        for (int i = 0; i < TASK_COUNT; i++) {
            int finalI = i;
            executor.execute(() -> {
                try {
                    Thread.sleep(5000);
                    System.out.printf("Finish task #: %s%n", finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
        }

        while(executor.getActiveCount()!=0){

        }
        assertEquals(executor.getCompletedTaskCount(), TASK_COUNT);
    }

}
