package server.model;

import dto.DTOConverterManager;
import dto.RequestDTO;
import dto.exceptions.UnableToDeserialize;
import dto.interfaces.DTOConverterManagerInterface;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import server.model.io_processing.ServerConnection;
import server.model.server_sections.ConnectionAccepter;
import server.model.server_sections.SectionFactory;
import server.model.server_sections.interfaces.CommandSupplier;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
  private static final long DELETER_DELAY = 30_000;
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Server.class);
  private final Logger moduleLogger = org.slf4j.LoggerFactory.getLogger("moduleLogger");

  private final ConcurrentHashMap<String, Integer> registeredUsers = new ConcurrentHashMap<>();

  private final ConcurrentHashMap.KeySetView<ServerConnection, Boolean> expiredConnections = ConcurrentHashMap.newKeySet();
  private final CopyOnWriteArrayList<ServerConnection> connections = new CopyOnWriteArrayList<>();
  private final DTOConverterManager converterManager;
  private final CommandSupplier commandSupplier;
  private final ServerSocket serverSocket;
  private final ExecutorService connectionsAccepterExecutor = Executors.newFixedThreadPool(2);

  private final ExecutorService connectionsPool = Executors.newCachedThreadPool();
  private final ExecutorService expiredConnectionsDeleter = Executors.newSingleThreadExecutor();
  private AtomicBoolean serveryPizda = new AtomicBoolean(false);

  public Server(Properties properties) throws IOException {
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
        log.info(STR."serwer worker: \{con.getConnectionName()} started");
        while (!connection.isClosed() && !Thread.currentThread().isInterrupted()) {
          try {
            byte[] msg = con.receiveMessage();
            log.info(STR."new message \{new String(msg)}");
            final Document xmlTree = XMLDTOConverterManager.getXMLTree(msg);
            final RequestDTO.DTO_TYPE dtoType = DTOConverterManagerInterface.getDTOType(xmlTree);
            final RequestDTO.DTO_SECTION dtoSection = DTOConverterManagerInterface.getDTOSection(xmlTree);
//            todo not support events getting
            log.info(STR."new message for \{dtoSection} with type \{dtoType}");
            if (dtoType == null || dtoSection == null) {
//            todo make in XMLDTOConverterManager support of base commands
              connection.sendMessage(XMLDTOConverterManager.serialize(
                      new RequestDTO.BaseErrorResponse("unhandled message in server protocol")
              ).getBytes());
            } else {
              var section = commandSupplier.getSection(dtoSection);
              log.info(STR."section \{section} started");
              section.perform(connection, xmlTree, dtoType, dtoSection);
            }

          } catch (UnableToDeserialize e) {
            log.info(e.getMessage(), e);
          } catch (SocketTimeoutException _) {
            /*todo handle ex*/
          }
        }
      } catch (IOException e) {
        log.warn(e.getMessage(), e);
      } finally {
        log.info(STR."server worker: \{connection.getConnectionName()} finish session");
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
    log.info(STR."try submit expired connection \{connection.getConnectionName()}");
    connection.markAsExpired();
    expiredConnections.add(connection);
    log.info(STR."\{connection} submit expired connection");
  }

  public void submitNewConnection(ServerConnection connection) {
    log.info(STR."try submit new connection \{connection.getConnectionName()}");
    connections.add(connection);
    connectionsPool.submit(new ServerWorker(connection, this));
    log.info(STR."submit new connection \{connection.getConnectionName()}");
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

  public ConcurrentHashMap<String, Integer> getRegisteredUsers() {
    return registeredUsers;
  }


  public Logger getModuleLogger() {
    return moduleLogger;
  }

  public static Logger getLog() {
    return Server.log;
  }
}
