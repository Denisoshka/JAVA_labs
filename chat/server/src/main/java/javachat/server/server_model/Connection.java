package javachat.server.server_model;

import javachat.server.exceptions.UnableToDecodeMessage;
import javachat.server.server_model.message_handler.CommandInterface;
import javachat.server.server_model.message_handler.MessageHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Connection implements Runnable, AutoCloseable {
  private final Socket socket;
  private final Server server;
  private final String name;
  private final MessageHandler handler;
  DataInputStream receiveStream = null;
  DataOutputStream sendStream = null;

  public Connection(Socket socket, Server server, MessageHandler handler, String name) {
    this.socket = socket;
    this.server = server;
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

        } catch (UnableToDecodeMessage e) {
          handler.sendMessage(this, ServerMSG.getError(e.getMessage()));
        } catch (IOException e) {
          handler.sendMessage(this, ServerMSG.getError(e.getMessage()));
          server.submitExpiredConnection(this);
          return;
        }
      }
    } catch (IOException e) {
//      todo
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
