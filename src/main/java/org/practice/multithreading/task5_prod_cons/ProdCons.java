package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class ProdCons {

    private static final Integer POISON_PILL = -1;

    public static class Producer implements Runnable {
        BlockingQueue<Integer> queue;
        private final int maxElements;
        private final boolean isLastProducer;

        public Producer(BlockingQueue<Integer> queue, int maxElements, boolean isLastProducer) {
            this.queue = queue;
            this.maxElements = maxElements;
            this.isLastProducer = isLastProducer;
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
                    System.out.println("Produced by:" + Thread.currentThread().getName() + " val:" + value);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            if (isLastProducer) {
                try {
                    queue.put(POISON_PILL);
                    System.out.println("Producer " + Thread.currentThread().getName() + " sent POISON_PILL");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
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
                try {
                    Integer el = queue.take();

                    if (el.equals(POISON_PILL)) {
                        queue.put(POISON_PILL);
                        System.out.println("Consumer " + Thread.currentThread().getName() + " received POISON_PILL");
                        break;
                    }

                    System.out.println("Consumed by:" + Thread.currentThread().getName() + " val:" + el);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("Consumer " + Thread.currentThread().getName() + " finished");
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
        int producerCount = 4;

        for (int i = 0; i < producerCount; i++) {
            boolean isLast = (i == producerCount - 1);
            new Thread(new Producer(queue, 10, isLast), "Producer-" + i).start();
        }

        int consumerCount = 2;
        for (int i = 0; i < consumerCount; i++) {
            new Thread(new Consumer(queue), "Consumer-" + i).start();
        }
    }
}