package org.practice.multithreading.task1_counter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CounterTest {

    private final int THREAD_COUNT = 5;
    private final int THREAD_ITERATIONS = 1000;

    @Test
    public void testCounter_wrong() throws InterruptedException {
        Counter counter = new Counter_Wrong();

        runProcess(counter);

        Assertions.assertNotEquals(THREAD_COUNT * THREAD_ITERATIONS, counter.getCounter());
    }

    @Test
    public void testCounter_sync() throws InterruptedException {
        Counter counter = new Counter_Sync();

        runProcess(counter);

        Assertions.assertEquals(THREAD_COUNT * THREAD_ITERATIONS, counter.getCounter());
    }

    @Test
    public void testCounter_atomic() throws InterruptedException {
        Counter counter = new Counter_Atomic();

        runProcess(counter);

        Assertions.assertEquals(THREAD_COUNT * THREAD_ITERATIONS, counter.getCounter());
    }

    @Test
    public void testCounter_recrentlock() throws InterruptedException {
        Counter counter = new Counter_ReentrantLock();

        runProcess(counter);

        Assertions.assertEquals(THREAD_COUNT * THREAD_ITERATIONS, counter.getCounter());
    }

    private void runProcess(Counter counter) throws InterruptedException {
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = prepareThread(counter);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        // need to wait all threads are finished
        for (Thread thread : threads) {
            thread.join();
        }
    }

    private Thread prepareThread(Counter counter) {
        return new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });
    }

}