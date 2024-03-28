package ru.nsu.zhdanov.subtask9.implementation;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

public class Task implements AutoCloseable {
  private static int minDelay = 3000;
  private static int maxAllowance = 2000;
  private static int workDelay = 1300;

  private final Semaphore semaphore;
  private static final Random random = new Random();
  private final ExecutorService executor;


  public Task(int subtasksQuantity, int permissionsQuantity) {
    semaphore = new Semaphore(permissionsQuantity);
    this.executor = Executors.newFixedThreadPool(subtasksQuantity);
    for (int i = 0; i < subtasksQuantity; ++i) {
      executor.submit(new Subtask(i));
    }
  }

  @Override
  public void close() throws Exception {
    executor.shutdownNow();
  }

  private class Subtask implements Runnable {
    private int number;

    public Subtask(int n) {
      number = n;
    }

    @Override
    public void run() {
      String workerName = "worker " + number + ":";
      while (!Thread.currentThread().isInterrupted()) {
        try {
//          System.out.println(workerName + " work for " + workDelay);
          Thread.sleep(workDelay);
        } catch (InterruptedException e) {
          System.out.println(workerName + " finish work");
          return;
        }


        try {
          semaphore.acquire();
          int delay = minDelay + random.nextInt(maxAllowance);
          System.out.println(workerName + " entered in rest room " + delay);
          Thread.sleep(delay);
          System.out.println(workerName + " leave rest room " + delay);
        } catch (InterruptedException e) {
          System.out.println(workerName + " finish work");
          return;
        } finally {
          semaphore.release();
        }
      }
    }
  }
}
