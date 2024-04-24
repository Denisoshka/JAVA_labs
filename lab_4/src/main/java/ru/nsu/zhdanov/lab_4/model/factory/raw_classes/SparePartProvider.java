package ru.nsu.zhdanov.lab_4.model.factory.raw_classes;

import org.slf4j.Logger;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.MonitorListenerIntroduction;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartConsumer;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartFactoryInterface;
import ru.nsu.zhdanov.lab_4.model.factory.interfaces.SparePartModelMonitorListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SparePartProvider<SparePartT> implements MonitorListenerIntroduction {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(SparePartProvider.class);
  protected SparePartConsumer<SparePartT> repository;
  private final SparePartFactoryInterface factory;

  private volatile int delay;
  ;
  private final ExecutorService executorService;
  private final SparePartType sparePartType;
  private final int providersQuantity;
  private final AtomicInteger totalProduced = new AtomicInteger(0);
  private final List<SparePartModelMonitorListener> listeners = new ArrayList<>(1);

  public SparePartProvider(final SparePartFactoryInterface factory, final SparePartType type, final int providersQuantity, final int delay) {
    this.executorService = Executors.newFixedThreadPool(providersQuantity);
    this.providersQuantity = providersQuantity;
    this.sparePartType = type;
    this.factory = factory;
    this.delay = delay;
  }

  public void perform() {
    Runnable task = () -> {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          SparePartT sparePart = (SparePartT) factory.make(sparePartType.toString());
          log.trace("spare part " + sparePartType + " " + Integer.toHexString(sparePart.hashCode()));
          onSparePartProduced();
          Thread.sleep(delay);
          repository.acceptSparePart(sparePart);
        }
      } catch (InterruptedException ignored) {
      } catch (Exception e) {
        log.warn("unexpected exception " + e.getMessage());
      } finally {
        log.trace("interrupted");
      }
    };
    for (int i = 0; i < providersQuantity; i++) {
      executorService.submit(task);
    }
  }

  public void shutdownNow() {
    executorService.shutdownNow();
  }

  private void onSparePartProduced() {
    String condition = String.valueOf(totalProduced.incrementAndGet());
    for (var listener : listeners) listener.changed(condition);
  }

  @Override
  public void addProduceMonitorListener(SparePartModelMonitorListener listener) {
    listeners.add(listener);
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }

  public SparePartType getSparePartType() {
    return sparePartType;
  }

  public void setRepository(SparePartConsumer<SparePartT> repository) {
    this.repository = repository;
  }
}