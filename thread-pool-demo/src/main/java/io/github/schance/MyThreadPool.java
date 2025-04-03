package io.github.schance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyThreadPool {

    BlockingQueue<Runnable> taskList = new ArrayBlockingQueue<>(1024);

    Thread thread = new Thread(() -> {
        while (true) {
            try {
                Runnable task = taskList.take();
                task.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }, "Only-Thread");

    {
        thread.start();
    }

    List<Thread> threadList = new ArrayList<>();

    void execute(Runnable task) {
        boolean offer = taskList.offer(task);
    }
}
