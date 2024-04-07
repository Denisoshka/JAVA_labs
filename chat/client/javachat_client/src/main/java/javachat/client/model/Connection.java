package javachat.client.model;

import javachat.client.model.message_handler.MessageHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Connection implements Runnable, AutoCloseable {
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
          CommandInterface command = handler.handleMessage(msg);
          if (command == null) {
            handler.sendMessage(sendStream, ServerMSG.getError("unsupported command"));
          } else {
            command.perform(this, contextExecutor, handler, msg);
          }
        } catch (UnableToDecodeMessage e) {
          handler.sendMessage(this, ServerMSG.getError(e.getMessage()));
        } catch (IOException e) {
          handler.sendMessage(this, ServerMSG.getError(e.getMessage()));
          contextExecutor.submitExpiredConnection(this);
          return;
        }
      }
    } catch (IOException e) {
      return;
//      todo need to make ex handle
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
}

