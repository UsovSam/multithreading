package org.practice.multithreading.task1_counter;

public class Counter_Sync extends Counter {

    private int counter = 0;

    @Override
    public synchronized void increment() {
        counter++;
    }

    @Override
    public int getCounter() {
        return counter;
    }
}
