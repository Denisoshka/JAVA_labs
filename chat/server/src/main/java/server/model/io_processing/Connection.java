package server.model.io_processing;

import org.slf4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Connection implements Runnable, AutoCloseable {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Connection.class);
  private final Socket socket;
  private SessionIOProcessor ioProcessor;
  private volatile boolean expired;

  public Connection(String ipaddr, int port) throws IOException {
    this.socket = new Socket(ipaddr, port);
  }

  @Override
  public void run() {
    try (DataInputStream receiveStream = new DataInputStream(socket.getInputStream());
         DataOutputStream sendStream = new DataOutputStream(socket.getOutputStream())) {
      ioProcessor = new SessionIOProcessor(receiveStream, sendStream);
      while (!socket.isClosed()
              && !Thread.currentThread().isInterrupted()) {
      }
    } catch (IOException e) {
//    todo need to make ex handle
    }
  }

  public boolean isClosed() {
    return socket.isClosed();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return Objects.equals(socket, that.socket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(socket);
  }

  @Override
  public void close() throws IOException {
    socket.close();
  }

  public void markAsExpired() {
    expired = true;
  }

  public boolean isExpired() {
    return expired;
  }

  public SessionIOProcessor getIoProcessor() {
    return ioProcessor;
  }
}

