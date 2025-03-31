package org.practice.multithreading.task8_threadlocal;

public class UserService implements Runnable {
    private ThreadLocal<String> localThreadValue = new ThreadLocal<>();
    private String userId;

    public UserService(String userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        localThreadValue.set(userId);
        System.out.println("Thread Id: " + Thread.currentThread().getName() + " - " + localThreadValue.get());
        simulateWork();
        String s = localThreadValue.get();
        System.out.println("Thread Id: " + Thread.currentThread().getName() + " - " + s);
    }

    private void simulateWork() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}