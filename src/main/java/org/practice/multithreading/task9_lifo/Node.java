package org.practice.multithreading.task9_lifo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {
    private Integer value;
    private Node next;

    public Node() {
        value = null;
        next = null;
    }

    public Node(Integer value) {
        this.value = value;
    }

}

