package org.practice.multithreading.task2_deadlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Deadlock_ReetrantLock {

    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();
    private static final int MAX_ATTEMPTS = 3;
    private static final int BASE_DELAY = 10;
    private static final int MAX_DELAY = 100;

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            int attempts = 0;
            int randomValue = new Random(1).nextInt(BASE_DELAY);
            boolean success = false;
            while (!success && attempts < MAX_ATTEMPTS) {
                attempts++;
                lock1.lock();
                try {
                    System.out.println("Thread1: " + Thread.currentThread().getName());
                    if (lock2.tryLock(BASE_DELAY, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("Thread1: Smth after lock2.tryLock " + Thread.currentThread().getName());
                            success = true;
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

                if (!success) {
                    try {
                        // Экспоненциальная задержка перед следующей попыткой
                        int delay = (int) Math.min(
                                BASE_DELAY * Math.pow(2, attempts - 1),
                                MAX_DELAY
                        ) + randomValue;
                        System.out.println("Thread1: wait " + delay + " before next attempt");
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            if (!success) {
                System.out.println("Thread1: can't lock");
            }
        });

        Thread thread2 = new Thread(() -> {
            int attempts = 0;
            int randomValue = new Random(2).nextInt(BASE_DELAY);
            boolean success = false;
            while (!success && attempts < MAX_ATTEMPTS) {
                attempts++;
                lock2.lock();
                try {
                    System.out.println("Thread2: " + Thread.currentThread().getName());
                    if (lock1.tryLock(BASE_DELAY, TimeUnit.SECONDS)) {
                        try {
                            System.out.println("Thread2: Smth after lock1.tryLock " + Thread.currentThread().getName());
                            success = true;
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


                if (!success) {
                    try {
                        int delay = (int) Math.min(
                                BASE_DELAY * Math.pow(2, attempts - 1),
                                MAX_DELAY
                        ) + randomValue;
                        System.out.println("Thread2: wait " + delay + " before next attempt");
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!success) {
                System.out.println("Thread2: can't lock");
            }
        });


        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Finished");
    }

}
