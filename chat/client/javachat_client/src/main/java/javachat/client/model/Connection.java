package javachat.client.model;

import javachat.client.exception.UnableToDecodeMessage;
import javachat.client.model.event_handler.EventInterface;
import javachat.client.model.event_handler.MessageHandler;
import org.slf4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;


public class Connection implements Runnable, AutoCloseable {
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(Connection.class);
  private final String name;
  private boolean expired;
  private final Socket socket;
  private final ContextExecutor contextExecutor;
  private final MessageHandler handler;
  DataInputStream receiveStream = null;
  DataOutputStream sendStream = null;

  public Connection(Socket socket, ContextExecutor executor, MessageHandler handler, String name) {
    this.socket = socket;
    this.contextExecutor = executor;
    this.handler = handler;
    this.name = name;
  }

  @Override
  public void run() {
    try (DataInputStream receiveStream = new DataInputStream(socket.getInputStream());
         DataOutputStream sendStream = new DataOutputStream(socket.getOutputStream())) {
      this.receiveStream = receiveStream;
      this.sendStream = sendStream;
      while (!socket.isClosed()) {
        try {
          var msg = handler.receiveMessage(this);
          EventInterface command = handler.handleMessage(msg);
          command.perform(this, contextExecutor, handler, msg);
        } catch (UnableToDecodeMessage e) {
          log.info(e.getMessage(), e);
//        todo
        }
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
    receiveStream = null;
    sendStream = null;
    socket.close();
  }

  public void markAsExpired() {
    expired = true;
  }

  public boolean isExpired() {
    return expired;
  }

  public DataInputStream getReceiveStream() {
    return receiveStream;
  }

  public DataOutputStream getSendStream() {
    return sendStream;
  }

  public String getName() {
    return name;
  }

  public static Logger getLog() {
    return Connection.log;
  }
}

