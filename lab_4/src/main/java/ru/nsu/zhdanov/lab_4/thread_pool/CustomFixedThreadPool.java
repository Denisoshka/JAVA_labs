package ru.nsu.zhdanov.lab_4.thread_pool;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomFixedThreadPool implements ExecutorService {
  private final BlockingQueue<Runnable> taskPool;
  private final AtomicBoolean shutdowned = new AtomicBoolean(false);
  private final AtomicInteger activeTaskQ;
  private final CopyOnWriteArrayList<Worker> workersPool;

  public CustomFixedThreadPool(int nThreads) {
    this(nThreads, new LinkedBlockingQueue<Runnable>());
  }

  public CustomFixedThreadPool(int nThreads, BlockingQueue<Runnable> taskPool) {
    this.taskPool = taskPool;
    this.activeTaskQ = new AtomicInteger(0);
    this.workersPool = new CopyOnWriteArrayList<>();
  }

  private class Worker extends Thread {
    @Override
    public void run() {
      while (true) {
        try {
          Runnable task = taskPool.poll(60, TimeUnit.SECONDS);
          if (task == null) {
            workersPool.remove(this);
            return;
          }
          task.run();
        } catch (InterruptedException e) {
          return;
        }
      }
    }
  }

  @Override
  public void shutdown() {

  }
//  def x(list: list[int]) -> float

  @NotNull
  @Override
  public List<Runnable> shutdownNow() {
    return taskPool.stream().toList();
  }

  @Override
  public boolean isShutdown() {
    return shutdowned.get();
  }

  @Override
  public boolean isTerminated() {
    return shutdowned.get();
  }

  @Override
  public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    return false;
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Callable<T> task) {
    taskPool.put(task.);
    return new FutureTask<>(task);
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Runnable task, T result) {
    return new FutureTask<>(task, result);
  }

  @NotNull
  @Override
  public Future<?> submit(@NotNull Runnable task) {
    return new FutureTask<>(task, null);
  }

  @NotNull
  @Override
  public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
    for (var task : tasks) {
    }
    return null;
  }

  @NotNull
  @Override
  public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    return null;
  }

  @NotNull
  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    return null;
  }

  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return null;
  }

  @Override
  public void execute(@NotNull Runnable command) {

  }



  /*private final ArrayList<WorkerThread> workers;
  private final LinkedBlockingQueue<Runnable> queue;

  public CustomThreadPool(int poolSize) {
    queue = new LinkedBlockingQueue<Runnable>();
    workers = new ArrayList<>(poolSize);

    for (int i = 0; i < poolSize; ++i) {
      workers.add(new WorkerThread());
      workers.getLast().start();
    }
  }

  public void execute(Runnable task) -> xyi{
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
*/
}
