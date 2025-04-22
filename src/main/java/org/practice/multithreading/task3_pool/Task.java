package org.practice.multithreading.task3_pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Task extends Thread {

    public static class TaskThread extends Thread {
        private final int taskId;

        public TaskThread(int i) {
            this.taskId = i;
        }

        public int getTaskId() {
            return taskId;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " is running. Task: " + taskId);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " is interrupted. Task: " + taskId);
                Thread.currentThread().interrupt();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        RejectedExecutionHandler rejectedExecutionHandler = (r, e) -> {
            TaskThread tt = (TaskThread) r;
            System.out.println("RejectedExecutionHandler: " + Thread.currentThread().getName() + " rejected task " + tt.getTaskId()
                    //        + " stats:" + e.getActiveCount() + " " + e.getCompletedTaskCount() + " " + e.getTaskCount()
            );
            try {
                System.out.println("RejectedExecutionHandler: Retry task " + tt.getTaskId());
                Thread.sleep(2000);
                e.execute(r);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                System.err.println("RejectedExecutionHandler: Retry interrupted, task will be lost: " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("RejectedExecutionHandler: Failed to resubmit task: " + ex.getMessage());
            }
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4,
                5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10), rejectedExecutionHandler);

        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(new TaskThread(i));
        }
        threadPoolExecutor.shutdown();
        if (!threadPoolExecutor.awaitTermination(15, TimeUnit.SECONDS)) {
            threadPoolExecutor.shutdownNow();
        }
    }
}
