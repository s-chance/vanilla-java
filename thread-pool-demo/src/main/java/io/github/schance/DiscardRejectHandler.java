package io.github.schance;

public class DiscardRejectHandler implements RejectHandler {
    @Override
    public void reject(Runnable rejectTask, MyThreadPool threadPool) {
        threadPool.taskList.poll();
        threadPool.execute(rejectTask);
    }
}
