package javachat.server.server_model;

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

  DataInputStream receiveStream = null;
  DataOutputStream sendStream = null;

  public Connection(Socket socket, Server server, MessageHandler handler, String name) {
    this.socket = socket;
    this.server = server;
    this.name = name;
  }

  @Override
  public void run() {
    try (DataInputStream receiveStream = new DataInputStream(socket.getInputStream());
         DataOutputStream sendStream = new DataOutputStream(socket.getOutputStream())) {
      while (!socket.isClosed()) {


      }
    } catch (IOException e) {
//      todo
    }
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
