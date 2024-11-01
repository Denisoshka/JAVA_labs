package server.model.connection_section;

import io_processing.IOProcessor;
import org.slf4j.Logger;
import server.model.Server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Objects;

public class ServerConnection implements AbstractServerConnection, AutoCloseable {

  private final String connectionName;
  private final IOProcessor ioProcessor;
  private volatile boolean expired;

  public ServerConnection(IOProcessor ioProcessor, String connectionName) {
    this.connectionName = connectionName;
    this.ioProcessor = ioProcessor;
  }

  public ServerConnection(IOProcessor ioProcessor, String connectionName, int soTimeout) throws IOException {
    this(ioProcessor, connectionName);
    ioProcessor.setSoTimeout(soTimeout);
  }

  @Override
  public void close() throws IOException {
    ioProcessor.close();
  }

  @Override
  public byte[] receiveMessage() throws IOException {
    return ioProcessor.receiveMessage();
  }

  @Override
  public void sendMessage(byte[] message) throws IOException {
    ioProcessor.sendMessage(message);
  }

  @Override
  public void sendBroadcastMessage(Server server, byte[] msg, Logger log) {
    for (var conn : server.getConnections()) {
      try {
        conn.sendMessage(msg);
      } catch (IOException e) {
        if (log != null) log.error(e.getMessage(), e);
        server.submitExpiredConnection(conn);
      }
    }
  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public void setSoTimeout(int timeout) throws SocketException {
    ioProcessor.setSoTimeout(timeout);
  }

  @Override
  public boolean isExpired() {
    return expired;
  }

  @Override
  public void markAsExpired() {
    expired = true;
  }

  public String getConnectionName() {
    return connectionName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ServerConnection that)) return false;
    return expired == that.expired && Objects.equals(connectionName, that.connectionName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connectionName, expired);
  }
}

