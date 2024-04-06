package javachat.server.server_model;

import javachat.server.exceptions.IOServerException;
import javachat.server.exceptions.ServerRegistrationException;
import javachat.server.exceptions.UnableToCreateServer;
import javachat.server.server_model.interfaces.AbstractServer;
import javachat.server.server_model.interfaces.GetMessage;
import javachat.server.server_model.interfaces.MessageSendInterface;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.security.MessageDigest;

public class Server implements AbstractServer, AutoCloseable, Runnable, MessageSendInterface {
  private final static String IP_ADDRESS = "ip_adress";
  private final static String MAX_CONNECTIONS_QUANTITY = "max_connections_quantity";
  private final static String MAX_HANDLED_CONNECTIONS_QUANTITY = "max_handled_connections_quantity";
  private final static String MAX_HANDLED_EVENTS_QUANTITY = "max_handled_events_quantity";
  private final static String PORT = "port";
  private final static String CONNECTION_TIMEOUT = "connection_timeout";
  private final static String CONNECTION_TIMEUNIT = "connection_timeunit";
  private final static String FAILED_CONNECTION = GetMessage.ServerErrorAnswer("connection failed");
  private final static String HASH_TYPE = "SHA-256";

  private ServerSocket srvSocket = null;
  private final ExecutorService connectionAccepterHandlerService;
  private final ExecutorService connectionHandler;
  private final MessageHandler messageHandler;
  private final MessageDigest hash;
  private final Properties properties;
  private final Properties registeredUsers;

  private final List<Connection> brokenConnections;
  private final Set<Connection> connections;

  private final int maxHandleConnectionsQuantity;
  //  private final int maxHandleConnectionsEventsQuantity;
  private final int maxHandleEventsQuantity;

  private final TimeUnit timeoutUnit;
  private final long timeout;

  public Server(Properties properties) {
    String ipAddress;
    int connectionsQuantity;
    int eventsQuantity;
    int port;
    long timeout;
    TimeUnit unit;
    this.properties = properties;
    try {
      connectionsQuantity = Integer.parseInt(properties.getProperty(MAX_CONNECTIONS_QUANTITY));
      eventsQuantity = Integer.parseInt(properties.getProperty(MAX_HANDLED_EVENTS_QUANTITY));
      port = Integer.parseInt(properties.getProperty(PORT));
      ipAddress = properties.getProperty(IP_ADDRESS);
      timeout = Long.getLong(properties.getProperty(CONNECTION_TIMEOUT));
      unit = TimeUnit.valueOf(properties.getProperty(CONNECTION_TIMEUNIT));
    } catch (NumberFormatException e) {
      throw new UnableToCreateServer("incorrect config file", e);
    }
    try {
      this.hash = MessageDigest.getInstance(HASH_TYPE);
      this.messageHandler = new MessageHandler(this);
      this.srvSocket = new ServerSocket();
      this.srvSocket.bind(new InetSocketAddress(ipAddress, port));
    } catch (IOException | NoSuchAlgorithmException e) {
      try {
        if (srvSocket != null) srvSocket.close();
      } catch (IOException ignored) {
      }
      throw new UnableToCreateServer("on port: " + properties.getProperty(PORT), e);
    }

    this.connectionAccepterHandlerService = Executors.newCachedThreadPool();
    this.connectionHandler = Executors.newCachedThreadPool();
    this.maxHandleConnectionsQuantity = connectionsQuantity;
    this.maxHandleEventsQuantity = eventsQuantity;
    this.timeout = timeout;
    this.timeoutUnit = unit;
    this.connections = new HashSet<>();
    this.brokenConnections = new ArrayList<>();
    this.registeredUsers = new Properties();
  }

  @Override
  public void close() throws Exception {
    connectionAccepterHandlerService.shutdownNow();
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
        connectionAccepterHandlerService.submit(new ConnectionAccepter(clSock));
      } catch (IOException ignored0) {
        try {
          clSock.close();
        } catch (IOException ignored1) {
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
        messageHandler.getNewConnection(clSock, outStream, inStream);

        /*if (connectionFailed) {
          sendConnectionError(outStream);
        } else {

          addConnection(clSock);
        }*/
      } catch (IOException e) {
//todo
      }
    }
  }

  public void addConnection(@NotNull Connection conn) {
    Objects.requireNonNull(conn);
    synchronized (connections) {
      connections.add(conn);
    }
    connectionHandler.submit(conn);
  }

  public boolean registerNewConnection(String name, String password) throws ServerRegistrationException {
    if (password == null || name == null) throw new ServerRegistrationException("incorrect name or password");
    String hash, passwordHash = String.valueOf(password.hashCode());
    if (null == (hash = registeredUsers.getProperty(name))) {
      registeredUsers.setProperty(name, passwordHash);
      return true;
    }
    return hash.compareTo(passwordHash) == 0;
  }

  private void sendConnectionError(DataOutputStream outStream) throws IOException {
    receiveMessage(FAILED_CONNECTION, outStream);
  }

  @Override
  public void tearConnection(Connection conn) {
    synchronized (connections) {
      connections.remove(conn);
    }
    try (Connection con = conn) {



    } catch (IOServerException e) {
//      todo make log
    } catch (Exception ignored) {
    }
  }

  @Override
  public boolean addBrokenConnection(Connection conn) {
    synchronized (brokenConnections) {
      return brokenConnections.add(conn);
    }
  }

  public Set<Connection> getConnections() {
    return connections;
  }
}
