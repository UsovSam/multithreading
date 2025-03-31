package org.practice.multithreading.task8_threadlocal;

import org.junit.jupiter.api.Test;

public class UserServiceTest {

    @Test
    public void test() throws InterruptedException {
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            UserService us = new UserService("userId-" + i);
            threads[i] = new Thread(us);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

    }

}
