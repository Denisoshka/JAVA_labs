package javachar.server.server_model;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ExchangeService implements Runnable, Consumer<Socket>, AutoCloseable {
  private final ExecutorService handleEventsService;
  private final Connection[] connections;
  private final int maxHandledUsersQuantity;
  private final BlockingQueue<Integer> emptySockets;
  private final BlockingQueue<Integer> busySockets;
  private final long timeout;
  private final TimeUnit unit;
  private final Semaphore semaphore;
  private final int maxHandleEventsQuantity;

  public ExchangeService(int maxHandleEventsQuantity, int maxHandledUsersQuantity, long timeout,
                         TimeUnit unit) {
    this.maxHandleEventsQuantity = maxHandleEventsQuantity;
    this.maxHandledUsersQuantity = maxHandledUsersQuantity;
    this.timeout = timeout;
    this.unit = unit;

    this.handleEventsService = Executors.newFixedThreadPool(maxHandleEventsQuantity);
    this.connections = new Connection[this.maxHandledUsersQuantity];
    this.semaphore = new Semaphore(this.maxHandledUsersQuantity);
    this.emptySockets = new ArrayBlockingQueue<>(this.maxHandledUsersQuantity);
    this.busySockets = new ArrayBlockingQueue<>(this.maxHandledUsersQuantity);
    for (int i = 0; i < this.maxHandledUsersQuantity; ++i) {
      emptySockets.add(i);
    }
  }

  @Override
  public void run() {

  }

  @Override
  public void accept(Socket socket) {
  }

  @Override
  public void close() throws Exception {
    for (var conn : connections) {
      conn.close();
    }
  }

  public static class Connection implements AutoCloseable, Runnable {
    private final List<Connection> connections;
    private final Socket socket;

    public Connection(Socket socket, List<Connection> connections) {
      this.connections = connections;
      this.socket = socket;
    }

    @Override
    public void run() {

    }

    @Override
    public void close() throws Exception {
      socket.close();
    }
  }

  private class InstallConnection implements Runnable {
    @Override
    public void run() {
      try {
        semaphore.acquire(maxHandleEventsQuantity);

      } catch (InterruptedException e) {

//        todo
        throw new RuntimeException(e);
      } finally {
        semaphore.release(maxHandledUsersQuantity);
      }
    }
  }

  private class TearConnection implements Runnable {
    @Override
    public void run() {
      try {
        semaphore.acquire(maxHandleEventsQuantity);

      } catch (InterruptedException e) {

//        todo
        throw new RuntimeException(e);
      } finally {
        semaphore.release(maxHandledUsersQuantity);
      }
    }
  }


}
