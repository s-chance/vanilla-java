package io.github.schance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class ScheduleService {

    ExecutorService executorService = Executors.newFixedThreadPool(6);

    Trigger trigger = new Trigger();

    void schedule(Runnable task, long delay) {
        Job job = new Job();
        job.setTask(task);
        job.setStartTime(System.currentTimeMillis() + delay);
        job.setDelay(delay);
        trigger.queue.offer(job);
        trigger.wakeUp();
    }

    class Trigger {
        // sorted automatic
        PriorityBlockingQueue<Job> queue = new PriorityBlockingQueue<>();

        Thread thread = new Thread(() -> {
            while (true) {
                while (queue.isEmpty()) {
                    LockSupport.park();
                }
                Job latelyJob = queue.peek();
                if (latelyJob.getStartTime() < System.currentTimeMillis()) {
                    // to check if exists a shorter task
                    latelyJob = queue.poll();
                    // execute the latest task
                    executorService.execute(latelyJob.getTask());
                    // calculate the next execute time
                    Job nextJob = new Job();
                    nextJob.setTask(latelyJob.getTask());
                    nextJob.setDelay(latelyJob.getDelay());
                    nextJob.setStartTime(System.currentTimeMillis() + latelyJob.getDelay());
                    queue.offer(nextJob);
                } else {
                    LockSupport.parkUntil(latelyJob.getStartTime());
                }
            }
        });

        {
            thread.start();
            System.out.println("task started");
        }

        void wakeUp() {
            LockSupport.unpark(thread);
        }
    }
}
