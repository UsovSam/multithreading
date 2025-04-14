package org.practice.multithreading.task10_virtualthreads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Virtual {

    private static final int THREAD_COUNT = 100_000;
    private static final Object PRINT_LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        long start = System.nanoTime();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < THREAD_COUNT; i++) {
                final int taskNumber = i;
                executor.submit(() -> {
                    try {
                        performTask("virtual", taskNumber);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
        }
        long end = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(end - start);
        System.out.println(duration + " ms");


        CountDownLatch latch1 = new CountDownLatch(THREAD_COUNT);
        long start1 = System.nanoTime();

        try (ExecutorService executor = Executors.newFixedThreadPool(1000)) {
            for (int i = 0; i < THREAD_COUNT; i++) {
                final int taskNumber = i;
                executor.submit(() -> {
                    try {
                        performTask("simple", taskNumber);
                    } finally {
                        latch1.countDown();
                    }
                });
            }
            latch.await();
        }

        long end1 = System.nanoTime();
        long duration1 = TimeUnit.NANOSECONDS.toMillis(end1 - start1);
        System.out.println(duration1 + " ms");
    }


    private static void performTask(String threadType, int taskNumber) {
        try {
            synchronized (PRINT_LOCK) {
                System.out.println(threadType + " " + taskNumber);
            }

            TimeUnit.MILLISECONDS.sleep(100);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("%s-%d was interrupted%n", threadType, taskNumber);
        }
    }

}
