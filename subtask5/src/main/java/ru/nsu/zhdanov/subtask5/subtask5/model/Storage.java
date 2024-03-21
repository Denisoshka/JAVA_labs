package ru.nsu.zhdanov.subtask5.subtask5.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Storage<T> implements Supplier<T>, Consumer<T> {
  //  final private ArrayList<T> storage;
//  final private int capacity;
//  final ReentrantLock lock = new ReentrantLock();
//  final Condition nonEmpty = lock.newCondition();
//  final Condition nonFull = lock.newCondition();
  private final BlockingQueue<T> storage;

  public Storage(int capacity) {
    this.storage = new ArrayBlockingQueue<>(capacity);
  }


  @Override
  public T get() {
    try {
      return storage.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void accept(T t) {
    try {
      storage.put(t);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}