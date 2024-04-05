package javachat.server.server_model;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public interface AbstractServer {
  void setConnection(Socket socket) throws IOException;
  void tearConnection(Connection conn) throws Exception;
  Set<Connection> getConnections();
}
