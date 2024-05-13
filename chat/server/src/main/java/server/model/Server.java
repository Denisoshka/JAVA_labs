package server.model;

import dto.DTOConverterManager;
import dto.RequestDTO;
import dto.exceptions.UnableToSerialize;
import dto.subtypes.LoginDTO;
import io_processing.IOProcessor;
import org.slf4j.Logger;
import org.w3c.dom.Node;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.SectionFactory;
import server.model.server_sections.interfaces.CommandSupplier;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server implements Runnable {
  private static final String IP_ADDRESS = "ip_address";
  private static final String PORT = "port";
  private static final long MAX_CONNECTION_TIME = 60_000;
  private static final long DELETER_DELAY = 30_000;
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Server.class);

  public ConcurrentHashMap<String, Integer> getRegisteredUsers() {
    return registeredUsers;
  }

  private final ConcurrentHashMap<String, Integer> registeredUsers = new ConcurrentHashMap<>();
  private final ConcurrentHashMap.KeySetView<ServerConnection, Boolean> expiredConnections = ConcurrentHashMap.newKeySet();
  private final CopyOnWriteArrayList<ServerConnection> connections = new CopyOnWriteArrayList<>();
  private final DTOConverterManager converterManager;
  private final CommandSupplier commandSupplier;
  private final ServerSocket serverSocket;

  //  todo

  private final ExecutorService expiredConnectionDeleter;
  private final ExecutorService connectionAcceptExecutor;
  
  private AtomicBoolean serveryPizda = new AtomicBoolean(false);

  Server(Properties properties) throws IOException {
    serverSocket = new ServerSocket();
    try {
      serverSocket.bind(
              new InetSocketAddress(Objects.requireNonNull(properties.getProperty(IP_ADDRESS)),
                      Integer.parseInt(Objects.requireNonNull(properties.getProperty(PORT))))
      );
    } catch (IOException e) {
      serverSocket.close();
    }
    this.converterManager = new DTOConverterManager(null /*todo fix this*/);
    this.commandSupplier = new SectionFactory(this);
    this.connectionAcceptExecutor = Executors.newFixedThreadPool(2);
    this.expiredConnectionDeleter = Executors.newSingleThreadExecutor();
  }

  @Override
  public void run() {
    expiredConnectionDeleter.submit(new ConnectionDeleter(this));

    try {
      while (!Thread.currentThread().isInterrupted()) {
        Socket socket = serverSocket.accept();
      }

    } catch (InterruptedException e) {
//      todo handle ex
    }
  }

  private static class ServerWorker implements Runnable {
    private final ServerConnection connection;
    private final DTOConverterManager XMLDTOConverterManager;
    private final CommandSupplier commandSupplier;
    private final Server server;

    public ServerWorker(ServerConnection connection, Server server) {
      this.server = server;
      this.connection = connection;
      this.commandSupplier = server.commandSupplier;
      this.XMLDTOConverterManager = server.converterManager;
    }

    @Override
    public void run() {
      try (ServerConnection con = connection) {
        while (!connection.isClosed() && !Thread.currentThread().isInterrupted()) {
          final Node xmlTree = XMLDTOConverterManager.getXMLTree(con.receiveMessage());
          final RequestDTO.DTO_TYPE dtoType = XMLDTOConverterManager.getDTOType(xmlTree);
          final RequestDTO.DTO_SECTION dtoSection = XMLDTOConverterManager.getDTOSection(xmlTree);

          if (dtoType == null || dtoSection == null) {
//            todo make in XMLDTOConverterManager support of base commands
            connection.sendMessage(XMLDTOConverterManager.serialize(
                    new RequestDTO.BaseErrorResponse(null, "unhandled message in server protocol")
            ).getBytes());
          } else {
            commandSupplier.getSection(dtoSection).perform(connection, xmlTree, dtoType, dtoSection);
          }
        }
      } catch (IOException _) {
      } finally {
        server.submitExpiredConnection(connection);
      }
    }
  }

  private static class ConnectionDeleter implements Runnable {
    CopyOnWriteArrayList<ServerConnection> connections;
    ConnectionDeleter(Server server) {
      connections = server.connections;
    }

    @Override
    public void run() {
      Thread.currentThread().setDaemon(true);
      try {
        while (!Thread.currentThread().isInterrupted()) {
          List<ServerConnection> exConnections = connections.stream().
                  filter(ServerConnection::isExpired).toList();
          exConnections.forEach(connection -> {
            try {
              connection.close();
            } catch (IOException _) {
            }
          });
          connections.removeAll(exConnections);
          Thread.sleep(DELETER_DELAY);
        }
      } catch (InterruptedException _) {
      } catch (Exception e) {
        log.warn(e.getMessage());
      }
    }
  }

  public void submitExpiredConnection(ServerConnection connection) {
    connection.markAsExpired();
    expiredConnections.add(connection);
  }

  public void submitNewConnection(ServerConnection connection) {
    connections.add(connection);
  }

  public CopyOnWriteArrayList<ServerConnection> getConnections() {
    return connections;
  }

  public AtomicBoolean getServeryPizda() {
    return serveryPizda;
  }

  public ConcurrentHashMap.KeySetView<ServerConnection, Boolean> getExpiredConnections() {
    return expiredConnections;
  }

  public DTOConverterManager getConverterManager() {
    return converterManager;
  }

  public static Logger getLog() {
    return Server.log;
  }
}
