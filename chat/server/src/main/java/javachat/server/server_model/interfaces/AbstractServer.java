package javachat.server.server_model.interfaces;

import javachat.server.exceptions.ServerRegistrationException;
import javachat.server.server_model.Connection;

import java.util.Set;

public interface AbstractServer {
  boolean registerNewConnection(String name, String password) throws ServerRegistrationException;

  void addConnection(Connection conn);

  void tearConnection(Connection conn);

  boolean addBrokenConnection(Connection conn);

  Set<Connection> getConnections();
}
