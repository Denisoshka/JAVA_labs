package javachar.server.server_model;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ExchangeService implements Runnable, Consumer<Socket>, AutoCloseable {
  private final ExecutorService handleEventsService;
  private final int maxHandledUsersQuantity;
  private final BlockingQueue<Integer> busySockets;
  private final long timeout;
  private final TimeUnit unit;
  private final Semaphore charngeConnections;
  private final int maxHandleEventsQuantity;

  public ExchangeService(int maxHandleEventsQuantity, int maxHandledUsersQuantity, long timeout,
                         TimeUnit unit) {
    this.maxHandleEventsQuantity = maxHandleEventsQuantity;
    this.maxHandledUsersQuantity = maxHandledUsersQuantity;
    this.timeout = timeout;
    this.unit = unit;

    this.handleEventsService = Executors.newFixedThreadPool(maxHandleEventsQuantity);
    this.charngeConnections = new Semaphore(this.maxHandledUsersQuantity);
    this.busySockets = new ArrayBlockingQueue<>(this.maxHandledUsersQuantity);
  }
  
  @Override
  public void run() {

  }

  @Override
  public void accept(Socket socket) {

  }

  @Override
  public void close() throws Exception {
//    todo
  }


  private class TearConnection implements Runnable {
    @Override
    public void run() {
      try {
        charngeConnections.acquire(maxHandleEventsQuantity);

      } catch (InterruptedException e) {

//        todo
        throw new RuntimeException(e);
      } finally {
        charngeConnections.release(maxHandledUsersQuantity);
      }
    }
  }
}
