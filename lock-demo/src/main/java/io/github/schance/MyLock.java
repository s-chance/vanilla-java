package io.github.schance;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class MyLock {

    AtomicBoolean flag = new AtomicBoolean(false);

    Thread owner = null;

    AtomicReference<Node> head = new AtomicReference<>(new Node());

    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    void lock() {
        // this segment make the lock is unfair
        /*if (flag.compareAndSet(false, true)) {
            System.out.println(Thread.currentThread().getName() + ": locked");
            owner = Thread.currentThread();
            return;
        }*/
        Node current = new Node();
        current.thread = Thread.currentThread();
        while (true) {
            // must be the latest node
            Node currentTail = tail.get();
            // if failed, it means the currentTail is not the latest node, there may be thread unsafe
            // so use while loop to get the latest node
            if (tail.compareAndSet(currentTail, current)) {
                System.out.println(Thread.currentThread().getName() + ": added to the tail of the linked list");
                current.pre = currentTail;
                currentTail.next = current;
                break;
            }
        }
        while (true) {
            // condition
            // head --> A --> B --> C
            if (current.pre == head.get() && flag.compareAndSet(false, true)) {
                owner = Thread.currentThread();
                head.set(current);
                current.pre.next = null;
                current.pre = null;
                System.out.println(Thread.currentThread().getName() + ": be notified and got the lock");
                return;
            }
            LockSupport.park();
        }
    }

    void unlock() {
        if (Thread.currentThread() != owner) {
            throw new IllegalStateException("Current thread is not the owner of the lock");
        }
        Node headNode = head.get();
        Node nextNode = headNode.next;
        flag.set(false); // release the lock
        if (nextNode != null) {
            System.out.println(Thread.currentThread().getName() + ": notify the next node: " + nextNode.thread.getName());
            LockSupport.unpark(nextNode.thread);
        }
    }

    class Node {
        Node pre;
        Node next;
        Thread thread;
    }
}
