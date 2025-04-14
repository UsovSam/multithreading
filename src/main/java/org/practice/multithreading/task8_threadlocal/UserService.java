package org.practice.multithreading.task8_threadlocal;

public class UserService implements Runnable {
    private static final ThreadLocal<String> localThreadValue = new ThreadLocal<>();

    public UserService() {
    }

    @Override
    public void run() {
        try {
            localThreadValue.set("userId - " + Thread.currentThread().getName());

            System.out.println("Thread Id: " + Thread.currentThread().getName() + " - " + localThreadValue.get());
            simulateWork();

            String s = localThreadValue.get();
            if (s != null)
                System.out.println("Thread Id: " + Thread.currentThread().getName() + " - " + s);
        } finally {
            localThreadValue.remove();
        }
    }

    private void simulateWork() {
        try {
            Thread.sleep(1000);
            localThreadValue.set("userId - " + Thread.currentThread().getName() + " - " + localThreadValue.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new UserService(), "subthread-" + i).start();
        }

        String name = localThreadValue.get();
        System.out.println("Main thread (should be null): " + name);
    }
}