package javachar.server.server_model;

import javachar.server.exceptions.UnableToCreateServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionService implements AutoCloseable, Runnable {
  private final static String IP_ADDRESS = "ip_adress";
  private final static String MAX_USERS_QUANTITY = "max_users_quantity";
  private final static String MAX_HANDLED_EVENTS_QUANTITY = "max_handled_events_quantity";
  private final static String PORT = "port";

  private final Properties properties;
  private final ServerSocket srvSocket;
  private final ExecutorService handleEventsService;
  private final List<ExchangeService.Connection> connections;
  private final int maxHandledUsers;

  public ConnectionService(Properties properties) {
    this.properties = properties;
    try {
      this.srvSocket = new ServerSocket();
      this.srvSocket.bind(new InetSocketAddress(properties.getProperty(IP_ADDRESS), Integer.parseInt(properties.getProperty(PORT))));

      this.maxHandledUsers = Integer.parseInt(properties.getProperty(MAX_USERS_QUANTITY));
      this.handleEventsService = Executors.newFixedThreadPool(Integer.parseInt(properties.getProperty(MAX_HANDLED_EVENTS_QUANTITY)));

    } catch (IOException e) {
      throw new UnableToCreateServer("on port: " + properties.getProperty(PORT), e);
    } catch (NumberFormatException e) {
      throw new UnableToCreateServer("incorrect config file", e);
    }
    this.connections = new LinkedList<>();
  }

  @Override
  public void close() throws Exception {
    handleEventsService.shutdownNow();
    for (var sock : connections) {
      sock.close();
    }
    srvSocket.close();
  }

  @Override
  public void run() {
    while (true) {

//    srvSocket.accept()
    }
  }
}
