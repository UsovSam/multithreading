package org.practice.multithreading.task1_counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter_Atomic extends Counter {

    private final AtomicInteger counter;

    public Counter_Atomic() {
        counter = new AtomicInteger(0);
    }

    @Override
    public void increment() {
        counter.incrementAndGet();
    }

    @Override
    public int getCounter() {
        return counter.get();
    }
}
