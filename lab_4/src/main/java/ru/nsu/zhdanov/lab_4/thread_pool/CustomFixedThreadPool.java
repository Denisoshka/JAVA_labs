package ru.nsu.zhdanov.lab_4.thread_pool;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class CustomFixedThreadPool implements ExecutorService {
  public final static int POOL_DELAY = 180;
  public final static int OFFER_DELAY = 30;

  private final BlockingQueue<Runnable> taskPool;
  private volatile boolean isShutdown = false;
  private volatile boolean terminated = false;
  private final Set<Worker> workers;
  private final int nThreads;

  public CustomFixedThreadPool(int nThreads) {
    this(nThreads, new LinkedBlockingQueue<>());
  }

  public CustomFixedThreadPool(int nThreads, BlockingQueue<Runnable> taskPool) {
    this.workers = ConcurrentHashMap.newKeySet();
    this.nThreads = nThreads;
    this.taskPool = taskPool;
  }

  @Override
  public void shutdown() {
    isShutdown = true;
  }

  @NotNull
  @Override
  public List<Runnable> shutdownNow() {
    return taskPool.stream().toList();
  }

  @Override
  public boolean isShutdown() {
    return isShutdown;
  }

  @Override
  public boolean isTerminated() {
    return isShutdown;
  }

  @Override
  public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    while
    return false;
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Callable<T> task) {
    FutureTask<T> futureTask = new FutureTask<>(task);
    addTask(futureTask);
    return futureTask;
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Runnable task, T result) {
    addTask(task);
    return new FutureTask<>(task, result);
  }

  @NotNull
  @Override
  public Future<?> submit(@NotNull Runnable task) {
    addTask(task);
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

  @NotNull
  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return null;
  }

  @Override
  public void execute(@NotNull Runnable command) {
    addTask(command);
  }

  private class Worker extends Thread {
    @Override
    public void run() {
      try {
        while (!isShutdown) {
          Runnable task = taskPool.poll(POOL_DELAY, TimeUnit.SECONDS);
          if (task == null) {
            break;
          }
          task.run();
        }
      } catch (InterruptedException ignored) {
      } finally {
        submitExpiredWorker(this);
      }
    }
  }

  private void submitExpiredWorker(Worker worker) {
    workers.remove(worker);
  }

  private void submitNewWorker() {
    var worker = new Worker();
    worker.start();
    workers.add(worker);
  }

  private void addTask(Runnable task) {
    boolean rez = taskPool.offer(task);
    if (rez) {
      return;
    }

    if (workers.size() < nThreads) {
      synchronized (workers) {
        if (workers.size() < nThreads) {
          submitNewWorker();
        }
      }
    }
    taskPool.add(task);
  }
}
