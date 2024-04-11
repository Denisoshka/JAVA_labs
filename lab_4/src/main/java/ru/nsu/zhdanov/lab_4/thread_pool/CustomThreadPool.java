package ru.nsu.zhdanov.lab_4.thread_pool;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomThreadPool {
  private final ArrayList<WorkerThread> workers;
  private final LinkedBlockingQueue<Runnable> queue;

  public CustomThreadPool(int poolSize) {
    queue = new LinkedBlockingQueue<Runnable>();
    workers = new ArrayList<>(poolSize);

    for (int i = 0; i < poolSize; ++i) {
      workers.add(new WorkerThread());
      workers.getLast().start();
    }
  }

  public void execute(Runnable task) {
    synchronized (queue) {
      queue.add(task);
      queue.notify();
    }
  }

  public void shutdown() {
    for (WorkerThread worker : workers) {
      worker.interrupt();
    }
  }

  private class WorkerThread extends Thread {
    @Override
    public void run() {
      while (true) {
        Runnable task;
        try {
          task = queue.take();
        } catch (InterruptedException e) {
          return;
        }

        try {
          task.run();
        } catch (RuntimeException ignored) {
          return;
        }
      }
    }
  }
}
