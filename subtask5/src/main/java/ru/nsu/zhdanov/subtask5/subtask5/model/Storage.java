package ru.nsu.zhdanov.subtask5.subtask5.model;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Storage<T> implements Supplier<T>, Consumer<T> {
  private int capacity;
  private int occupancy;
  private ArrayList<T> storage;
  ReentrantLock lock;
  ReentrantLock.Con


  @Override
  public T get() {
    return null;
  }

  @Override
  public void accept(T t) {

  }
}