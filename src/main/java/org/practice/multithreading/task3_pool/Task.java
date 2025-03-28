package org.practice.multithreading.task3_pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task {

    public static void main(String[] args) {
        RejectedExecutionHandler rejectedExecutionHandler = (r, e) -> System.out.println("error ");
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), rejectedExecutionHandler);
//        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(
                    () -> {
                        System.out.println(Thread.currentThread().getName() + " is running ");
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
        threadPoolExecutor.shutdown();

    }
}
