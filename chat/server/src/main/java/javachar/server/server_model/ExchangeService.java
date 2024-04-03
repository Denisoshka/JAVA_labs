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

  public class Connection implements AutoCloseable, Runnable {
    private final static String COMMAND_TAG = "command";
    private final static String COMMAND_NAME_ATTR = "name";


    public final static int BUFFERSIZE = 41_943_040;
    private final List<Connection> connections;
    private final Socket socket;
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    private final DocumentBuilder builder = factory.newDocumentBuilder();
    private DataOutputStream outStream;
    private DataInputStream inStream;
    Integer connectionNum = null;


    public Connection(Socket socket, List<Connection> connections) throws ParserConfigurationException {
      this.connections = connections;
      this.socket = socket;
//      this.buffer = new byte[BUFFERSIZE];
    }

    @Override
    public void run() {
      try (DataOutputStream localOStream = new DataOutputStream(socket.getOutputStream());
           DataInputStream localIStream = new DataInputStream(socket.getInputStream())) {
        outStream = localOStream;
        inStream = localIStream;

        installConnection(socket);


      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        outStream = null;
        inStream = null;
      }
    }

    private void writeMessage(String message, DataOutputStream outStream) throws IOException {
      int msgLen = message.getBytes().length;
      outStream.write(msgLen);
      outStream.write(message.getBytes(), 0, msgLen);
    }

    private Integer installConnection(Socket socket) {
      String commandName = null;
      boolean connectionFailed = false;
      try {
        int len = inStream.readInt();
        byte[] buffer = inStream.readNBytes(len);
        Document document = builder.parse(new ByteArrayInputStream(buffer, 0, buffer.length));
        NodeList commandElements = document.getElementsByTagName(COMMAND_TAG);

        if (commandElements.getLength() > 0) {
          Element commandElement = (Element) commandElements.item(0);
          commandName = commandElement.getAttribute(COMMAND_NAME_ATTR);
        }
        if (!Objects.nonNull(commandName) || commandName.isEmpty()) {
          connectionFailed = true;
        }
        if (!connectionFailed && Objects.isNull(connectionNum = emptySockets.poll(timeout, unit))) {
          String msg = unableToConnect("failed connection");
          outStream.write(msg.getBytes().length);
          return null;
        }

        try {
          semaphore.acquire(maxHandleEventsQuantity);
          if (connectionNum == null) {
            String msg = unableToConnect("failed connection");
            outStream.write(msg.getBytes().length);
            return null;
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        } finally {
          semaphore.release(maxHandledUsersQuantity);
        }
      } catch (IOException | SAXException | InterruptedException e) {
        return null;

        throw new RuntimeException("pizda");
      }
    }

    @Override
    public void close() throws Exception {
      socket.close();
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

  private String unableToConnect(String reason) {
    return "<error><message>" + reason + "</message></error>";
  }
}
