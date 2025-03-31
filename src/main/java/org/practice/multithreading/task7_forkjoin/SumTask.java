package org.practice.multithreading.task7_forkjoin;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class SumTask {

    public static class SumTaksImpl extends RecursiveTask<Integer> {

        int[] array;

        public SumTaksImpl(int[] array) {
            this.array = array;
        }

        @Override
        protected Integer compute() {
            if (array.length > 2) {
                int mid = array.length / 2;
                SumTaksImpl left = new SumTaksImpl(Arrays.copyOfRange(array, 0, mid));
                SumTaksImpl right = new SumTaksImpl(Arrays.copyOfRange(array, mid, array.length));
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
            System.out.printf("Task %s execute in thread %s%n", this, Thread.currentThread().getName());
            return Arrays.stream(array).sum();
        }
    }

    public static void main(String[] args) {
        System.out.println("Procecors " + Runtime.getRuntime().availableProcessors());

        int[] ints = prepareArray(10);
        SumTaksImpl task = new SumTaksImpl(ints);
        System.out.println(task.invoke());
    }

    private static int[] prepareArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

}
