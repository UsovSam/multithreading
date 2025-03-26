package org.practice.multithreading.task1_counter;

import java.util.concurrent.locks.ReentrantLock;

public class Counter_ReentrantLock extends Counter {

    private int counter = 0;
    private final ReentrantLock reentrantLock;

    public Counter_ReentrantLock() {
        reentrantLock = new ReentrantLock();
    }

    @Override
    public synchronized void increment() {
// I can do it this way ?
//
//        if(reentrantLock.tryLock()){
//            try {
//                counter++;
//            } finally {
//                reentrantLock.unlock();
//            }
//        } else {
//            System.out.println("Lock is locked");
//        }

        reentrantLock.lock();
        try {
            counter++;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public int getCounter() {
        return counter;
    }
}
