package server.model;

import dto.DTOConverterManager;
import dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import server.model.io_processing.Connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server {
  private static final String IP_ADDRESS = "ip_address";
  private static final String PORT = "port";
  private static final long MAX_CONNECTION_TIME = 60_000;

  private final ConcurrentHashMap.KeySetView<Connection, Boolean> expiredConnections = ConcurrentHashMap.newKeySet();
  private final CopyOnWriteArrayList<Connection> connections = new CopyOnWriteArrayList<>();

  private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
  private final DTOConverterManager converterManager;
  private final ServerSocket serverSocket;
  //  todo
  private final ExecutorService expiredConnectionDeleter;
  private final ExecutorService chatExchangeExecutor;
  private final ExecutorService connectionAcceptExecutor;

  private final Properties registeredUsers;

  Server(Properties properties) throws IOException {
    converterManager = new DTOConverterManager(null /*todo fix this*/);
    serverSocket = new ServerSocket();
    try {
      serverSocket.bind(
              new InetSocketAddress(Objects.requireNonNull(properties.getProperty(IP_ADDRESS)),
                      Integer.parseInt(Objects.requireNonNull(properties.getProperty(PORT))))
      );
    } catch (IOException e) {
      serverSocket.close();
    }
    this.registeredUsers = new Properties();
    this.chatExchangeExecutor = Executors.newCachedThreadPool();
    this.connectionAcceptExecutor = Executors.newFixedThreadPool(2);
    this.expiredConnectionDeleter = Executors.newSingleThreadExecutor();
  }

  private static class ServerWorker implements Runnable {
    private final Connection connection;
    private final DTOConverterManager XMLDTOConverterManager;

    public ServerWorker(Connection connection, DTOConverterManager XMLDTOConverterManager) {
      this.connection = connection;
      this.XMLDTOConverterManager = XMLDTOConverterManager;
    }

    @Override
    public void run() {
      try (Connection con = connection) {
        while (!connection.isClosed() && !Thread.currentThread().isInterrupted()) {
          final var xmlTree = XMLDTOConverterManager.getXMLTree(con.receiveMessage());
          final var type = XMLDTOConverterManager.getDTOType(xmlTree);

          if (type == null) {
            connection.sendMessage(XMLDTOConverterManager.serialize(
                    new RequestDTO.BaseErrorResponse(null, "unhandled message in server protocol")).getBytes()
            );
          } else if (type != RequestDTO.DTO_TYPE.COMMAND) {
            connection.sendMessage(XMLDTOConverterManager.serialize(
                    new RequestDTO.BaseErrorResponse(null, "server support only COMMAND messages")).getBytes()
            );
          } else {



          }
        }
      } catch (IOException _) {

      }
    }
  }
}
