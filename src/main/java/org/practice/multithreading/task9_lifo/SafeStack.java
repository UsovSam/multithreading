package org.practice.multithreading.task9_lifo;

import java.util.concurrent.atomic.AtomicStampedReference;

public class SafeStack {

    private AtomicStampedReference<Node> head = new AtomicStampedReference<>(null, 0);

    public void push(Integer value) throws InterruptedException {
        Node newNode = new Node(value);
        int[] stamps = new int[1];
        Node oldHead;

        do {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            oldHead = head.get(stamps);
            newNode.setNext(oldHead);
        } while (!head.compareAndSet(oldHead, newNode, stamps[0], stamps[0] + 1));
    }

    public Integer pop() throws InterruptedException {
        Node currentHead;
        int[] stamps = new int[1];
        Node newHead;
        do {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            currentHead = head.get(stamps);
            if (currentHead == null) {
                return null;
            }
            newHead = currentHead.getNext();
        } while (!head.compareAndSet(currentHead, newHead, stamps[0], stamps[0] + 1));
        return currentHead.getValue();
    }

    public Integer peek() {
        int[] stamps = new int[1];
        Node node = head.get(stamps);
        return node == null ? null : node.getValue();
    }

    public boolean isEmpty() {
        return head.getReference() == null;
    }

    public static void main(String[] args) {
        SafeStack stack = new SafeStack();

        Thread pushThread = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    stack.push(i);
                    System.out.println("Pushed: " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted adding to stack");
            }
        });

        Thread popThread = new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    Integer value = stack.pop();
                    System.out.println("Popped: " + value);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted getting from stack");
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
