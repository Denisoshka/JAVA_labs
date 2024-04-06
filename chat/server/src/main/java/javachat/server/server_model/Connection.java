package javachat.server.server_model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Connection implements AutoCloseable, Runnable {
  public final static int BUFFERSIZE = 41_943_040;
  private final Socket clSocket;
  private final MessageHandler handler;
  private final String connectionName;

  private DataOutputStream sendStream = null;
  private DataInputStream receiveStream = null;

  public Connection(MessageHandler handler, String connectionName, Socket clSocket) {
    this.connectionName = connectionName;
    this.clSocket = clSocket;
    this.handler = handler;
  }

  @Override
  public void run() {
    try (DataOutputStream outStream = new DataOutputStream(clSocket.getOutputStream());
         DataInputStream inStream = new DataInputStream(clSocket.getInputStream());
    ) {
      while (!clSocket.isClosed()) {
        var msg = handler.receiveMessage(inStream);
        MessageHandler.Command com = handler.handleMessage(msg);
        com.perform(this, );
//  todo
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public DataOutputStream getSendStream() {
    return sendStream;
  }


  public DataInputStream getReceiveStream() {
    return receiveStream;
  }

  @Override
  public void close() throws Exception {
    clSocket.close();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Connection that = (Connection) o;
    return Objects.equals(clSocket, that.clSocket);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clSocket);
  }
}