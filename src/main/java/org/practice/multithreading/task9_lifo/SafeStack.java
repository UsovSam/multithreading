package org.practice.multithreading.task9_lifo;

import java.util.concurrent.atomic.AtomicReference;

public class SafeStack {

    private AtomicReference<Node> head = new AtomicReference<>();

    public void push(Integer value) {
        Node newNode = new Node(value);
        Node oldHead;
        do {
            oldHead = head.get();
            newNode.setNext(oldHead);
        } while (!head.compareAndSet(oldHead, newNode));
    }

    public Integer pop() {
        Node currentHead;
        Node newHead;
        do {
            currentHead = head.get();
            if (currentHead == null) {
                return null;
            }
            newHead = currentHead.getNext();
        } while (!head.compareAndSet(currentHead, newHead));
        return currentHead.getValue();
    }

    public Integer peek() {
        if (!isEmpty()) {
            return head.get().getValue();
        }
        return null;
    }

    public boolean isEmpty() {
        return head.get() == null;
    }

    public static void main(String[] args) {
        SafeStack stack = new SafeStack();

        Thread pushThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                stack.push(i);
                System.out.println("Pushed: " + i);
            }
        });

        Thread popThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                Integer value = stack.pop();
                System.out.println("Popped: " + value);
            }
        });


        pushThread.start();
        popThread.start();

        try {
            pushThread.join();
            popThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
