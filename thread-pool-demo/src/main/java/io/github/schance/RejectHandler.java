package io.github.schance;

public interface RejectHandler {
    void reject(Runnable rejectTask, MyThreadPool threadPool);
}
