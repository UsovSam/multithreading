package org.practice.multithreading.task5_prod_cons;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ProdCons {

    public static class Producer implements Runnable {
        BlockingQueue<Integer> queue;
        private final int maxElements;

        public Producer(BlockingQueue<Integer> queue, int maxElements) {
            this.queue = queue;
            this.maxElements = maxElements;
        }

        @Override
        public void run() {
            generateNumber();
        }

        public void generateNumber() {
            for (int i = 0; i < maxElements; i++) {
                Integer value = ThreadLocalRandom.current().nextInt(100);
                try {
                    this.queue.put(value);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("Produced by:" + Thread.currentThread().getName() + " val:" + value);
            }
        }
    }

    public static class Consumer implements Runnable {
        BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                if (!queue.isEmpty()) {
                    Integer el = queue.poll();
                    log.info("Consumed by:" + Thread.currentThread().getName() + " val:" + el);
                }
            }
        }
    }

    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

        for (int i = 0; i < 4; i++) {
            new Thread(new Producer(queue, 10)).start();
        }

        for (int i = 0; i < 2; i++) {
            new Thread(new Consumer(queue)).start();
        }
    }
}
