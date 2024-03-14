package ru.nsu.zhdanov.subtask5.subtask5.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Storage<T> implements Supplier<T>, Consumer<T> {
  final private ArrayList<T> storage;
  final private int capacity;
  final ReentrantLock lock = new ReentrantLock();
  final Condition nonEmpty = lock.newCondition();
  final Condition nonFull = lock.newCondition();

  public Storage(int capacity) {
    this.capacity = capacity;
    this.storage = new ArrayList<>(capacity);
  }


  @Override
  public T get() {
    lock.lock();
    try {
      while (storage.isEmpty()) {
        nonEmpty.await();
      }
      return storage.removeLast();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      nonFull.signalAll();
      lock.unlock();
    }
  }

  @Override
  public void accept(T t) {
    lock.lock();
    try {
      while (storage.size() == capacity) {
        nonFull.await();
      }
      storage.add(t);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      nonEmpty.signalAll();
      lock.unlock();
    }
  }
}