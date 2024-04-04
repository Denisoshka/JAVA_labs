package javachar.server.server_model;

import javachar.server.exceptions.UnableToCreateConnection;
import javachar.server.exceptions.UnableToCreateServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ConnectionService implements AutoCloseable, Runnable, MessageSendInterface {
  private final static String COMMAND_TAG = "command";
  private final static String COMMAND_NAME_ATTR = "name";
  private final static String IP_ADDRESS = "ip_adress";
  private final static String MAX_CONNECTIONS_QUANTITY = "max_connections_quantity";
  private final static String MAX_HANDLED_CONNECTIONS_QUANTITY = "max_handled_connections_quantity";
  private final static String MAX_HANDLED_EVENTS_QUANTITY = "max_handled_events_quantity";
  private final static String PORT = "port";
  private final static String CONNECTION_TIMEOUT = "connection_timeout";
  private final static String CONNECTION_TIMEUNIT = "connection_timeunit";
  private final static String FAILED_CONNECTION = "<error><message>REASON</message></error>";

  private final Properties properties;
  private final ServerSocket srvSocket;
  private final ExecutorService handleConnectionService;
  private final List<Connection> connections;

  private final int maxHandleConnectionsQuantity;
  private final int maxHandleConnectionsEventsQuantity;
  private final int maxHandleEventsQuantity;

  private final TimeUnit timeoutUnit;
  private final long timeout;

  private final Semaphore ConnectionsOnAction;
  private final Semaphore connectionsAvalibe;

  public ConnectionService(Properties properties) {
    String ipAddress;
    int connectionsQuantity;
    int handleConnectionsEventsQuantity;
    int eventsQuantity;
    int port;
    long timeout;
    TimeUnit unit;

    this.properties = properties;
    try {
      handleConnectionsEventsQuantity = Integer.parseInt(properties.getProperty(MAX_HANDLED_CONNECTIONS_QUANTITY));
      connectionsQuantity = Integer.parseInt(properties.getProperty(MAX_CONNECTIONS_QUANTITY));
      eventsQuantity = Integer.parseInt(properties.getProperty(MAX_HANDLED_EVENTS_QUANTITY));
      port = Integer.parseInt(properties.getProperty(PORT));
      ipAddress = properties.getProperty(IP_ADDRESS);
      timeout = Long.getLong(properties.getProperty(CONNECTION_TIMEOUT));
      unit = TimeUnit.valueOf(properties.getProperty(CONNECTION_TIMEUNIT));
      this.handleConnectionService = Executors.newFixedThreadPool(eventsQuantity);
    } catch (NumberFormatException e) {
      throw new UnableToCreateServer("incorrect config file", e);
    }
    this.maxHandleConnectionsQuantity = connectionsQuantity;
    this.maxHandleEventsQuantity = eventsQuantity;
    this.maxHandleConnectionsEventsQuantity = handleConnectionsEventsQuantity;
    this.timeout = timeout;
    this.timeoutUnit = unit;

    this.connections = new ArrayList<>(maxHandleConnectionsQuantity);
    this.connectionsAvalibe = new Semaphore(maxHandleConnectionsQuantity, true);
    this.ConnectionsOnAction = new Semaphore(maxHandleEventsQuantity, true);

    try {
      this.srvSocket = new ServerSocket();
      this.srvSocket.bind(new InetSocketAddress(ipAddress, port));
    } catch (IOException e) {
      handleConnectionService.shutdownNow();
      throw new UnableToCreateServer("on port: " + properties.getProperty(PORT), e);
    }
  }

  @Override
  public void close() throws Exception {
    handleConnectionService.shutdownNow();
    for (var sock : connections) {
      sock.close();
    }
    srvSocket.close();
  }

  @Override
  public void run() {
    while (true) {
      Socket clSock = null;
      try {
        clSock = srvSocket.accept();
        handleConnectionService.submit(new ConnectionAccepter(clSock));
      } catch (IOException ignored) {

        try {
          clSock.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  private class ConnectionAccepter implements Runnable {
    private final Socket clSock;
    private Integer connectionNum = null;

    public ConnectionAccepter(Socket clSock) {
      this.clSock = clSock;
    }

    @Override
    public void run() {
      boolean connectionFailed = false;
      String commandName = null;

      try (DataOutputStream outStream = new DataOutputStream(clSock.getOutputStream());
           DataInputStream inStream = new DataInputStream(clSock.getInputStream())) {
        int len = inStream.readInt();
        byte[] buffer = inStream.readNBytes(len);
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(buffer, 0, buffer.length));
        NodeList commandElements = document.getElementsByTagName(COMMAND_TAG);
        if (commandElements.getLength() > 0) {
          Element commandElement = (Element) commandElements.item(0);
          commandName = commandElement.getAttribute(COMMAND_NAME_ATTR);
        }

        connectionFailed = Objects.isNull(commandName) || commandName.isEmpty();
        try {
          if (!connectionFailed) {
            connectionFailed = !connectionsAvalibe.tryAcquire(timeout, timeoutUnit);
          }
        } catch (InterruptedException e) {
          return;
        }

        if (connectionFailed) {
          sendConnectionError(outStream);
        } else {
          acceptConnection(clSock);
        }
      } catch (IOException | SAXException | ParserConfigurationException | InterruptedException e) {
//todo
      }
    }
  }

  private void acceptConnection(Socket socket) throws InterruptedException, UnableToCreateConnection {
    try {
      ConnectionsOnAction.acquire(maxHandleEventsQuantity);
      connections.add(new Connection(socket, connections));
    } finally {
      ConnectionsOnAction.release(maxHandleEventsQuantity);
    }
  }

  private void sendConnectionError(DataOutputStream outStream) throws IOException {
    sendMessage(FAILED_CONNECTION, outStream);
  }

  private void tearConnection(int connectionIndex) {
//
  }

}
