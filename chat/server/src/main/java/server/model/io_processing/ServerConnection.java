package server.model.io_processing;

import io_processing.IOProcessor;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class Connection implements AbstractConnection, AutoCloseable {
  private final Logger log = org.slf4j.LoggerFactory.getLogger(Connection.class);

  private final String connectionName;
  private final IOProcessor ioProcessor;
  private volatile boolean expired;

  public Connection(IOProcessor ioProcessor, String connectionName) throws IOException {
    this.connectionName = connectionName;
    this.ioProcessor = ioProcessor;
  }

  @Override
  public void close() throws IOException {
    ioProcessor.close();
  }

  public String getConnectionName() {
    return connectionName;
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
  public boolean isClosed() {
    return false;
  }

  @Override
  public boolean isExpired() {
    return expired;
  }

  @Override
  public void markAsExpired() {
    expired = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Connection that)) return false;
    return Objects.equals(connectionName, that.connectionName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(connectionName);
  }
}

