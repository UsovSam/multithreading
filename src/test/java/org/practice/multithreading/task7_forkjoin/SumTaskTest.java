package org.practice.multithreading.task7_forkjoin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SumTaskTest {


    @Test
    public void test() {
        int[] arrays = prepareArray(1000);
        int sum = Arrays.stream(arrays).sum();
        SumTask.SumTaksImpl task = new SumTask.SumTaksImpl(arrays);
        Integer result = task.invoke();

        Assertions.assertEquals(sum, result.intValue());
    }


    private static int[] prepareArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }
}
