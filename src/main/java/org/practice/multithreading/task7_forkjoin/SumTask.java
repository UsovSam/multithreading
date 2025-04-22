package org.practice.multithreading.task7_forkjoin;

import lombok.SneakyThrows;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class SumTask {

    public static final Integer THRESHOLD = 100;
    public static final Integer MAX_ELEMENT = 10000;

    public static class SumTakskImpl extends RecursiveTask<Integer> {

        int[] array;
        int start;
        int end;

        public SumTakskImpl(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @SneakyThrows
        @Override
        protected Integer compute() {
            int length = end - start;

            if (length <= THRESHOLD) {
                int sum = 0;
                for (int i = start; i < end; i++) {
                    TimeUnit.MILLISECONDS.sleep(1);
                    sum += array[i];
                }
                return sum;
            }

            int mid = start + length / 2;
            SumTakskImpl left = new SumTakskImpl(array, start, mid);
            SumTakskImpl right = new SumTakskImpl(array, mid, end);

            left.fork();
//            right.fork();

            return right.compute() + left.join(); // вычисляем правую часть, пока ждем ответа от левой
//            return right.join() + left.join(); текущий поток блокируется дважды
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Processors " + Runtime.getRuntime().availableProcessors());

        int[] ints = prepareArray(MAX_ELEMENT);


        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < ints.length; i++) {
            TimeUnit.MILLISECONDS.sleep(1);
            sum += ints[i];
        }
        System.out.println("Sum: " + sum);
        long end = System.nanoTime();
        System.out.println("SIMPLE SUM: " + TimeUnit.NANOSECONDS.toMillis(end - start));


        start = System.nanoTime();
        try (ForkJoinPool forkPool = ForkJoinPool.commonPool()) {

            SumTakskImpl task = new SumTakskImpl(ints, 0, ints.length);
            Integer invoke = forkPool.invoke(task);
            System.out.println("Result: " + invoke);
        }
        end = System.nanoTime();

        System.out.println("ForkPool: " + TimeUnit.NANOSECONDS.toMillis(end - start));
    }

    private static int[] prepareArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

}
