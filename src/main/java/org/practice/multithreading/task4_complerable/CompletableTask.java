package org.practice.multithreading.task4_complerable;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CompletableTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        List<CompletableFuture<Void>> collect = list.stream()
                .map(CompletableTask::handleElement)
                .toList();

        CompletableFuture.allOf(collect.toArray(new CompletableFuture[0]));

        System.out.println("==============");

        List<Integer> listForSum = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<CompletableFuture<Integer>> forSum = listForSum.stream()
                .map(CompletableTask::handleElementSum)
                .toList();

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(forSum.toArray(new CompletableFuture[0]));

        Integer i = voidCompletableFuture.thenApply(o ->
                        forSum.stream().mapToInt(CompletableFuture::join).sum()
                )
                .get();

        System.out.println("Result " + i);

    }

    private static CompletableFuture<Void> handleElement(int element) {
        System.out.println("Started  " + element);
        return CompletableFuture.supplyAsync(() -> element)
                .thenApply(o -> 10 / o)
                .thenApply(o -> {
                    try {
                        TimeUnit.SECONDS.sleep(o);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return o;
                })
                .thenAccept(o -> System.out.println(element + " " + o))
                .exceptionally(throwable -> {
                    log.error(String.valueOf(throwable));
                    System.out.println(element);
                    return null;
                });
    }

    private static CompletableFuture<Integer> handleElementSum(int element) {
        System.out.println("Started Sum " + element);
        return CompletableFuture.supplyAsync(() -> element)
                .thenApply(o -> 10 / o)
                .thenApply(o -> {
                    try {
                        TimeUnit.SECONDS.sleep(o);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return o;
                })
                .exceptionally(throwable -> {
                    log.error(String.valueOf(throwable));
                    System.out.println(element);
                    return -100;
                });
    }

}
