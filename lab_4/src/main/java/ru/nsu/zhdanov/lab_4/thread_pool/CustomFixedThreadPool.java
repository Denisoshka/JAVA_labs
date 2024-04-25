package ru.nsu.zhdanov.lab_4.thread_pool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomFixedThreadPool implements ExecutorService {
  public final static int POOL_DELAY = 180;
  private final String namePrefix;

  private final ReentrantLock lock = new ReentrantLock();
  private final Condition termination = lock.newCondition();

  private final BlockingQueue<Runnable> taskPool;
  private final Set<Worker> workers;
  private final int nThreads;

  private volatile boolean isShutdown = false;

  public CustomFixedThreadPool(int nThreads) {
    this(nThreads, new LinkedBlockingQueue<>());
  }

  public CustomFixedThreadPool(int nThreads, BlockingQueue<Runnable> taskPool) {
    this.namePrefix = "custom pool-" + Integer.toHexString(this.hashCode()) + "-thread-";
    this.workers = ConcurrentHashMap.newKeySet(nThreads);
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
    shutdown();
    if (!isTerminated()) {
      lock.lock();
      try {
        if (!isTerminated()) {
          for (var worker : workers) {
            worker.interrupt();
          }
        }
      } finally {
        lock.unlock();
      }
    }
    return taskPool.stream().toList();
  }

  @Override
  public boolean isShutdown() {
    return isShutdown;
  }

  @Override
  public boolean isTerminated() {
    return isShutdown && workers.isEmpty();
  }

  @Override
  public boolean awaitTermination(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    long duration = unit.toMillis(timeout);
    long start = System.currentTimeMillis();
    long end = start + duration;
    lock.lock();
    try {
      for (long cur = System.currentTimeMillis(); !workers.isEmpty() && end - cur > 0; cur = System.currentTimeMillis()) {
        termination.await(end - cur, TimeUnit.MILLISECONDS);
      }
    } finally {
      lock.unlock();
    }
    return workers.isEmpty();
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
    return invokeAll(tasks, Integer.MAX_VALUE, TimeUnit.DAYS);
  }

  @NotNull
  @Override
  public <T> List<Future<T>> invokeAll(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException {
    List<Future<T>> futures = new ArrayList<>(tasks.size());
    tasks.forEach(callbl -> futures.add(submit(callbl)));

    long duration = unit.toMillis(timeout);
    long start = System.currentTimeMillis();
    long end = start + duration;

    try {
      for (var fut : futures) {
        long cur = System.currentTimeMillis();
        try {
          if (end - cur > 0) fut.get(end - cur, TimeUnit.MILLISECONDS);
          else break;
        } catch (ExecutionException | CancellationException ignored) {
        }
      }
    } catch (TimeoutException ignored) {
      futures.forEach(future -> future.cancel(true));
    }
    return futures;
  }

  @NotNull
  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
    try {
      return invokeAny(tasks, Long.MAX_VALUE, TimeUnit.DAYS);
    } catch (TimeoutException e) {
      throw new ExecutionException(e);
    }
  }

  @NotNull
  @Override
  public <T> T invokeAny(@NotNull Collection<? extends Callable<T>> tasks, long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    ExecutorCompletionService<T> cs = new ExecutorCompletionService<>(this);
    List<Future<T>> futures = new ArrayList<>(tasks.size());
    tasks.forEach(task -> futures.add(cs.submit(task)));

    long duration = unit.toMillis(timeout);
    long start = System.currentTimeMillis();
    long end = start + duration;

    try {
      for (long cur = System.currentTimeMillis(); cur < end; cur = System.currentTimeMillis()) {
        var future = cs.poll(end - cur, TimeUnit.MILLISECONDS);
        if (future != null) return future.get();
      }
    } finally {
      futures.forEach(tFuture -> tFuture.cancel(true));
    }

    throw new TimeoutException();
  }

  @Override
  public void execute(@NotNull Runnable command) {
    if (command == null) throw new NullPointerException();
    addTask(command);
  }

  private class Worker extends Thread {
    @Override
    public void run() {
      try {
        while (!isShutdown && !Thread.currentThread().isInterrupted()) {
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

  private void submitNewWorker() {
    var worker = new Worker();
    worker.setName(namePrefix + worker.hashCode());
    worker.start();
    workers.add(worker);
  }

  private void addTask(Runnable task) {
    if (!isShutdown && (workers.isEmpty() || !taskPool.isEmpty() && workers.size() < nThreads)) {
      lock.lock();
      try {
        if (!isShutdown && (workers.isEmpty() || !taskPool.isEmpty() && workers.size() < nThreads)) {
          submitNewWorker();
        }
      } finally {
        lock.unlock();
      }
    }
    if (isShutdown || !taskPool.offer(task)) throw new RejectedExecutionException();
  }

  private void submitExpiredWorker(Worker worker) {
    lock.lock();
    try {
      workers.remove(worker);
      termination.signalAll();
    } finally {
      lock.unlock();
    }
  }
}
