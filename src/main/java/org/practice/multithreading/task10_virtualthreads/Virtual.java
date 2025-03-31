package org.practice.multithreading.task10_virtualthreads;

import java.util.concurrent.TimeUnit;

public class Virtual {

    private static final int THREAD_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread.startVirtualThread(new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getThreadGroup().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        System.out.println("Finished");
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);

                }
            });
            thread.start();
        }
    }

}
