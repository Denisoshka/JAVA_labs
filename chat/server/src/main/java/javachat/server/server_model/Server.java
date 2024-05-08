package javachat.server.server_model;

import javachat.server.exceptions.UnableToDecodeMessage;
import javachat.server.exceptions.UnableToRegisterUser;
import javachat.server.server_model.message_handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server implements Runnable, AutoCloseable {
  private static final String IP_ADDRESS = "ip_address";
  private static final String PORT = "port";
  private static final long MAX_CONNECTION_TIME = 60_000;

  private final ConcurrentHashMap.KeySetView<Connection, Boolean> expiredConnections = ConcurrentHashMap.newKeySet();
  private final CopyOnWriteArrayList<Connection> connections = new CopyOnWriteArrayList<>();

  private final ServerSocket serverSocket;
  private final MessageHandler messageHandler;
  private final ExecutorService expiredConnectionDeleter;
  private final ExecutorService chatExchangeExecutor;
  private final ExecutorService connectionAcceptExecutor;
  private final Properties registeredUsers;

  Server(Properties properties) throws TransformerConfigurationException, ParserConfigurationException, IOException {
    String ipadress;
    int port;
    try {
      ipadress = Objects.requireNonNull(properties.getProperty(IP_ADDRESS));
      port = Integer.parseInt(Objects.requireNonNull(properties.getProperty(PORT)));
      serverSocket = new ServerSocket();
      serverSocket.bind(new InetSocketAddress(ipadress, port));
    } catch (NumberFormatException | NullPointerException | IOException e) {
      throw e;
//      todo make custom exc
    }

    this.registeredUsers = new Properties();
    this.messageHandler = new MessageHandler(this);
    this.chatExchangeExecutor = Executors.newCachedThreadPool();
    this.connectionAcceptExecutor = Executors.newFixedThreadPool(2);
    this.expiredConnectionDeleter = Executors.newSingleThreadExecutor();
  }

  @Override
  public void run() {
    while (!serverSocket.isClosed()) {
      try {
        Socket clSocket = serverSocket.accept();
        connectionAcceptExecutor.execute(new ConnectionAccepter(clSocket, this));
      } catch (IOException e) {
        log.info("Error received while wait connection");
//      todo need to handle accept errors;
        return;
      }
    }
  }

  public void submitExpiredConnection(Connection connection) {
    connection.markAsExpired();
    expiredConnections.add(connection);
  }

  private void submitNewConnection(Connection connection) {
    connections.add(connection);
    chatExchangeExecutor.execute(connection);
  }

  public RegistrationState registerUser(String name, String password) throws UnableToRegisterUser {
    if (name == null || name.isEmpty()) return RegistrationState.INCORRECT_NAME_DATA;
    if (password == null || password.isEmpty()) return RegistrationState.INCORRECT_PASSWORD_DATA;
    String pswrd;
    if (null == (pswrd = registeredUsers.getProperty(name))) {
      registeredUsers.setProperty(name, String.valueOf(password.hashCode()));
      return RegistrationState.SUCCESS;
    }
    if (pswrd.hashCode() == password.hashCode()) return RegistrationState.SUCCESS;
    return RegistrationState.INCORRECT_PASSWORD;
  }

  @Override
  public void close() throws Exception {
    try {
      serverSocket.close();
    } catch (IOException ignored) {
    }
    expiredConnectionDeleter.shutdownNow();
    chatExchangeExecutor.shutdownNow();
    connectionAcceptExecutor.shutdownNow();
    for (var conn : connections) {
      try {
        conn.close();
      } catch (IOException ignored) {
      }
    }
    for (var conn : expiredConnections) {
      try {
        conn.close();
      } catch (IOException ignored) {
      }
    }
  }

  private class ConnectionAccepter implements Runnable {
    private final Socket socket;
    private final Server server;

    public ConnectionAccepter(Socket socket, Server server) {
      this.socket = socket;
      this.server = server;
    }

    @Override
    public void run() {
//    todo очень смущает разделение на исполение подключения и иные команды
      long start = System.currentTimeMillis();
      long end = 0;
      String name = null;
      try (DataInputStream receiveStream = new DataInputStream(socket.getInputStream());
           DataOutputStream sendStream = new DataOutputStream(socket.getOutputStream())) {
        for (; end - start <= MAX_CONNECTION_TIME; end = System.currentTimeMillis()) {
          try {
            Document doc = messageHandler.receiveMessage(receiveStream);
            RegistrationState ret = messageHandler.handleConnectionMessage(doc);
            if (ret == RegistrationState.SUCCESS) {
              messageHandler.sendMessage(sendStream, ServerMSG.getSuccess());
              end = System.currentTimeMillis();
              name = messageHandler.getConnectionName(doc);
              break;
            } else {
              messageHandler.sendMessage(sendStream, ServerMSG.getError(ret.getDescription()));
            }
          } catch (UnableToDecodeMessage e) {
            messageHandler.sendMessage(sendStream, ServerMSG.getError(e.getMessage()));
          }
        }
        if (end - start <= MAX_CONNECTION_TIME) {
          String msg = ServerMSG.getUserLogin(name);
          messageHandler.sendBroadcastMessage(msg);
          messageHandler.sendMessage(sendStream, ServerMSG.getSuccess());
        } else {
          messageHandler.sendMessage(sendStream, ServerMSG.getError(RegistrationState.TIMEOUT_EXPIRED.getDescription()));
        }
      } catch (IOException e) {
        try {
          socket.close();
        } catch (IOException ignored) {
        }
      }
      if (!socket.isClosed()) submitNewConnection(new Connection(socket, server, messageHandler, name));
    }
  }

  public CopyOnWriteArrayList<Connection> getConnections() {
    return connections;
  }

  public String getSessionName() {
    return "xyi";
  }
}