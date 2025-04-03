package io.github.schance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {

    private final int corePoolSize;

    private final int maxSize;

    private final int timeout;

    private final TimeUnit timeUnit;

    public final BlockingQueue<Runnable> taskList;

    private final RejectHandler rejectHandler;

    public MyThreadPool(int corePoolSize, int maxSize, int timeout, TimeUnit timeUnit, BlockingQueue<Runnable> taskList, RejectHandler rejectHandler) {
        this.corePoolSize = corePoolSize;
        this.maxSize = maxSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskList = taskList;
        this.rejectHandler = rejectHandler;
    }

    List<Thread> coreList = new ArrayList<>();

    List<Thread> supportList = new ArrayList<>();

    void execute(Runnable task) {
        if (coreList.size() < corePoolSize) {
            Thread thread = new CoreThread();
            coreList.add(thread);
            thread.start();
        }
        if (taskList.offer(task)) {
            return;
        }
        if (coreList.size() + supportList.size() < maxSize) {
            Thread thread = new SupportThread();
            supportList.add(thread);
            thread.start();
        }
        if (!taskList.offer(task)) {
            rejectHandler.reject(task, this);
        }
    }

    class CoreThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task = taskList.take();
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class SupportThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task = taskList.poll(timeout, timeUnit);
                    if (task == null) {
                        supportList.remove(this);
                        break;
                    }
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Support thread finished: " + Thread.currentThread().getName());
        }
    }
}
