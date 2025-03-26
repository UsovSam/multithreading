package org.practice.multithreading.task2_deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock_ReetrantLock {

    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {


        Thread thread1 = new Thread(() -> {
            lock1.lock();
            try {

                System.out.println("Thread1: " + Thread.currentThread().getName());
                if (lock2.tryLock(10, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread1: Smth after lock2.tryLock " + Thread.currentThread().getName());
                    } finally {
                        lock2.unlock();
                    }
                } else {
                    System.out.println("Thread1: Lock 2 is locked");
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                lock1.unlock();
            }
        });

        Thread thread2 = new Thread(() -> {
            lock2.lock();
            try {
                System.out.println("Thread2: " + Thread.currentThread().getName());
                if (lock1.tryLock(20, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("Thread2: Smth after lock1.tryLock " + Thread.currentThread().getName());
                    } finally {
                        lock1.unlock();
                    }
                } else {
                    System.out.println("Thread2: Lock 1 is locked");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock2.unlock();
            }
        });


        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Finished");
    }

}
