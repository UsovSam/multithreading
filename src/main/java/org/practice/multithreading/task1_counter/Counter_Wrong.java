package org.practice.multithreading.task1_counter;

public class Counter_Wrong extends Counter {

    private int counter = 0;

    @Override
    public void increment() {
        counter++;
    }

    @Override
    public int getCounter() {
        return counter;
    }

}
