package ru.nsu.zhdanov.lab_4.thread_pool;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class CustomFixedThreadPool implements ExecutorService {
  private final BlockingQueue<Runnable> taskPool;
  private volatile boolean shutdowned = false;
  private volatile int activeTaskQ = 0;
  private final HashSet<Worker> workers;
  private final int nThreads;
  private final ReentrantLock mainLock = new ReentrantLock();

  public CustomFixedThreadPool(int nThreads) {
    this(nThreads, new LinkedBlockingQueue<Runnable>());
  }

  public CustomFixedThreadPool(int nThreads, BlockingQueue<Runnable> taskPool) {
    this.taskPool = taskPool;
    this.nThreads = nThreads;
    this.workers = new HashSet<>(nThreads);
  }

  private class Worker extends Thread {
    @Override
    public void run() {
      try {
        while (true) {
          Runnable task = taskPool.poll(180, TimeUnit.SECONDS);
          if (task == null) {
            break;
          }
          task.run();
        }
      } catch (InterruptedException ignored) {
      } finally {
        workerExpired(this);
      }
    }
  }

  private void workerExpired(Worker worker) {
    synchronized (workers) {
      workers.remove(worker);
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
    return shutdowned;
  }

  @Override
  public boolean isTerminated() {
    return shutdowned;
  }

  @Override
  public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    return false;
  }

  @NotNull
  @Override
  public <T> Future<T> submit(@NotNull Callable<T> task) {
    if (activeTaskQ == nThreads) {
//      taskPool.put();
    }

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
    if (task == null) {
      throw new NullPointerException();
    }
    RunnableFuture<Void> futureTask = new FutureTask<>(task, null);
    execute(futureTask);
    return futureTask;
  }

  private Future<?> handleSubmitArgs(){
    return null;
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
    taskPool.add(command);
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
