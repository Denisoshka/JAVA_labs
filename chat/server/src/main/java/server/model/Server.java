package server.model;

import dto.DTOConverterManager;
import org.slf4j.Logger;
import server.model.connection_section.ChatUsersDAO;
import server.model.connection_section.ConnectionAccepter;
import server.model.connection_section.ServerConnection;
import server.model.connection_section.ServerWorker;
import server.model.server_sections.SectionFactory;
import server.model.server_sections.interfaces.CommandSupplier;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
  private static final String IP_ADDRESS = "ip_address";
  private static final String PORT = "port";
  private static final long DELETER_DELAY = 60_000;
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Server.class);
  private final Logger moduleLogger = org.slf4j.LoggerFactory.getLogger("moduleLogger");

  private final ChatUsersDAO chatUsersDAO = new ChatUsersDAO();
  private final ConcurrentHashMap.KeySetView<ServerConnection, Boolean> expiredConnections = ConcurrentHashMap.newKeySet();
  private final CopyOnWriteArrayList<ServerConnection> connections = new CopyOnWriteArrayList<>();
  private final DTOConverterManager converterManager;
  private final CommandSupplier commandSupplier;

  private final ServerSocket serverSocket;

  private final ExecutorService connectionsAccepterExecutor = Executors.newFixedThreadPool(2);
  private final ExecutorService connectionsPool = Executors.newCachedThreadPool();
  private final ExecutorService expiredConnectionsDeleter = Executors.newSingleThreadExecutor();

  public Server(String host, String port) throws IOException {
    serverSocket = new ServerSocket();
    try {
      serverSocket.bind(new InetSocketAddress(host,  Integer.parseInt(port)));
    } catch (IOException e) {
      serverSocket.close();
    }
    this.converterManager = new DTOConverterManager(null /*todo fix this*/);
    this.commandSupplier = new SectionFactory(this);
  }

  @Override
  public void run() {
    expiredConnectionsDeleter.execute(new ConnectionDeleter(this));
    try {
      while (!Thread.currentThread().isInterrupted()) {
        Socket socket = serverSocket.accept();
        log.info("new connection from {}", socket.getRemoteSocketAddress());
        connectionsAccepterExecutor.execute(new ConnectionAccepter(this, socket));
      }
    } catch (IOException e) {
      log.warn(e.getMessage(), e);
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
      expiredConnectionsDeleter.shutdownNow();
      connectionsAccepterExecutor.shutdownNow();
      connectionsPool.shutdownNow();
    }
  }


  private static class ConnectionDeleter implements Runnable {

    CopyOnWriteArrayList<ServerConnection> connections;

    ConnectionDeleter(Server server) {
      connections = server.connections;
    }

    @Override
    public void run() {
      try {
        while (!Thread.currentThread().isInterrupted()) {
          List<ServerConnection> exConnections = connections.stream().filter(ServerConnection::isExpired).toList();
          exConnections.forEach(connection -> {
            try {
              connection.close();
            } catch (IOException _) {
            }
          });
          connections.removeAll(exConnections);
          log.info(STR."delete \{exConnections.size()} expired connectinos");
          log.info(STR."Connections quantity:\{connections.size()}");
          Thread.sleep(DELETER_DELAY);
        }
      } catch (InterruptedException _) {
        connections.forEach(connection -> {
          try {
            connection.close();
          } catch (IOException _) {
          }
        });
      } catch (Exception e) {
        log.warn(e.getMessage());
      }
    }

  }

  public void submitExpiredConnection(ServerConnection connection) {
    connection.markAsExpired();
    expiredConnections.add(connection);
    log.info(STR."\{connection} submit expired connection");
  }

  public void submitNewConnection(ServerConnection connection) {
    connections.add(connection);
    connectionsPool.submit(new ServerWorker(connection, this));
    log.info(STR."submit new connection \{connection.getConnectionName()}");
  }

  public CopyOnWriteArrayList<ServerConnection> getConnections() {
    return connections;
  }

  public ConcurrentHashMap.KeySetView<ServerConnection, Boolean> getExpiredConnections() {
    return expiredConnections;
  }

  public DTOConverterManager getConverterManager() {
    return converterManager;
  }

  public CommandSupplier getCommandSupplier() {
    return commandSupplier;
  }

  public ChatUsersDAO getChatUsersDAO() {
    return chatUsersDAO;
  }
}
