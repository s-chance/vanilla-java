package io.github.schance;

public class ThrowRejectHandler implements RejectHandler {
    @Override
    public void reject(Runnable rejectTask, MyThreadPool threadPool) {
        throw new RuntimeException("Task rejected: queue is full");
    }
}
