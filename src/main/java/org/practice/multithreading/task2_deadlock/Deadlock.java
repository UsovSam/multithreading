package org.practice.multithreading.task2_deadlock;

public class Deadlock {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName());
                synchronized (lock2) {
                    System.out.println("Smth 2 " + Thread.currentThread().getName());
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName());
                synchronized (lock1) {
                    System.out.println("Smth 2 " + Thread.currentThread().getName());
                }
            }
        });


        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Finished");
    }

}
