package org.practice.multithreading.task1_counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter_Atomic extends Counter {

    private final AtomicInteger counter;

    public Counter_Atomic() {
        counter = new AtomicInteger(0);
    }

    @Override
    public synchronized void increment() {
        counter.incrementAndGet();
//        counter.addAndGet(1);
    }

    @Override
    public int getCounter() {
        return counter.get();
    }
}
