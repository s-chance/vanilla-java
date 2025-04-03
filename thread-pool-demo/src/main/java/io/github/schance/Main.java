package io.github.schance;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(
                2,
                4,
                1,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                new DiscardRejectHandler()
        );

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            myThreadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " finished. task " + finalI +" completed");
            });
        }


        System.out.println("Main thread: " + Thread.currentThread().getName());
    }
}